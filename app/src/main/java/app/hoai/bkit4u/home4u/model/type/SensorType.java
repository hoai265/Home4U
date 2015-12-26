package app.hoai.bkit4u.home4u.model.type;

/**
 * Created by hoaipc on 11/10/15.
 */
public enum SensorType
{
    TEMPERATOR(0),HUMIDITY(1),LIGHT(2);

    private int value;

    private SensorType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
