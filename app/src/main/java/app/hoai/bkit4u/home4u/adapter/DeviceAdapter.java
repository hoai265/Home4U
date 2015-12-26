package app.hoai.bkit4u.home4u.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import app.hoai.bkit4u.home4u.listener.OnFragmentChangeListener;
import app.hoai.bkit4u.home4u.model.BaseDeviceModel;
import app.hoai.bkit4u.home4u.model.type.DeviceType;
import app.hoai.bkit4u.home4u.model.viewholder.IRViewHolder;
import app.hoai.bkit4u.home4u.model.viewholder.SensorViewHolder;
import app.hoai.bkit4u.home4u.model.viewholder.SmartplugViewHolder;

/**
 * Created by hoaipc on 10/6/15.
 */
public class DeviceAdapter extends BaseAdapter
{
    ArrayList<BaseDeviceModel> listItem = new ArrayList<>();

    final int MAX_TYPE = 3;

    OnFragmentChangeListener mListener;

    @Override
    public int getCount()
    {
        return listItem.size();
    }

    @Override
    public BaseDeviceModel getItem(int position)
    {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        SmartplugViewHolder smartplugViewHolder = null;
        SensorViewHolder sensorViewHolder = null;
        IRViewHolder irButtonViewHolder = null;
        BaseDeviceModel model = getItem(position);
        DeviceType type = model.getType();

        switch (type)
        {
            case SMARTPLUG:
                if (convertView == null)
                {
                    smartplugViewHolder = new SmartplugViewHolder();
                    convertView = SmartplugViewHolder.getRootView(parent);
                    smartplugViewHolder.init(convertView);
                    convertView.setTag(smartplugViewHolder);
                }
                else
                {
                    smartplugViewHolder = (SmartplugViewHolder) convertView.getTag();
                }

                smartplugViewHolder.updateUI(model);
                break;
            case SENSOR:
                if (convertView == null)
                {
                    sensorViewHolder = new SensorViewHolder();
                    convertView = SensorViewHolder.getRootView(parent);
                    sensorViewHolder.init(convertView);
                    convertView.setTag(sensorViewHolder);
                }
                else
                {
                    sensorViewHolder = (SensorViewHolder) convertView.getTag();
                }

                sensorViewHolder.updateUI(model);
                break;
            case IR:
                if (convertView == null)
                {
                    irButtonViewHolder = new IRViewHolder();
                    convertView = IRViewHolder.getRootView(parent);
                    irButtonViewHolder.init(convertView);
                    convertView.setTag(irButtonViewHolder);
                }
                else
                {
                    irButtonViewHolder = (IRViewHolder) convertView.getTag();
                }

                irButtonViewHolder.setListener(mListener);
                irButtonViewHolder.updateUI(model);
                break;
        }

        return convertView;
    }

    @Override
    public int getViewTypeCount()
    {
        return MAX_TYPE;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position > getCount() || position < 0)
            return -1;

        if (null == getItem(position))
            return -1;

        Log.d("Position", position + "");
        return getItem(position).getType().getValue();

    }

    public void addItem(BaseDeviceModel model)
    {
        listItem.add(model);
        notifyDataSetChanged();
    }

    public void updateItem(BaseDeviceModel model)
    {
        for (int i = 0; i < listItem.size(); i++)
        {
            if (listItem.get(i).getId().equals(model.getId()))
            {
                listItem.set(i, model);
                break;
            }
        }

        notifyDataSetChanged();
    }

    public void setOnFragmentChangeListener(OnFragmentChangeListener listener)
    {
        this.mListener = listener;
    }
}
