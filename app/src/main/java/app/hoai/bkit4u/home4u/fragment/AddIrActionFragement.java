package app.hoai.bkit4u.home4u.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import app.hoai.bkit4u.home4u.R;
import app.hoai.bkit4u.home4u.controller.NetworkController;
import app.hoai.bkit4u.home4u.thread.TcpClientThread;
import app.hoai.bkit4u.home4u.thread.UdpReceiverThread;
import app.hoai.bkit4u.home4u.thread.UdpSendingThread;

/**
 * Created by hoaipc on 12/21/15.
 */
public class AddIrActionFragement extends BaseFragment
{
    EditText editActionName;
    TextView gatewayIpText;
    TextView gatewayPortText;

    Button btnAdd;
    Button btnConnect;
    Button btnRequest;

    ProgressBar mProgress;
    View mProgressContainer;
    TcpClientThread mTcpClient;
    View mConnectView;
    View mConfigView;

    String GW_IP;
    final int GW_PORT = 2500;

    String mDeviceID;

    UdpReceiverThread mReceiverThread;
    UdpSendingThread mUdpSendingThread;
    TCPTask mTCPTask;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("Home4U-fragment", "oncreate!");
    }

    @Override
    public View getLayout(LayoutInflater inflater)
    {
        Log.d("Home4U-fragment", "on get layout");
        View rootView = inflater.inflate(R.layout.add_ir_action_fragment_layout, null);

        mConnectView = rootView.findViewById(R.id.connect_container);
        gatewayIpText = (TextView) rootView.findViewById(R.id.edit_ip);
        gatewayPortText = (TextView) rootView.findViewById(R.id.edit_port);
        btnConnect = (Button) rootView.findViewById(R.id.btn_connect);
        btnRequest = (Button) rootView.findViewById(R.id.btn_request);
        btnRequest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mUdpSendingThread != null)
                    mUdpSendingThread.sendString(NetworkController.getInstance().getBroadcastString(), "255.255.255.255", 2000);
            }
        });

        btnConnect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                GW_IP = gatewayIpText.getText().toString();
                mProgressContainer.setVisibility(View.VISIBLE);
                mTCPTask = new TCPTask();
                mTCPTask.execute("");
            }
        });

        mConfigView = rootView.findViewById(R.id.config_container);
        editActionName = (EditText) rootView.findViewById(R.id.edit_name);
        btnAdd = (Button) rootView.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String actionName = editActionName.getText().toString();
                String requestString = NetworkController.getInstance().getAddActionString(mDeviceID, actionName);
                if (mTcpClient != null)
                {
                    mTcpClient.sendMessage(requestString);
                }

                if (mOnFragmentChangeListener != null)
                {
                    mTcpClient.stopClient();
                    mOnFragmentChangeListener.onHomeRequest();
                }
            }
        });

        mProgress = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mProgressContainer = rootView.findViewById(R.id.progress_container);
        mProgressContainer.setVisibility(View.GONE);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Log.d("Home4U-fragment", "on activity create!");

        mReceiverThread = new UdpReceiverThread(new UdpReceiverThread.OnResponseListener()
        {
            @Override
            public void OnGetMessage(String... responses)
            {
                final String address = responses[0];
                final String message = responses[1];
                Log.d("Home4U-UDP", "Adress " + address);

                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (message.equals("OK"))
                        {
                            GW_IP = address;
                            gatewayIpText.setText(address);
                            mReceiverThread.kill();
                            mUdpSendingThread.kill();
                        }
                    }
                });
            }
        });

        mReceiverThread.start();

        mUdpSendingThread = new UdpSendingThread();
        mUdpSendingThread.start();
        mUdpSendingThread.sendString(NetworkController.getInstance().getBroadcastString(), "255.255.255.255", 2000);
    }

    public class TCPTask extends AsyncTask<String, String, TcpClientThread>
    {

        @Override
        protected TcpClientThread doInBackground(String... message)
        {

            //we create a TCPClient object and
            mTcpClient = new TcpClientThread(new TcpClientThread.OnMessageReceived()
            {
                @Override
                public void onMessage(String message)
                {
                    //this method calls the onProgressUpdate
                    publishProgress("Received " + message);
                }

                @Override
                public void onConnecting()
                {
                    publishProgress("connecting");
                }

                @Override
                public void onConnected()
                {
                    publishProgress("connected");
                }

                @Override
                public void onError()
                {
                    publishProgress("error");
                }

            }, GW_IP, GW_PORT, "");
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values)
        {
            super.onProgressUpdate(values);
            if (values[0].equals("connected"))
                onConnected();
            Log.d("TCP Status!", values[0]);
        }
    }

    void onConnected()
    {
        mProgressContainer.setVisibility(View.GONE);
        mConnectView.setVisibility(View.GONE);
        mConfigView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        Log.d("Home4U", "on detach");
        mUdpSendingThread.kill();
        mReceiverThread.kill();
        if (mTcpClient != null) mTcpClient.stopClient();
    }

    public void setDeviceID(String mDeviceID)
    {
        this.mDeviceID = mDeviceID;
    }
}
