package app.hoai.bkit4u.home4u.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import app.hoai.bkit4u.home4u.R;

/**
 * Created by hoaipc on 10/6/15.
 */
public class FragmentController
{
    private static FragmentManager mFragmentManager;
    private static int mFragmentcontainerID;

    public static void init(FragmentManager fm, int containerId)
    {
        mFragmentManager = fm;
        mFragmentcontainerID = containerId;
    }

    public static void addFragment(Fragment fragment)
    {
        mFragmentManager.beginTransaction().add(mFragmentcontainerID, fragment).commit();
    }

    public static void changeFragment(Fragment fragment,String fragmentSign)
    {
        mFragmentManager.beginTransaction().replace(mFragmentcontainerID, fragment).addToBackStack(fragmentSign).commit();
    }
}
