package app.hoai.bkit4u.home4u.adapter;

import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.hoai.bkit4u.home4u.R;
import app.hoai.bkit4u.home4u.controller.NetworkController;
import app.hoai.bkit4u.home4u.model.DeviceActionModel;
import app.hoai.bkit4u.home4u.model.DeviceOfflineModel;

/**
 * Created by hoaipc on 12/28/15.
 */
public class DeviceOfflineAdapter extends BaseAdapter
{
    ArrayList<DeviceOfflineModel> mList = new ArrayList<>();
    OnSendCommandListener mListener;

    @Override
    public int getCount()
    {
        return mList.size();
    }

    @Override
    public DeviceOfflineModel getItem(int position)
    {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent)
    {
        final Context context = parent.getContext();
        final DeviceOfflineModel model = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.device_offline_item_layout, null);
        TextView deviceName = (TextView) convertView.findViewById(R.id.device_name);
        TextView deviceType = (TextView) convertView.findViewById(R.id.device_type);
        final View actionMore = convertView.findViewById(R.id.ic_show_action);

        deviceName.setText(model.getName());
        deviceType.setText(model.getType().toString());
        actionMore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!model.getActionAdapter().isEmpty())
                {
                    float scale = context.getResources().getDisplayMetrics().density;
                    final ListPopupWindow listPopupWindow = new ListPopupWindow(context);
                    listPopupWindow.setAdapter(model.getActionAdapter());
                    listPopupWindow.setAnchorView(actionMore);
                    listPopupWindow.setWidth((int) (240 * scale + 0.5f));
                    listPopupWindow.setModal(true);
                    listPopupWindow.getBackground().setAlpha(0);
                    listPopupWindow.setAnimationStyle(R.style.anim_option_menu);
                    listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(final AdapterView<?> parent, View view, int position, long id)
                        {
                            if (mListener != null)
                                mListener.OnSendCommand(NetworkController.getInstance().getOfflineCommandString(model.getId(), model.getActionAdapter().getItem(position).getId()));
                        }
                    });
                    listPopupWindow.show();
                }
            }
        });

        return convertView;
    }

    public void addAction(DeviceActionModel action)
    {
        int size = mList.size();
        for (int i = 0; i < size; i++)
        {
            DeviceOfflineModel device = mList.get(i);
            if (action.getDeviceId().equals(device.getId()))
            {
                device.getActionAdapter().addItem(action);
                mList.set(i, device);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void addDevice(DeviceOfflineModel device)
    {
        mList.add(device);
        notifyDataSetChanged();
    }

    public interface OnSendCommandListener
    {
        void OnSendCommand(String string);
    }

    public void setListener(OnSendCommandListener mListener)
    {
        this.mListener = mListener;
    }
}
