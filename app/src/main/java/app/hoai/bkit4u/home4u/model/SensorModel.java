package app.hoai.bkit4u.home4u.model;

import app.hoai.bkit4u.home4u.model.type.DeviceType;

/**
 * Created by hoaipc on 10/20/15.
 */
public class SensorModel extends BaseDeviceModel
{
    String status;
    int value;

    public String getStatus()
    {
        return status;
    }

    public int getValue()
    {
        return value;
    }

    public SensorModel()
    {
        this.type = DeviceType.SENSOR;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public void setValue(int value)
    {
        this.value = value;
    }
}
