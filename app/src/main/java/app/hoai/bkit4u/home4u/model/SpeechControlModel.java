package app.hoai.bkit4u.home4u.model;

import app.hoai.bkit4u.home4u.model.type.SpeechType;

/**
 * Created by hoaipc on 12/12/15.
 */
public class SpeechControlModel
{
    String describe;
    String id;
    SpeechType type;
    DeviceActionModel actionModel;

    public SpeechControlModel(String decribeString, String idString, SpeechType speechType)
    {
        this.describe = decribeString;
        this.id = idString;
        this.type = speechType;
    }

    public SpeechControlModel()
    {

    }

    public DeviceActionModel getActionModel()
    {
        return actionModel;
    }

    public void setActionModel(DeviceActionModel actionModel)
    {
        this.actionModel = actionModel;
    }

    public SpeechType getType()
    {
        return type;
    }

    public String getDescribe()
    {
        return describe;
    }

    public String getId()
    {
        return id;
    }
}
