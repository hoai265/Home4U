package app.hoai.bkit4u.home4u.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.hoai.bkit4u.home4u.R;
import app.hoai.bkit4u.home4u.model.BaseDeviceModel;
import app.hoai.bkit4u.home4u.model.DeviceActionModel;

/**
 * Created by hoaipc on 12/8/15.
 */
public class ActionAdapter extends BaseAdapter
{
    ArrayList<DeviceActionModel> mModels = new ArrayList<>();

    @Override
    public int getCount()
    {
        return mModels.size();
    }

    @Override
    public DeviceActionModel getItem(int position)
    {
        return mModels.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        DeviceActionModel model = getItem(position);
        BaseDeviceModel device = model.getDevice();

        LayoutInflater inflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.action_item_layout, null);
        TextView name = (TextView) convertView.findViewById(R.id.action_name);
        TextView deviceName = (TextView) convertView.findViewById(R.id.device_name);
        TextView deviceType = (TextView) convertView.findViewById(R.id.device_type);
        View deleteActionView = convertView.findViewById(R.id.ic_delete_action);
        deleteActionView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        deviceName.setText(device.getName());
        deviceType.setText(device.getType().toString());
        name.setText(model.getName());

        return convertView;
    }

    public void addListItem(ArrayList<DeviceActionModel> list)
    {
        this.mModels = list;
        notifyDataSetChanged();
    }

    public void addItem(DeviceActionModel action)
    {
        this.mModels.add(action);
        notifyDataSetChanged();
    }
}
