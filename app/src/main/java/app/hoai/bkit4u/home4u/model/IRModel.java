package app.hoai.bkit4u.home4u.model;

import app.hoai.bkit4u.home4u.model.type.DeviceType;

/**
 * Created by hoaipc on 10/20/15.
 */
public class IRModel extends BaseDeviceModel
{
    String status;

    public IRModel()
    {
        this.type = DeviceType.IR;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
