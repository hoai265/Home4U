package app.hoai.bkit4u.home4u.model.type;

/**
 * Created by hoaipc on 12/12/15.
 */
public enum SpeechType
{
    ACTION(0),EVENT(1);

    private int value;

    private SpeechType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
