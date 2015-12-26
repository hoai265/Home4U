package app.hoai.bkit4u.home4u.model;

import java.util.ArrayList;

/**
 * Created by hoaipc on 12/12/15.
 */
public class SpeechModelCollection
{
    ArrayList<SpeechControlModel> mModels = new ArrayList<>();

    public ArrayList<SpeechControlModel> getModels()
    {
        return mModels;
    }

    public void addModel(SpeechControlModel model)
    {
        mModels.add(model);
    }

    public SpeechControlModel findModelByDescribe(String describe)
    {
        SpeechControlModel retModel = new SpeechControlModel();

        for(SpeechControlModel model:mModels)
        {
            if(model.describe.equals(describe))
                return model;
        }

        return null;
    }
}
