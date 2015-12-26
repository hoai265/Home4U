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

/**
 * Created by hoaipc on 11/20/15.
 */
public class EventPagerFragment extends HomePagerFragment
{

    @Override
    public void onClick(View v)
    {
        NewEventDialogFragment dialogFragment = NewEventDialogFragment.getInstance();
        dialogFragment.setOnCreateEventSuccessCallback(new ICallBack()
        {
            @Override
            public void onCallBack()
            {
                Snackbar.make(getView(), "Add event successfully!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                refresh();
            }
        });
        dialogFragment.show(getFragmentManager(),"create_event");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        HomeItemModel model = mAdapter.getItem(position);
        ActionCollectionFragment fragment = new ActionCollectionFragment();

        if(mOnFragmentChangeListener!=null)
            fragment.setOnFragmentChangeListener(mOnFragmentChangeListener);

        Bundle bundle = new Bundle();
        bundle.putString("title",model.getTypeName());
        bundle.putString("event_id",model.getId());
        fragment.setArguments(bundle);
        FragmentController.changeFragment(fragment, "event item");
    }

    void refresh()
    {
        onFetchData();
    }

    public void onFetchData()
    {
        NetworkController.getInstance().getEventCollection(responseListener);
    }
}
