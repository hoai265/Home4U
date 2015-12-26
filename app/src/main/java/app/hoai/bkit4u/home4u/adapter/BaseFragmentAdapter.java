package app.hoai.bkit4u.home4u.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by hoaipc on 10/6/15.
 */
public class BaseFragmentAdapter extends FragmentPagerAdapter
{
    Fragment[] mFragments;
    public BaseFragmentAdapter(FragmentManager fm, Fragment[] fragments)
    {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position)
    {
        return mFragments[position];
    }

    @Override
    public int getCount()
    {
        return mFragments.length;
    }
}
