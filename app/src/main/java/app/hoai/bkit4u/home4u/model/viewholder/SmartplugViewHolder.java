package app.hoai.bkit4u.home4u.model.viewholder;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import app.hoai.bkit4u.home4u.R;
import app.hoai.bkit4u.home4u.adapter.DeviceActionAdapter;
import app.hoai.bkit4u.home4u.controller.NetworkController;
import app.hoai.bkit4u.home4u.listener.ResponseListener;
import app.hoai.bkit4u.home4u.listener.SendActionListener;
import app.hoai.bkit4u.home4u.model.BaseDeviceModel;
import app.hoai.bkit4u.home4u.model.DeviceActionCollection;
import app.hoai.bkit4u.home4u.model.SmartPlugModel;

/**
 * Created by hoaipc on 11/10/15.
 */
public class SmartplugViewHolder extends BaseViewHolder
{
    TextView deviceName;
    View showActionView;
    TextView deviceValue;
    TextView deviceStatus;
    static Context context;

    @Override
    public void init(View rootView)
    {
        deviceName = (TextView) rootView.findViewById(R.id.device_name);
        deviceValue = (TextView) rootView.findViewById(R.id.value);
        deviceStatus = (TextView) rootView.findViewById(R.id.status);
        showActionView = rootView.findViewById(R.id.ic_show_action);
    }

    @Override
    public void updateUI(final BaseDeviceModel model)
    {
        final SmartPlugModel modelData = (SmartPlugModel) model;

        deviceName.setText(modelData.getName());
        if (modelData.getValue() > 0)
            deviceValue.setText(modelData.getValue() + "");
        else
            deviceValue.setText("");
        deviceStatus.setText(modelData.getStatus().toUpperCase());
        showActionView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loadDeviceAction(modelData);
            }
        });

    }

    void loadDeviceAction(SmartPlugModel model)
    {
        final DeviceActionAdapter actionAdapter = new DeviceActionAdapter();

        NetworkController.getInstance().getDeviceActions(new ResponseListener<DeviceActionCollection>()
        {
            @Override
            public void onResponse(DeviceActionCollection response)
            {
                actionAdapter.addListItem(response.getItems());
                float scale = context.getResources().getDisplayMetrics().density;
                final ListPopupWindow listPopupWindow = new ListPopupWindow(context);
                listPopupWindow.setAdapter(actionAdapter);
                listPopupWindow.setAnchorView(showActionView);
                listPopupWindow.setWidth((int) (240 * scale + 0.5f));
                listPopupWindow.setModal(true);
                listPopupWindow.getBackground().setAlpha(0);
                listPopupWindow.setAnimationStyle(R.style.anim_option_menu);
                listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(final AdapterView<?> parent, View view, int position, long id)
                    {
                        NetworkController.getInstance().addCommand(new SendActionListener()
                        {
                            @Override
                            public void onSuccess()
                            {
                                Snackbar.make(showActionView, "Send action successfully!", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                            @Override
                            public void onError()
                            {

                            }
                        }, actionAdapter.getItem(position));
                    }
                });
                listPopupWindow.show();
            }

            @Override
            public void onError()
            {

            }
        }, model.getId());
    }

    public static View getRootView(ViewGroup parentView)
    {
        context = parentView.getContext();
        LayoutInflater inflater = (LayoutInflater) parentView.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return inflater.inflate(R.layout.smartplug_item_layout, null);
    }
}
