package app.hoai.bkit4u.home4u.thread;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by hoaipc on 11/10/15.
 */
public class UdpReceiverThread extends Thread
{
    private boolean bKeepRunning = true;
    private static final int DEFAULT_PORT = 2000;
    OnResponseListener mListener;

    public UdpReceiverThread(OnResponseListener listener)
    {
        this.mListener = listener;
    }

    public void run()
    {
        String message;
        byte[] lmessage = new byte[100];
        DatagramPacket packet = new DatagramPacket(lmessage, lmessage.length);
        DatagramSocket socket = null;
        while (bKeepRunning)
        {
//            Log.e("Home4U-TCP","thread udp recei unning!");
            try
            {
                socket = new DatagramSocket(DEFAULT_PORT);

            } catch (SocketException e)
            {
                e.printStackTrace();
            }

            try
            {
                socket.receive(packet);
                message = new String(lmessage, 0, packet.getLength());
                message = message.trim();
                String addr = packet.getAddress().toString().substring(1);

                Log.d("Home4U-UDP", "Response " + addr + "/" + message + "/");
                mListener.OnGetMessage(addr, message);

            } catch (Throwable e)
            {
                e.printStackTrace();
            }

            if (socket != null)
            {
                socket.close();
            }
        }

    }

    public void kill()
    {
        bKeepRunning = false;
    }

    public interface OnResponseListener
    {
        void OnGetMessage(String... responses);
    }
}
