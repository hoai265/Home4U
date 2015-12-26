package app.hoai.bkit4u.home4u.thread;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by hoaipc on 12/15/15.
 */
public class TcpClientThread extends Thread
{
    private String serverMessage;
    private String requestString;
    private String SERVERIP = "192.168.4.1";
    private int SERVERPORT = 5000;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;

    PrintWriter out;
    BufferedReader in;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TcpClientThread(OnMessageReceived listener, String ip, int port, String request)
    {
        this.SERVERIP = ip;
        this.SERVERPORT = port;
        this.mMessageListener = listener;
        this.requestString = request;
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public void sendMessage(String message)
    {
        if (out != null && !out.checkError())
        {
            Log.e("Home4U-TCP Send", "Sending data "+message);
            out.println(message);
            out.flush();
        }
    }

    public void stopClient()
    {
        mRun = false;
    }

    public void run()
    {

        mRun = true;

        try
        {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);

            Log.d("Home4U-TCP", "Connecting...");
            mMessageListener.onConnecting();

            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVERPORT);

            try
            {

                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                if (!requestString.isEmpty())
                {
                    out.println(requestString);
                    out.flush();
                    Log.e("Home4U-TCP Send", "Sending data " + requestString);
                }

                Log.d("Home4U-TCP", "Done.");
                mMessageListener.onConnected();
                while(mRun)
                {
//                    Log.e("Home4U-TCP","thread tcp running!");
                }
            } catch (Exception e)
            {

                Log.d("Home4U-TCP", "Error", e);
                mMessageListener.onError();

            } finally
            {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }

        } catch (Exception e)
        {

            Log.e("Home4U-TCP", "Error", e);
            mMessageListener.onError();

        }

    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived
    {
        void messageReceived(String message);

        void onConnecting();

        void onConnected();

        void onError();
    }
}
