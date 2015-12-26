package app.hoai.bkit4u.home4u.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.hoai.bkit4u.home4u.listener.OnFragmentChangeListener;

/**
 * Created by hoaipc on 11/5/15.
 */
public abstract class BaseFragment extends Fragment
{
    OnFragmentChangeListener mOnFragmentChangeListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return getLayout(inflater);
    }

    public abstract View getLayout(LayoutInflater inflater);

    public void setOnFragmentChangeListener(OnFragmentChangeListener listener)
    {
        this.mOnFragmentChangeListener = listener;
    }
}
