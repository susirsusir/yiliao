package com.yl.yiliao.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.Toast;

import com.yl.yiliao.R;
import com.yl.yiliao.adapter.CityAdapter;
import com.yl.yiliao.model.Province;

public class CityActivity extends BaseTopActivity {

    private CityAdapter mAdapter;
    private ListView mListView;
    private String countryName;
    private Province province;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("请选择国家地区");
        setBackRes(R.drawable.top_cancel);
        if (getIntent() != null) {
            province = (Province) getIntent().getSerializableExtra("Province");
            countryName = getIntent().getStringExtra("countryName");
        }
        if (province != null) {
            if (TextUtils.isEmpty(province.getName())) {
                mAdapter = new CityAdapter(this, province.getCity(), countryName);
            } else {
                mAdapter = new CityAdapter(this, province.getCity(), countryName + "." + province.getName());
            }
            mListView = (ListView) findViewById(R.id.listview);
            mListView.setAdapter(mAdapter);
        } else {
            Toast.makeText(this, "网络异常，请稍后重试!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected int getContentRes() {
        return R.layout.activity_city;
    }
}
