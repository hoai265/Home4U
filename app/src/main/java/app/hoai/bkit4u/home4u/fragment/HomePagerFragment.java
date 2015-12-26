package app.hoai.bkit4u.home4u.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import app.hoai.bkit4u.home4u.R;
import app.hoai.bkit4u.home4u.adapter.HomePagerAdapter;
import app.hoai.bkit4u.home4u.listener.ICallBack;
import app.hoai.bkit4u.home4u.listener.ResponseListener;
import app.hoai.bkit4u.home4u.model.HomeItemCollection;

/**
 * Created by hoaipc on 11/20/15.
 */
public abstract class HomePagerFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener
{
    protected HomePagerAdapter mAdapter;
    private ListView mListView;
    private Buider mBuider;
    protected FloatingActionButton fab;
    ProgressBar mProgress;
    View mProgressContainer;

    @Override
    public View getLayout(LayoutInflater inflater)
    {
        View rootView = inflater.inflate(R.layout.base_collection_layout, null);
        mListView = (ListView) rootView.findViewById(R.id.baseListView);
        mListView.setVisibility(View.GONE);

        mProgress = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mProgressContainer = rootView.findViewById(R.id.progress_container);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        mAdapter = new HomePagerAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setDividerHeight(0);
        mListView.setOnItemClickListener(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if (mBuider != null)
        {
            mBuider.getOnReadyCallback().onCallBack();
        }
    }

    public ResponseListener<HomeItemCollection> responseListener = new ResponseListener<HomeItemCollection>()
    {
        @Override
        public void onResponse(HomeItemCollection response)
        {
            mAdapter.addListItem(response.getModels());
            onFetchCompleted();
        }

        @Override
        public void onError()
        {
            onFetchCompleted();
        }
    };

    protected void onFetchCompleted()
    {
        mProgressContainer.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
    }

    public void getBuider(Buider buider)
    {
        this.mBuider = buider;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
    }

    @Override
    public void onClick(View v)
    {

    }

    public interface Buider
    {
        String getRootFirebase();

        ICallBack getOnReadyCallback();
    }

    public abstract void onFetchData();
}
