package app.hoai.bkit4u.home4u.controller;

import app.hoai.bkit4u.home4u.model.type.DeviceType;

/**
 * Created by hoaipc on 10/6/15.
 */
public class AppUtils
{
    public static DeviceType getDeviceTypeFromString(String type)
    {
        DeviceType retType = null;
        switch (type)
        {
            case "smartplug":
                retType = DeviceType.SMARTPLUG;
                break;
            case "sensor":
                retType = DeviceType.SENSOR;
                break;
            case "ir":
                retType = DeviceType.IR;
                break;
            default:
                break;
        }

        return retType;
    }
}
