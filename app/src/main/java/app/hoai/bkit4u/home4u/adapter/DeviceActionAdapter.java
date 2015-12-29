package app.hoai.bkit4u.home4u.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.hoai.bkit4u.home4u.R;
import app.hoai.bkit4u.home4u.controller.NetworkController;
import app.hoai.bkit4u.home4u.listener.SendActionListener;
import app.hoai.bkit4u.home4u.model.DeviceActionModel;

/**
 * Created by hoaipc on 12/1/15.
 */
public class DeviceActionAdapter extends BaseAdapter
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
        final DeviceActionModel model = getItem(position);
        LayoutInflater inflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.device_action_item_layout,null);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(model.getName());
        return convertView;
    }

    public void addListItem(ArrayList<DeviceActionModel> list)
    {
        this.mModels = list;
        notifyDataSetChanged();
    }

    public void addItem(DeviceActionModel model)
    {
        this.mModels.add(model);
        notifyDataSetChanged();
    }
}
