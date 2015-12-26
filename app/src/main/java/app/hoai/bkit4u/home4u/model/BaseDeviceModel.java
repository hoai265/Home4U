package app.hoai.bkit4u.home4u.model;

import app.hoai.bkit4u.home4u.controller.AppUtils;
import app.hoai.bkit4u.home4u.model.type.DeviceType;

/**
 * Created by hoaipc on 10/6/15.
 */
public class BaseDeviceModel
{
    String id;
    String name;
    DeviceType type;
    String room_id;

    public String getName()
    {
        return name;
    }

    public String getId()
    {
        return id;
    }

    public String getRoom_id()
    {
        return room_id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public DeviceType getType()
    {
        return type;
    }

    public BaseDeviceModel(String deviceId, String deviceName, String type, String roomId)
    {
        this.id = deviceId;
        this.name = deviceName;
        this.type = AppUtils.getDeviceTypeFromString(type);
        this.room_id = roomId;
    }

    public void setType(DeviceType type)
    {
        this.type = type;
    }

    public BaseDeviceModel()
    {

    }
}
