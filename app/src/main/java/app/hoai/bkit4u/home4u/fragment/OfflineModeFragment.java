package app.hoai.bkit4u.home4u.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import app.hoai.bkit4u.home4u.R;
import app.hoai.bkit4u.home4u.adapter.DeviceOfflineAdapter;
import app.hoai.bkit4u.home4u.controller.NetworkController;
import app.hoai.bkit4u.home4u.model.DeviceActionModel;
import app.hoai.bkit4u.home4u.model.DeviceOfflineModel;
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
    DeviceOfflineAdapter mAdapter;
    View mProgressContainer;
    TcpClientThread mTcpClient;
    UdpReceiverThread mUdpReceiverThread;
    UdpSendingThread mUdpSendingThread;
    TcpServerThread mTcpServer;

    View mConnectView;
    EditText mEditIp;
    EditText mEditPort;
    Button mBtnConnect;
    Button mBtnReqquest;
    View mContentView;
    TCPTask mTcpTask;

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
        mProgressContainer = rootView.findViewById(R.id.progress_container);
        mProgressContainer.setVisibility(View.GONE);
        mConnectView = rootView.findViewById(R.id.connect_container);
        mEditIp = (EditText) rootView.findViewById(R.id.edit_ip);
        mEditPort = (EditText) rootView.findViewById(R.id.edit_port);
        mBtnConnect = (Button) rootView.findViewById(R.id.btn_connect);
        mBtnReqquest = (Button) rootView.findViewById(R.id.btn_request);
        mContentView = rootView.findViewById(R.id.content_container);

        mAdapter = new DeviceOfflineAdapter();
        mAdapter.setListener(new DeviceOfflineAdapter.OnSendCommandListener()
        {
            @Override
            public void OnSendCommand(String string)
            {
                if (mTcpClient != null) mTcpClient.stopClient();

                mTcpTask = new TCPTask();
                mTcpTask.execute(string);
            }
        });
        mListView.setAdapter(mAdapter);
        mListView.setDividerHeight(0);

        mBtnConnect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("Home4U", "On connect");
                GW_IP = mEditIp.getText().toString();
                GW_PORT = Integer.parseInt(mEditPort.getText().toString());
                mProgressContainer.setVisibility(View.VISIBLE);

                mTcpTask = new TCPTask();
                mTcpTask.execute(NetworkController.getInstance().getRequestDataGateWayString());
            }
        });

        mBtnReqquest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mUdpSendingThread != null)
                    mUdpSendingThread.sendString(NetworkController.getInstance().getBroadcastString(), "255.255.255.255", 2000);
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
            public void OnGetMessage(final String... responses)
            {
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Log.d("Home4U-TCP",responses[0]+responses[1]);
                        try
                        {
                            JSONObject json = new JSONObject(responses[1]);

                            if(responses[1].indexOf("actionID") == -1)
                            {
                                DeviceOfflineModel device = new DeviceOfflineModel(json.getString("deviceID"), json.getString("name"), json.getString("type"));
                                mAdapter.addDevice(device);
                            } else
                            {
                                DeviceActionModel action = DeviceActionModel.createAction(json.getString("actionID"),
                                        json.getString("deviceID"), json.getString("name"), null);
                                mAdapter.addAction(action);
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
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
                Log.d("Home4U-UDP offline", "Address " + address);

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

            }, GW_IP, GW_PORT, message[0]);
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values)
        {
            super.onProgressUpdate(values);
            if (values[0].equals("connected"))
                onConnected();
        }
    }

    void onConnected()
    {
        mConnectView.setVisibility(View.GONE);
        mContentView.setVisibility(View.VISIBLE);
        mProgressContainer.setVisibility(View.GONE);
    }

    @Override
    public void onStop()
    {
        mUdpSendingThread.kill();
        mUdpReceiverThread.kill();
        mTcpServer.stopServer();
        if (mTcpClient != null) mTcpClient.stopClient();
        super.onStop();
    }
}
