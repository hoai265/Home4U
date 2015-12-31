package app.hoai.bkit4u.home4u.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import app.hoai.bkit4u.home4u.R;
import app.hoai.bkit4u.home4u.adapter.DeviceAdapter;
import app.hoai.bkit4u.home4u.controller.NetworkController;
import app.hoai.bkit4u.home4u.listener.ResponseListener;
import app.hoai.bkit4u.home4u.model.BaseDeviceModel;
import app.hoai.bkit4u.home4u.model.IRModel;
import app.hoai.bkit4u.home4u.model.SensorModel;
import app.hoai.bkit4u.home4u.model.SmartPlugModel;
import app.hoai.bkit4u.home4u.model.type.DeviceType;

/**
 * Created by hoaipc on 11/26/15.
 */
public class BaseDeviceCollectionFragment extends BaseFragment implements com.firebase.client.ChildEventListener
{
    ListView mListView;
    DeviceAdapter mAdapter;
    Firebase rootFireBase;
    FloatingActionButton fab;
    OnAddDeviceBuider mBuider;
    String firebaseString;
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

        mAdapter = new DeviceAdapter();
        mAdapter.setOnFragmentChangeListener(mOnFragmentChangeListener);
        mListView.setAdapter(mAdapter);
        mListView.setDividerHeight(0);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        if (mBuider == null) fab.setVisibility(View.GONE);
        else
        {
            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnFragmentChangeListener.onAddDeviceRequest(getView(), mBuider.ObjectId());
                }
            });
        }

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        firebaseString = bundle.getString("firebaseRoot");

        String title = bundle.getString("title");
        if (mOnFragmentChangeListener != null)
        {
            mOnFragmentChangeListener.onChangeToolbarTitle(title);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Log.d("Home4U","onActivityCreated");
        rootFireBase = new Firebase(firebaseString);
        rootFireBase.addChildEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s)
    {
        final DataSnapshot data = dataSnapshot;
        NetworkController.getInstance().getDeviceDetail(new ResponseListener<BaseDeviceModel>()
        {
            @Override
            public void onResponse(BaseDeviceModel response)
            {
                Log.d("Home4U-Child add", response.getId() + "/" + response.getName() + "/" + response.getType());

                switch (response.getType())
                {
                    case SMARTPLUG:
                        SmartPlugModel smartplugModel = data.getValue(SmartPlugModel.class);
                        smartplugModel.setId(data.getKey());
                        smartplugModel.setType(DeviceType.SMARTPLUG);
                        mAdapter.addItem(smartplugModel);
                        break;
                    case SENSOR:
                        SensorModel sensorModel = data.getValue(SensorModel.class);
                        sensorModel.setId(data.getKey());
                        sensorModel.setType(DeviceType.SENSOR);
                        mAdapter.addItem(sensorModel);
                        break;
                    case IR:
                        IRModel model = data.getValue(IRModel.class);
                        model.setId(data.getKey());
                        model.setType(DeviceType.IR);
                        mAdapter.addItem(model);
                        break;
                }

                onFetchCompleted();
            }

            @Override
            public void onError()
            {
                onFetchCompleted();
            }
        }, dataSnapshot.getKey());
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s)
    {
        final DataSnapshot data = dataSnapshot;
        NetworkController.getInstance().getDeviceDetail(new ResponseListener<BaseDeviceModel>()
        {
            @Override
            public void onResponse(BaseDeviceModel response)
            {
                Log.d("Home4U-Child change", response.getId() + "/" + response.getName() + "/" + response.getType());
                switch (response.getType())
                {
                    case SMARTPLUG:
                        SmartPlugModel smartplugModel = data.getValue(SmartPlugModel.class);
                        smartplugModel.setId(data.getKey());
                        mAdapter.updateItem(smartplugModel);
                        break;
                    case SENSOR:
                        SensorModel sensorModel = data.getValue(SensorModel.class);
                        sensorModel.setId(data.getKey());
                        mAdapter.updateItem(sensorModel);
                        break;
                    case IR:
                        IRModel model = data.getValue(IRModel.class);
                        model.setId(data.getKey());
                        mAdapter.updateItem(model);
                        break;
                }
            }

            @Override
            public void onError()
            {

            }
        }, dataSnapshot.getKey());
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot)
    {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s)
    {

    }

    @Override
    public void onCancelled(FirebaseError firebaseError)
    {
        onFetchCompleted();
        Log.d("Home4U - firebase",firebaseError.getMessage());
    }

    public void setAddDeviceBuider(OnAddDeviceBuider buider)
    {
        this.mBuider = buider;
    }

    public interface OnAddDeviceBuider
    {
        String ObjectId();
    }

    protected void onFetchCompleted()
    {
        mProgressContainer.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
    }
}
