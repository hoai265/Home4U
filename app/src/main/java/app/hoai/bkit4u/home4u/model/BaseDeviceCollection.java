package app.hoai.bkit4u.home4u.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hoaipc on 10/27/15.
 */
public class BaseDeviceCollection
{
    ArrayList<BaseDeviceModel> mModels;

    public static BaseDeviceCollection makeFromJsonArray(JSONArray json)
    {
        final ArrayList<BaseDeviceModel> list = new ArrayList<>();

        for (int i = 0; i < json.length(); i++)
        {
            try
            {
                JSONObject jsonObject = json.getJSONObject(i);
                BaseDeviceModel model = new BaseDeviceModel(jsonObject.getString("id"), jsonObject.getString("name"), jsonObject.getString("type"),jsonObject.getString("room_id"));
                list.add(model);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return new BaseDeviceCollection()
        {
            {
                this.mModels = list;
            }
        };
    }

    public ArrayList<BaseDeviceModel> getModels()
    {
        return mModels;
    }
}
