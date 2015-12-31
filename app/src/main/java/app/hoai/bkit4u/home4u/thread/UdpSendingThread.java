package app.hoai.bkit4u.home4u.thread;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by hoaipc on 11/10/15.
 */
public class UdpSendingThread extends Thread
{
    private boolean runningState = true;
    private boolean sendingState = false;
    private int sendPort;
    private DatagramSocket mSocket = null;
    private DatagramPacket sendPacket;
    private InetAddress IPAddress;
    private byte[] sendingBytes;
    private String sendingString = "";

    public void run()
    {
        while (runningState)
        {
//            Log.e("Home4U-UDP","thread udp send running!");
            if (sendingState)
            {
                Log.d("Home4U-UDP","send UDP");
                sendingBytes = sendingString.getBytes();
                sendPacket = new DatagramPacket(sendingBytes, sendingString.length(), IPAddress, sendPort);

                try
                {
                    mSocket = new DatagramSocket();
                } catch (SocketException e)
                {
                    e.printStackTrace();
                }

                try
                {
                    mSocket.send(sendPacket);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }


                if (mSocket != null)
                {
                    mSocket.close();
                }
                sendingState = false;
            }
        }
    }

    public void sendString(String str, String serverIp, int port)
    {
        sendingString = str;
        sendPort = port;
        try
        {
            IPAddress = InetAddress.getByName(serverIp);
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }

        sendingState = true;
    }

    public void kill()
    {
        runningState = false;
    }
}
