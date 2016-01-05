package app.hoai.bkit4u.home4u.thread;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoaipc on 12/25/15.
 */
public class TcpServerThread extends Thread
{
    final int SERVER_PORT = 2500;
    ServerSocket mServerSocket;
    List<Client> mUserList = new ArrayList<>();
    OnResponseListener mListener;
    boolean IsStop = false;

    @Override
    public void run()
    {
        Socket socket = null;

        try
        {
            mServerSocket = new ServerSocket(SERVER_PORT);
            while (!mServerSocket.isClosed())
            {
                Log.e("Home4U", "TCP Server running!");
                socket = mServerSocket.accept();
                Client client = new Client();
                mUserList.add(client);
                ConnectThread connectThread = new ConnectThread(client, socket, new OnResponseListener()
                {
                    @Override
                    public void OnGetMessage(String... responses)
                    {
                        if (mListener != null)
                        {
                            mListener.OnGetMessage(responses);
                        }
                    }
                });

                connectThread.start();
            }

            Log.d("Home4U","Close TCP Server!");

        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            if (socket != null)
            {
                try
                {
                    socket.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stopServer()
    {
        Log.d("Home4U","Stop TCP Server!");
        IsStop = true;
        try
        {
            mServerSocket.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    private class ConnectThread extends Thread
    {

        Socket socket;
        Client connectClient;
        OnResponseListener mListener;
        boolean IsClose = false;

        ConnectThread(Client client, Socket socket, OnResponseListener listener)
        {
            connectClient = client;
            this.socket = socket;
            client.socket = socket;
            client.mThread = this;
            this.mListener = listener;
        }

        @Override
        public void run()
        {
            DataInputStream dataInputStream = null;

            try
            {
                dataInputStream = new DataInputStream(socket.getInputStream());
                while (!IsClose)
                {
                    if (dataInputStream.available() > 0)
                    {
                        String newMsg = dataInputStream.readLine().trim();
                        if (mListener != null && !newMsg.isEmpty())
                        {
                            mListener.OnGetMessage("message", newMsg);
                            closeConnection();
                        }
                    }
                }

                Log.d("Home4U","Close connection");
            } catch (IOException e)
            {
                e.printStackTrace();
            } finally
            {
                if (dataInputStream != null)
                {
                    try
                    {
                        dataInputStream.close();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

        }

        public void closeConnection()
        {
            IsClose = true;
        }
    }

    class Client
    {
        Socket socket;
        ConnectThread mThread;
    }

    public interface OnResponseListener
    {
        void OnGetMessage(String... responses);
    }

    public void setListener(OnResponseListener mListener)
    {
        this.mListener = mListener;
    }
}
