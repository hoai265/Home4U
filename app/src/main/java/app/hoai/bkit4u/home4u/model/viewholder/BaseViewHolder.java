package app.hoai.bkit4u.home4u.model.viewholder;

import android.view.View;
import android.view.ViewGroup;

import app.hoai.bkit4u.home4u.model.BaseDeviceModel;

/**
 * Created by hoaipc on 11/10/15.
 */
public abstract class BaseViewHolder
{
    public abstract void init(View rootView);

    public abstract void updateUI(BaseDeviceModel model);
}
