package app.hoai.bkit4u.home4u.model;

import app.hoai.bkit4u.home4u.model.type.HomeType;

/**
 * Created by hoaipc on 11/14/15.
 */
public class HomeItemModel
{
    String typeName;
    HomeType type;
    String id;

    public static HomeItemModel createModel(final String typeString, final HomeType homeType, final String itemId)
    {
        return new HomeItemModel()
        {
            {
                this.typeName = typeString;
                this.type = homeType;
                this.id = itemId;
            }
        };
    }

    public String getTypeName()
    {
        return typeName;
    }

    public HomeType getType()
    {
        return type;
    }

    public String getId()
    {
        return id;
    }
}
