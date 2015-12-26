package app.hoai.bkit4u.home4u.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.hoai.bkit4u.home4u.model.type.HomeType;

/**
 * Created by hoaipc on 11/19/15.
 */
public class HomeItemCollection
{
    ArrayList<HomeItemModel> mModels;

    public HomeItemCollection()
    {
        mModels = new ArrayList<>();
    }

    public static HomeItemCollection makeFromJsonArray(JSONArray json, HomeType type)
    {
        final ArrayList<HomeItemModel> retModels = new ArrayList<>();

        for (int i = 0; i < json.length(); i++)
        {
            try
            {
                JSONObject jsonObject = json.getJSONObject(i);
                HomeItemModel model = HomeItemModel.createModel(jsonObject.getString("name"), type, jsonObject.getString("id"));
                retModels.add(model);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return new HomeItemCollection()
        {
            {
                this.mModels = retModels;
            }
        };
    }

    public ArrayList<HomeItemModel> getModels()
    {
        return mModels;
    }

    public void addItem(HomeItemModel model)
    {
        mModels.add(model);
    }

    public void addListItem(ArrayList<HomeItemModel> list)
    {
        mModels.addAll(list);
    }
}
