package com.yl.yiliao.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.yl.yiliao.R;
import com.yl.yiliao.adapter.ProvinceAdapter;
import com.yl.yiliao.event.ChangeCountryEvent;
import com.yl.yiliao.model.CountryItem;

import de.greenrobot.event.EventBus;

public class ProvinceActivity extends BaseTopActivity {

    private ListView mListView;
    private ProvinceAdapter mAdapter;
    private CountryItem countryItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("请选择国家地区");
        setBackRes(R.drawable.top_cancel);
        EventBus.getDefault().register(this);
        if (getIntent() != null) {
            countryItem = (CountryItem) getIntent().getSerializableExtra("CountryItem");
        }
        if (countryItem != null) {
            mListView = (ListView) findViewById(R.id.listview);
            mAdapter = new ProvinceAdapter(this, countryItem.getState(), countryItem.getName());
            mListView.setAdapter(mAdapter);

        } else {
            Toast.makeText(this, "网络异常，请稍后重试!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected int getContentRes() {
        return R.layout.activity_province;
    }

    // 修改地区
    public void onEventMainThread(ChangeCountryEvent event) {
        if (event != null) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }
}
