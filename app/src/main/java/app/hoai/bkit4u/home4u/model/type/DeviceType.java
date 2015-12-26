package app.hoai.bkit4u.home4u.model.type;

/**
 * Created by hoaipc on 11/10/15.
 */
public enum DeviceType
{
    SMARTPLUG(0),SENSOR(1), IR(2);

    private int value;

    DeviceType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
