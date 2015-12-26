package app.hoai.bkit4u.home4u.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.hoai.bkit4u.home4u.R;
import app.hoai.bkit4u.home4u.model.HomeItemModel;

/**
 * Created by hoaipc on 11/20/15.
 */
public class HomePagerAdapter extends  BaseAdapter
{
    ArrayList<HomeItemModel> mListItem;

    public HomePagerAdapter()
    {
        mListItem = new ArrayList<>();
    }
    @Override
    public int getCount()
    {
        return mListItem.size();
    }

    @Override
    public HomeItemModel getItem(int position)
    {
        return mListItem.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        HomeItemModel model = getItem(position);

        LayoutInflater inflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rootView = inflater.inflate(R.layout.home_pager_item_layout,null);

        TextView title = (TextView) rootView.findViewById(R.id.title);
        title.setText(model.getTypeName());


        return rootView;
    }

    public void addItem(HomeItemModel item)
    {
        mListItem.add(item);
        notifyDataSetChanged();
    }

    public void addListItem(ArrayList<HomeItemModel> list)
    {
        mListItem.clear();
        mListItem.addAll(list);
        notifyDataSetChanged();
    }
}
