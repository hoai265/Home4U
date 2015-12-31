package app.hoai.bkit4u.home4u.listener;

import android.view.View;

import app.hoai.bkit4u.home4u.model.type.HomeType;

/**
 * Created by hoaipc on 10/12/15.
 */
public interface OnFragmentChangeListener
{
    void onChangeToolbarTitle(String title);
    void onAddDeviceRequest(View v,String objectId);
    void onAddActionRequest(View v,String objectId, ICallBack OnCallback);
    void onAddIrActionRequest(String id);
    void onHomeRequest();
}
