package app.hoai.bkit4u.home4u.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

import app.hoai.bkit4u.home4u.constant.AppConstant;
import app.hoai.bkit4u.home4u.controller.FragmentController;
import app.hoai.bkit4u.home4u.listener.ICallBack;
import app.hoai.bkit4u.home4u.model.HomeItemModel;
import app.hoai.bkit4u.home4u.model.type.HomeType;

/**
 * Created by hoaipc on 11/20/15.
 */
public class TypePagerFragment extends HomePagerFragment
{
    @Override
    public void onClick(View v)
    {
        super.onClick(v);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

        HomeItemModel model = mAdapter.getItem(position);
        Log.d("Home4U-Type click", model.getTypeName() + "/" + model.getId());

        String firebaseRoot = AppConstant.DEVICES_REF + model.getId();


        BaseDeviceCollectionFragment fragment = new BaseDeviceCollectionFragment();
        if(mOnFragmentChangeListener!=null)
            fragment.setOnFragmentChangeListener(mOnFragmentChangeListener);

        Bundle bundle = new Bundle();
        bundle.putString("firebaseRoot", firebaseRoot);
        fragment.setArguments(bundle);
        FragmentController.changeFragment(fragment,"type item");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        fab.setVisibility(View.GONE);
    }

    public void onFetchData()
    {
        ArrayList<HomeItemModel> list = new ArrayList<>();

        HomeItemModel model1 = HomeItemModel.createModel("Smartplug",HomeType.TYPE,"smartplug");
        HomeItemModel model2 = HomeItemModel.createModel("Sensor",HomeType.TYPE,"sensor");
        HomeItemModel model3 = HomeItemModel.createModel("IR",HomeType.TYPE,"ir");

        list.add(model1);
        list.add(model2);
        list.add(model3);

        mAdapter.addListItem(list);

        onFetchCompleted();
    }
}
