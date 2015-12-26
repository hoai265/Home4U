package app.hoai.bkit4u.home4u.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import app.hoai.bkit4u.home4u.R;
import app.hoai.bkit4u.home4u.adapter.DeviceAdapter;
import app.hoai.bkit4u.home4u.controller.NetworkController;
import app.hoai.bkit4u.home4u.thread.TcpClientThread;
import app.hoai.bkit4u.home4u.thread.TcpServerThread;
import app.hoai.bkit4u.home4u.thread.UdpReceiverThread;
import app.hoai.bkit4u.home4u.thread.UdpSendingThread;

/**
 * Created by hoaipc on 12/14/15.
 */
public class OfflineModeFragment extends BaseFragment
{
    ListView mListView;
    DeviceAdapter mAdapter;
    ProgressBar mProgress;
    View mProgressContainer;
    TcpClientThread mTcpClient;
    UdpReceiverThread mUdpReceiverThread;
    UdpSendingThread mUdpSendingThread;
    TcpServerThread mTcpServer;

    View mConnectView;
    EditText mEditIp;
    EditText mEditPort;
    Button mBtnConnect;
    View mContentView;

    String GW_IP;
    int GW_PORT;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View getLayout(LayoutInflater inflater)
    {
        View rootView = inflater.inflate(R.layout.offline_mode_fragment_layout, null);

        mListView = (ListView) rootView.findViewById(R.id.baseListView);
        mProgress = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mProgress.setVisibility(View.GONE);
        mProgressContainer = rootView.findViewById(R.id.progress_container);

        mConnectView = rootView.findViewById(R.id.connect_container);
        mEditIp = (EditText) rootView.findViewById(R.id.edit_ip);
        mEditPort = (EditText) rootView.findViewById(R.id.edit_port);
        mBtnConnect = (Button) rootView.findViewById(R.id.btn_connect);
        mContentView = rootView.findViewById(R.id.content_container);

        mAdapter = new DeviceAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setDividerHeight(0);

        mBtnConnect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                GW_IP = mEditIp.getText().toString();
                GW_PORT = Integer.parseInt(mEditPort.getText().toString());
                mProgressContainer.setVisibility(View.VISIBLE);
                new TCPTask().execute("");
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mTcpServer = new TcpServerThread();
        mTcpServer.setListener(new TcpServerThread.OnResponseListener()
        {
            @Override
            public void OnGetMessage(String... responses)
            {
                Log.d("Home4U-TCP Offline", responses[0] + responses[1]);
            }
        });
        mTcpServer.start();

        mUdpReceiverThread = new UdpReceiverThread(new UdpReceiverThread.OnResponseListener()
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
                            mEditIp.setText(address);
                            mUdpReceiverThread.kill();
                            mUdpSendingThread.kill();
                        }
                    }
                });
            }
        });

        mUdpReceiverThread.start();

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
                public void messageReceived(String message)
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

            }, GW_IP, GW_PORT, NetworkController.getInstance().getRequestDataGateWayString());
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
        mConnectView.setVisibility(View.GONE);
        mContentView.setVisibility(View.VISIBLE);
        mProgressContainer.setVisibility(View.GONE);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mUdpSendingThread.kill();
        mUdpReceiverThread.kill();
    }
}
