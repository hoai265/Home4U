package app.hoai.bkit4u.home4u.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import app.hoai.bkit4u.home4u.constant.AppConstant;
import app.hoai.bkit4u.home4u.controller.FragmentController;
import app.hoai.bkit4u.home4u.controller.NetworkController;
import app.hoai.bkit4u.home4u.fragment.dialog.NewEventDialogFragment;
import app.hoai.bkit4u.home4u.fragment.dialog.NewRoomDialogFragment;
import app.hoai.bkit4u.home4u.listener.ICallBack;
import app.hoai.bkit4u.home4u.model.HomeItemModel;
import app.hoai.bkit4u.home4u.model.type.HomeType;

/**
 * Created by hoaipc on 11/20/15.
 */
public class RoomPagerFragment extends HomePagerFragment
{
    @Override
    public void onClick(View v)
    {
        NewRoomDialogFragment dialogFragment = NewRoomDialogFragment.getInstance();
        dialogFragment.setOnCreateRoomSuccessCallback(new ICallBack()
        {
            @Override
            public void onCallBack()
            {
                Snackbar.make(getView(), "Add room successfully!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                refresh();
            }
        });
        dialogFragment.show(getFragmentManager(),"create_room");
    }

    @Override
    public void onFetchData()
    {
        NetworkController.getInstance().getRoomCollection(responseListener);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        final HomeItemModel model = mAdapter.getItem(position);
        Log.d("App-Room click", model.getTypeName() + "/" + model.getId());

        String firebaseRoot = AppConstant.ROOMS_REF + model.getId();

        BaseDeviceCollectionFragment fragment = new BaseDeviceCollectionFragment();

        if(mOnFragmentChangeListener!=null)
        fragment.setOnFragmentChangeListener(mOnFragmentChangeListener);

        Bundle bundle = new Bundle();
        bundle.putString("firebaseRoot", firebaseRoot);
        bundle.putString("title",model.getTypeName());
        fragment.setArguments(bundle);

        fragment.setAddDeviceBuider(new BaseDeviceCollectionFragment.OnAddDeviceBuider()
        {
            @Override
            public String ObjectId()
            {
                return model.getId();
            }
        });

        FragmentController.changeFragment(fragment, "room item");
    }

    void refresh()
    {
        onFetchData();
    }
}
