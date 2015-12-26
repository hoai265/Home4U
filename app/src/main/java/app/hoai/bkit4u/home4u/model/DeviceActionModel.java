package app.hoai.bkit4u.home4u.model;

/**
 * Created by hoaipc on 10/22/15.
 */
public class DeviceActionModel
{
    String id;
    String device_id;
    BaseDeviceModel device;
    String name;

    public String getId()
    {
        return id;
    }

    public String getDeviceId()
    {
        return device_id;
    }

    public String getName()
    {
        return name;
    }

    public BaseDeviceModel getDevice()
    {
        return device;
    }

    public static DeviceActionModel createAction(final String action_id, final String deviceId, final String actionName, final BaseDeviceModel deviceModel)
    {
        return new DeviceActionModel()
        {
            {
                this.id = action_id;
                this.device_id = deviceId;
                this.name = actionName;
                this.device = deviceModel;
            }
        };
    }
}
