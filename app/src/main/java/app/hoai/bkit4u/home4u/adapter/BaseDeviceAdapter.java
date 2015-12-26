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

/**
 * Created by hoaipc on 12/8/15.
 */
public class BaseDeviceAdapter extends BaseAdapter
{
    ArrayList<BaseDeviceModel> mModels = new ArrayList<>();

    @Override
    public int getCount()
    {
        return mModels.size();
    }

    @Override
    public BaseDeviceModel getItem(int position)
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
        BaseDeviceModel model = getItem(position);
        LayoutInflater inflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.base_device_item_layout, null);
        TextView name = (TextView) convertView.findViewById(R.id.device_name);
        TextView type = (TextView) convertView.findViewById(R.id.device_type);
        name.setText(model.getName());
        type.setText(model.getType().toString());
        return convertView;
    }

    public void refreshItems(ArrayList<BaseDeviceModel> list)
    {
        this.mModels = list;
        notifyDataSetChanged();
    }
}
