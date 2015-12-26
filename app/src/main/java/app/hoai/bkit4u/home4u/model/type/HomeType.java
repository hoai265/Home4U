package app.hoai.bkit4u.home4u.model.type;

/**
 * Created by hoaipc on 11/15/15.
 */
public enum HomeType
{
    TITLE(0),TYPE(1),EVENT(2),ROOM(3);

    private int value;

    private HomeType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
