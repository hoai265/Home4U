package app.hoai.bkit4u.home4u.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import app.hoai.bkit4u.home4u.R;
import app.hoai.bkit4u.home4u.constant.AppConstant;
import app.hoai.bkit4u.home4u.listener.OnGetNotifications;

/**
 * Created by hoaipc on 11/15/15.
 */
public class NotificationFragment extends Fragment
{
    OnGetNotifications mListener;
    TextView mNotificationText;
    boolean isFirstTime = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.notification_layout, null);

        Button btnExit = (Button) rootView.findViewById(R.id.btn_exit);
        mNotificationText = (TextView) rootView.findViewById(R.id.title);
        btnExit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mListener != null)
                {
                    mListener.onHideNotification();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        Firebase rootFirebase = new Firebase(AppConstant.NOTIFICATIONS_REF);

        rootFirebase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(isFirstTime)
                {
                    isFirstTime = false;
                } else
                {
                    String deviceName = (String) dataSnapshot.child("name").getValue();
                    String status = (String) dataSnapshot.child("status").getValue();
                    String room = (String) dataSnapshot.child("roomId").getValue();
                    String noticationContent = deviceName + " in " + room + " is " + status;

                    mNotificationText.setText(noticationContent);

                    if (mListener != null)
                    {
                        mListener.onShowNotification();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError)
            {

            }
        });
    }

    public void setOnGetNotification(OnGetNotifications listener)
    {
        this.mListener = listener;
    }
}
