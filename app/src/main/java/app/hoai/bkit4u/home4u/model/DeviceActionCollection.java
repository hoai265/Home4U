package app.hoai.bkit4u.home4u.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hoaipc on 12/1/15.
 */
public class DeviceActionCollection
{
    ArrayList<DeviceActionModel> mModels;

    public static DeviceActionCollection makeFromJsonArray(JSONArray json)
    {
        final ArrayList<DeviceActionModel> list = new ArrayList<>();

        for (int i = 0; i < json.length(); i++)
        {
            try
            {
                JSONObject jsonObject = json.getJSONObject(i);

                JSONObject deviceObject = jsonObject.getJSONObject("device");
                BaseDeviceModel device = new BaseDeviceModel(deviceObject.getString("id"),deviceObject.getString("name"),
                        deviceObject.getString("type"),deviceObject.getString("room_id"));

                DeviceActionModel model = DeviceActionModel.createAction(jsonObject.getString("id"),jsonObject.getString("device_id"),
                        jsonObject.getString("name"), device);
                list.add(model);

            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return new DeviceActionCollection()
        {
            {
                this.mModels = list;
            }
        };
    }

    public ArrayList<DeviceActionModel> getItems()
    {
        return mModels;
    }
}
