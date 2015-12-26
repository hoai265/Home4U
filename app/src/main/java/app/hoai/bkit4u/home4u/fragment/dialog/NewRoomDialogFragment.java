package app.hoai.bkit4u.home4u.fragment.dialog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import app.hoai.bkit4u.home4u.R;
import app.hoai.bkit4u.home4u.controller.NetworkController;
import app.hoai.bkit4u.home4u.listener.ICallBack;
import app.hoai.bkit4u.home4u.listener.ResponseListener;

/**
 * Created by hoaipc on 11/28/15.
 */
public class NewRoomDialogFragment extends DialogFragment
{
    public static NewRoomDialogFragment mInstance = null;
    boolean mIsShown = false;
    private ICallBack mOnCreateRoomSuccessCallback;

    @SuppressLint ("ValidFragment")
    private NewRoomDialogFragment()
    {

    }

    public static NewRoomDialogFragment getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new NewRoomDialogFragment();
        }

        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.new_room_dialog_layout, container,
                false);

        final EditText editRoomName = (EditText)rootView.findViewById(R.id.edit_name);

        Button btnCreate = (Button) rootView.findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(editRoomName.getText().equals(""))
                {

                } else
                {
                    NetworkController.getInstance().addNewRoom(new ResponseListener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            mOnCreateRoomSuccessCallback.onCallBack();
                            dismiss();
                        }

                        @Override
                        public void onError()
                        {

                        }
                    }, editRoomName.getText().toString());
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    public static void closeDialog()
    {
        if (mInstance != null)
        {
            mInstance.dismissAllowingStateLoss();

            mInstance = null;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog)
    {
        super.onDismiss(dialog);

        mIsShown = false;
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onDestroy()
    {
        mInstance = null;

        super.onDestroy();
    }

    @Override
    public int show(FragmentTransaction transaction, String tag)
    {
        if (mIsShown)
        {
            return -1;
        }
        else
        {
            mIsShown = true;
            return super.show(transaction, tag);
        }
    }

    @Override
    public void show(FragmentManager manager, String tag)
    {
        if (mIsShown)
        {

        }
        else
        {
            mIsShown = true;
            super.show(manager, tag);
        }

    }

    @Override
    public void onStart()
    {
        getDialog().getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        super.onStart();
    }

    public void setOnCreateRoomSuccessCallback(ICallBack callback)
    {
        this.mOnCreateRoomSuccessCallback = callback;
    }
}
