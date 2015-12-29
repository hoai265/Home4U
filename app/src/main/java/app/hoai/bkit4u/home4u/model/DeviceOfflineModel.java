package app.hoai.bkit4u.home4u.model;

import java.util.ArrayList;

import app.hoai.bkit4u.home4u.adapter.ActionAdapter;
import app.hoai.bkit4u.home4u.adapter.DeviceActionAdapter;
import app.hoai.bkit4u.home4u.controller.AppUtils;
import app.hoai.bkit4u.home4u.model.type.DeviceType;

/**
 * Created by hoaipc on 12/28/15.
 */
public class DeviceOfflineModel
{
    String id;
    String name;
    DeviceType type;
    DeviceActionAdapter actionAdapter;

    public String getName()
    {
        return name;
    }

    public String getId()
    {
        return id;
    }

    public DeviceType getType()
    {
        return type;
    }

    public DeviceActionAdapter getActionAdapter()
    {
        return actionAdapter;
    }

    public DeviceOfflineModel(String id, String name, String type)
    {
        this.id = id;
        this.name = name;
        this.type = AppUtils.getDeviceTypeFromString(type);
        actionAdapter = new DeviceActionAdapter();
    }

    public void addAction(DeviceActionModel action)
    {
        this.actionAdapter.addItem(action);
    }
}
