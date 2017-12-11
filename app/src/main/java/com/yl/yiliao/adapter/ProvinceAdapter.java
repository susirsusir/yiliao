package com.yl.yiliao.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yl.yiliao.R;
import com.yl.yiliao.activity.CityActivity;
import com.yl.yiliao.event.ChangeCountryEvent;
import com.yl.yiliao.model.Province;

import java.util.List;

import de.greenrobot.event.EventBus;

public class ProvinceAdapter extends BaseAdapter {

    private Context mContext;
    private List<Province> items;
    private String countryName;

    public ProvinceAdapter(Context mContext, List<Province> items, String countryName) {
        this.mContext = mContext;
        this.items = items;
        this.countryName = countryName;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.country_item, null);
            holder.itemName = view.findViewById(R.id.tv);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final Province item = items.get(position);
        holder.itemName.setText(item.getName());
        holder.itemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getCity() != null) {
                    Intent intent = new Intent(mContext, CityActivity.class);
                    intent.putExtra("Province", item);
                    intent.putExtra("countryName", countryName);
                    mContext.startActivity(intent);
                } else {
                    EventBus.getDefault().post(new ChangeCountryEvent(countryName + "." + item.getName()));
                }

            }
        });

        return view;
    }

    public static class ViewHolder {
        TextView itemName;
    }

}