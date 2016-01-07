package app.hoai.bkit4u.home4u.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import app.hoai.bkit4u.home4u.R;
import app.hoai.bkit4u.home4u.controller.PreferencesController;
import app.hoai.bkit4u.home4u.thread.TcpClientConfigThread;

/**
 * Created by hoaipc on 11/5/15.
 */
public class ConfigFragment extends BaseFragment
{
    EditText editWifiName;
    EditText editWifiPass;
    EditText editDeviceName;
    EditText editGatewayIp;
    Button btnConfig;
    Button btnConnect;
    TextView mDeviceType;
    ProgressBar mProgress;
    View mProgressContainer;
    TcpClientConfigThread mTcpClient;
    View rootView;
    View mConnectView;
    View mConfigView;
    TCPTask mTCPTask;
    String GW_IP;
    final int GW_PORT = 5000;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View getLayout(LayoutInflater inflater)
    {
        rootView = inflater.inflate(R.layout.config_fragment_layout, null);

        mConnectView = rootView.findViewById(R.id.connect_container);
        editGatewayIp = (EditText) rootView.findViewById(R.id.edit_ip);
        btnConnect = (Button) rootView.findViewById(R.id.btn_connect);

        btnConnect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                GW_IP = editGatewayIp.getText().toString();
                mTCPTask = new TCPTask();
                mTCPTask.execute("");
            }
        });

        mConfigView = rootView.findViewById(R.id.config_container);
        editDeviceName = (EditText) rootView.findViewById(R.id.edit_device_name);
        editWifiName = (EditText) rootView.findViewById(R.id.edit_name);
        mDeviceType = (TextView) rootView.findViewById(R.id.device_type);
        editWifiPass = (EditText) rootView.findViewById(R.id.edit_password);
        btnConfig = (Button) rootView.findViewById(R.id.btn_config);

        mProgress = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mProgressContainer = rootView.findViewById(R.id.progress_container);
        mProgressContainer.setVisibility(View.GONE);

        String wifiName = PreferencesController.getInstance().getWifiName();
        String wifiPass = PreferencesController.getInstance().getWifiPass();

        Log.d("restore", wifiName + "/" + wifiPass + "/" + getContext());

        if (!wifiName.equals("")) editWifiName.setText(wifiName);
        if (!wifiPass.equals("")) editWifiPass.setText(wifiPass);

        btnConfig.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                onConfiguring();

                String wifiName = editWifiName.getText().toString();
                String wifiPass = editWifiPass.getText().toString();
                String deviceName = editDeviceName.getText().toString();

                JsonObject json = new JsonObject();
                json.addProperty("wifi_name",wifiName);
                json.addProperty("wifi_pass",wifiPass);
                json.addProperty("device_name",deviceName);

                if (mTcpClient != null)
                {
                    mTcpClient.sendMessage(json.toString());
                }

                Log.d("Home4u", wifiName + "/" + wifiPass + "/" + json.toString());

                PreferencesController.getInstance().setWifiName(wifiName);
                PreferencesController.getInstance().setWifiPass(wifiPass);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStop()
    {
        Log.d("Home4U", "onStop");
        super.onStop();
        if (mTcpClient != null) mTcpClient.stopClient();
    }

    void onConfiguring()
    {
        mProgressContainer.setVisibility(View.VISIBLE);
    }

    void onConfiguringCompleted()
    {
        mProgressContainer.setVisibility(View.GONE);
        if (mTcpClient != null) mTcpClient.stopClient();
        Snackbar.make(getView(), "Config successfully!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        if (mOnFragmentChangeListener != null)
        {
            mOnFragmentChangeListener.onHomeRequest();
        }
    }

    void onConfiguringError()
    {
        mProgressContainer.setVisibility(View.GONE);
    }

    public class TCPTask extends AsyncTask<String, String, TcpClientConfigThread>
    {

        @Override
        protected TcpClientConfigThread doInBackground(String... message)
        {

            //we create a TCPClient object and
            mTcpClient = new TcpClientConfigThread(new TcpClientConfigThread.OnMessageReceived()
            {
                @Override
                public void onMessage(String message)
                {
                    //this method calls the onProgressUpdate
                    publishProgress("message", message);
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

            }, GW_IP, GW_PORT, "config");
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values)
        {
            super.onProgressUpdate(values);

            if (values[0].equals("connected"))
            {
                onConnected();
            }
            else if (values[0].equals("message"))
            {
//                Log.d("Home4U-TCP Response", values[1]);
                if (values[1].equals("OK"))
                {
                    onConfiguringCompleted();
                }
                else
                {
                    try
                    {
                        JSONObject json = new JSONObject(values[1]);
                        String type = json.get("type").toString().toUpperCase();
                        mDeviceType.setText(type);
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    void onConnected()
    {
        mConnectView.setVisibility(View.GONE);
        mConfigView.setVisibility(View.VISIBLE);
        Snackbar.make(getView(), "Connected!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
