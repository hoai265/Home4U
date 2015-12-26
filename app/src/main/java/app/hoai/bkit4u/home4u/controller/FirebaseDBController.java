package app.hoai.bkit4u.home4u.controller;

import android.content.Context;

import com.firebase.client.Firebase;

/**
 * Created by hoaipc on 11/5/15.
 */
public class FirebaseDBController
{
    private static FirebaseDBController mInstance;
    Firebase rootRef;
    Firebase smartplugRef;
    Firebase sensorRef;
    Firebase irbuttonRef;
    Firebase actionRef;

    private FirebaseDBController()
    {

    }

    public static FirebaseDBController getInstance()
    {
        if(mInstance == null)
        {
            mInstance = new FirebaseDBController();
        }

        return mInstance;
    }


}
