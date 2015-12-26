package app.hoai.bkit4u.home4u.fragment.dialog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import app.hoai.bkit4u.home4u.R;
import app.hoai.bkit4u.home4u.adapter.BaseDeviceAdapter;
import app.hoai.bkit4u.home4u.controller.NetworkController;
import app.hoai.bkit4u.home4u.listener.ResponseListener;
import app.hoai.bkit4u.home4u.model.BaseDeviceCollection;
import app.hoai.bkit4u.home4u.model.BaseDeviceModel;

/**
 * Created by hoaipc on 9/21/15.
 */
public class AddDeviceDialogFragment extends android.support.v4.app.DialogFragment implements AdapterView.OnItemClickListener
{
    public static AddDeviceDialogFragment mInstance = null;
    boolean mIsShown = false;
    private ListView mListView;
    private BaseDeviceAdapter mAdapter;
    private OnChooseDeviceCompletedListener mListener;
    ProgressBar mProgress;
    View mProgressContainer;

    @SuppressLint ("ValidFragment")
    private AddDeviceDialogFragment()
    {

    }

    public static AddDeviceDialogFragment getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new AddDeviceDialogFragment();
        }

        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.choose_device_dialog_layout, container,
                false);
        mListView = (ListView)rootView.findViewById(R.id.listView);
        mListView.setVisibility(View.GONE);

        mProgress = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mProgressContainer = rootView.findViewById(R.id.progress_container);

        final View emptyView = rootView.findViewById(R.id.empty_view);
        final TextView emptyText = (TextView) rootView.findViewById(R.id.empty_text);
        mAdapter = new BaseDeviceAdapter();
        mListView.setAdapter(mAdapter);

        NetworkController.getInstance().getDeviceRoomless(new ResponseListener<BaseDeviceCollection>()
        {
            @Override
            public void onResponse(BaseDeviceCollection response)
            {
                if(response.getModels().size() >0)
                    mAdapter.refreshItems(response.getModels());
                else
                {
                    emptyView.setVisibility(View.VISIBLE);
                    emptyText.setText("No Device!");
                }

                onFetchCompleted();
            }

            @Override
            public void onError()
            {
            }
        });

        mListView.setOnItemClickListener(this);

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        BaseDeviceModel model = mAdapter.getItem(position);
        mListener.onCompleteted(model);
        dismiss();
    }

    public void setOnChooseDeviceCompleted(OnChooseDeviceCompletedListener listener)
    {
        this.mListener = listener;
    }

    public interface OnChooseDeviceCompletedListener
    {
        void onCompleteted(BaseDeviceModel model);
        void onError();
    }

    protected void onFetchCompleted()
    {
        mProgressContainer.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
    }
}
