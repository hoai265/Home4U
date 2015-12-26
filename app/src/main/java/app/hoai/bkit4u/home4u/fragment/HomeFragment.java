package app.hoai.bkit4u.home4u.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import app.hoai.bkit4u.home4u.R;
import app.hoai.bkit4u.home4u.adapter.BaseFragmentAdapter;
import app.hoai.bkit4u.home4u.constant.AppConstant;
import app.hoai.bkit4u.home4u.controller.NetworkController;
import app.hoai.bkit4u.home4u.listener.ICallBack;

/**
 * Created by hoaipc on 11/5/15.
 */
public class HomeFragment extends BaseFragment
{
    View tabRoom;
    View tabType;
    View tabEvent;
    ViewPager mPager;
    BaseFragmentAdapter mFragmentAdapter;
    TabMenuController mTabController;
    HomePagerFragment[] listFragment;

    final int DEFAULT_TAB_INDEX = -1;

    @Override
    public View getLayout(LayoutInflater inflater)
    {
        View rootView = inflater.inflate(R.layout.home_fragment_new_layout, null);
        View tabbarLayout = rootView.findViewById(R.id.tabbar);

        tabRoom = tabbarLayout.findViewById(R.id.tab_room);
        tabType = tabbarLayout.findViewById(R.id.tab_type);
        tabEvent = tabbarLayout.findViewById(R.id.tab_event);
        mPager = (ViewPager) rootView.findViewById(R.id.pager);

        View[] menuView = new View[3];
        menuView[0] = tabRoom;
        menuView[1] = tabType;
        menuView[2] = tabEvent;

        mFragmentAdapter = new BaseFragmentAdapter(getChildFragmentManager(), getFragmentss());
        mPager.setAdapter(mFragmentAdapter);

        mTabController = new TabMenuController(menuView, mPager);
        mTabController.switchTab(0);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if(mOnFragmentChangeListener!= null)
        {
            mOnFragmentChangeListener.onChangeToolbarTitle("HOME4U");
        }
    }

    public Fragment[] getFragmentss()
    {
        listFragment = new HomePagerFragment[3];

        final RoomPagerFragment roomFragment = new RoomPagerFragment();
        roomFragment.setOnFragmentChangeListener(mOnFragmentChangeListener);
        roomFragment.getBuider(new HomePagerFragment.Buider()
        {
            @Override
            public String getRootFirebase()
            {
                return AppConstant.ROOMS_REF;
            }

            @Override
            public ICallBack getOnReadyCallback()
            {
                return new ICallBack()
                {
                    @Override
                    public void onCallBack()
                    {
                        roomFragment.onFetchData();
                    }
                };
            }
        });

        final TypePagerFragment typeFragment = new TypePagerFragment();
        typeFragment.setOnFragmentChangeListener(mOnFragmentChangeListener);
        typeFragment.getBuider(new HomePagerFragment.Buider()
        {
            @Override
            public String getRootFirebase()
            {
                return null;
            }

            @Override
            public ICallBack getOnReadyCallback()
            {
                return new ICallBack()
                {
                    @Override
                    public void onCallBack()
                    {
                        typeFragment.onFetchData();
                    }
                };
            }
        });


        final EventPagerFragment eventFragment = new EventPagerFragment();
        eventFragment.setOnFragmentChangeListener(mOnFragmentChangeListener);
        eventFragment.getBuider(new HomePagerFragment.Buider()
        {
            @Override
            public String getRootFirebase()
            {
                return AppConstant.EVENTS_REF;
            }

            @Override
            public ICallBack getOnReadyCallback()
            {
                return new ICallBack()
                {
                    @Override
                    public void onCallBack()
                    {
                        eventFragment.onFetchData();
                    }
                };
            }
        });

        listFragment[0] = roomFragment;
        listFragment[1] = typeFragment;
        listFragment[2] = eventFragment;

        return listFragment;
    }

    private class TabMenuController
    {
        View[] mMenuViews;
        ViewPager mTabs;
        int mCurrentMenuIndex = -1;
        int mSize = 0;

        public TabMenuController(View[] menuViews, ViewPager tabs)
        {
            mMenuViews = menuViews;
            mTabs = tabs;

            for (int index = 0; index < menuViews.length; index++)
            {
                final int tabIndex = index;
                View v = mMenuViews[index];
                v.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        switchTab(tabIndex);
                    }
                });
            }

            mTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
            {

                @Override
                public void onPageSelected(int idx)
                {
                    switchTab(idx);

                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2)
                {

                }

                @Override
                public void onPageScrollStateChanged(int arg0)
                {

                }
            });

            mSize = mMenuViews.length;

            if (mSize != mTabs.getAdapter().getCount())
            {
                throw new IllegalArgumentException();
            }

            mCurrentMenuIndex = DEFAULT_TAB_INDEX;

        }

        private boolean isRegularIndex(int idx)
        {
            return idx >= 0 && idx < mSize;
        }

        public void switchTab(final int index)
        {
            if (isRegularIndex(index))
            {
                if (mCurrentMenuIndex != index)
                {
                    if(isRegularIndex(mCurrentMenuIndex)) mMenuViews[mCurrentMenuIndex].setSelected(false);
                    mCurrentMenuIndex = index;
                    mMenuViews[mCurrentMenuIndex].setSelected(true);
                    mTabs.setCurrentItem(index);
                }
            }
        }
    }

}
