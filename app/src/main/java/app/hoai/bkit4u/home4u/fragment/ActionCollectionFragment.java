package app.hoai.bkit4u.home4u.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Network;

import java.util.ArrayList;

import app.hoai.bkit4u.home4u.R;
import app.hoai.bkit4u.home4u.adapter.ActionAdapter;
import app.hoai.bkit4u.home4u.controller.NetworkController;
import app.hoai.bkit4u.home4u.listener.ResponseListener;
import app.hoai.bkit4u.home4u.listener.SendActionListener;
import app.hoai.bkit4u.home4u.model.DeviceActionCollection;
import app.hoai.bkit4u.home4u.model.DeviceActionModel;

/**
 * Created by hoaipc on 12/8/15.
 */
public class ActionCollectionFragment extends BaseFragment implements View.OnClickListener
{
    ListView mListView;
    ActionAdapter mAdapter;
    FloatingActionButton fab;
    FloatingActionButton fabExcute;
    String mEventId;
    ProgressBar mProgress;
    View mProgressContainer;
    DeviceActionCollection mActionCollection;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        String title = bundle.getString("title");
        mEventId = bundle.getString("event_id");

        if (mOnFragmentChangeListener != null)
        {
            mOnFragmentChangeListener.onChangeToolbarTitle(title);
        }
    }

    @Override
    public View getLayout(LayoutInflater inflater)
    {
        View rootView = inflater.inflate(R.layout.action_collection_layout, null);
        mListView = (ListView) rootView.findViewById(R.id.baseListView);

        mProgress = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mProgressContainer = rootView.findViewById(R.id.progress_container);

        mAdapter = new ActionAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setDividerHeight(0);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        fabExcute = (FloatingActionButton) rootView.findViewById(R.id.fab_excute);
        fabExcute.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        onFetchData();
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.fab:
                mOnFragmentChangeListener.onAddActionRequest(getView(),mEventId);
                break;
            case R.id.fab_excute:
                if(mActionCollection.getItems().size() > 0)
                    NetworkController.getInstance().excuteEvent(new SendActionListener()
                    {
                        @Override
                        public void onSuccess()
                        {
                            Snackbar.make(getView(), "Send event successfully!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                        @Override
                        public void onError()
                        {
                            Snackbar.make(getView(), "Send event error!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }, mEventId);
                break;
            default:
                break;
        }

    }

    protected void onFetchCompleted()
    {
        mProgressContainer.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
    }

    protected void onFetchingData()
    {
        mProgressContainer.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.VISIBLE);
    }

    void onFetchData()
    {
        NetworkController.getInstance().getEventAction(new ResponseListener<DeviceActionCollection>()
        {
            @Override
            public void onResponse(DeviceActionCollection response)
            {
                mActionCollection = response;
                mAdapter.addListItem(response.getItems());
                onFetchCompleted();
            }

            @Override
            public void onError()
            {
                onFetchCompleted();
            }
        }, mEventId);
    }
}
