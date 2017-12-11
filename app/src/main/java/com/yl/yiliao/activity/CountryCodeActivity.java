package com.yl.yiliao.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.yl.yiliao.R;
import com.yl.yiliao.adapter.CountryAdapter;
import com.yl.yiliao.event.ChangeCountryEvent;
import com.yl.yiliao.model.CountryItem;
import com.yl.yiliao.model.CountryListData;
import com.yl.yiliao.model.GPSData;
import com.yl.yiliao.utils.Constants;
import com.yl.yiliao.utils.GsonUtils;
import com.yl.yiliao.utils.RequestUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 修改国家地区
 */
public class CountryCodeActivity extends BaseTopActivity {
    private final String TAG = CountryCodeActivity.class.getSimpleName();

    private double latitude;
    private double longitude;
    private ListView mListView;
    private String location;
    private String select;
    private List<CountryItem> items = new ArrayList<>();
    private CountryAdapter mAdapter;
    private TextView tvCurrent;
    private TextView tvSelect;
    private RelativeLayout selectLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("请选择国家地区");
        setBackRes(R.drawable.top_cancel);
        initView();
        EventBus.getDefault().register(this);

        if (getIntent() != null) {
            latitude = getIntent().getDoubleExtra("latitude", 0);
            longitude = getIntent().getDoubleExtra("longitude", 0);
            select = getIntent().getStringExtra("select");
            if (TextUtils.isEmpty(select)) {
                selectLay.setVisibility(View.GONE);
            } else {
                selectLay.setVisibility(View.VISIBLE);
                tvSelect.setText(select);
            }
        }
        if (latitude > 0 && longitude > 0) {
            initGPSData();
        } else {
            tvCurrent.setText("定位失败");
        }
        initCodeData();
    }

    private void initCodeData() {
        new RequestUtils(this, TAG, Constants.GET_REGION_LIST, new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {

                CountryListData resultData = GsonUtils.getResult(result, CountryListData.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        Toast.makeText(CountryCodeActivity.this, resultData.getDescription(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (resultData.getData() != null) {
                        items.addAll(resultData.getData().getCountryRegion());
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void fail(VolleyError volleyError) {
                if (volleyError != null) {
                    Toast.makeText(CountryCodeActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initGPSData() {
        new RequestUtils(this, TAG, Constants.GET_GPS + latitude + "," + longitude, new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {

                GPSData resultData = GsonUtils.getResult(result, GPSData.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        Toast.makeText(CountryCodeActivity.this, resultData.getDescription(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (resultData.getData() != null) {
                        location = resultData.getData();
                        tvCurrent.setText(location);
                    }
                }
            }

            @Override
            public void fail(VolleyError volleyError) {
                if (volleyError != null) {
                    Toast.makeText(CountryCodeActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        mAdapter = new CountryAdapter(this, items);
        mListView.setAdapter(mAdapter);
        tvCurrent = (TextView) findViewById(R.id.tv_current);
        tvSelect = (TextView) findViewById(R.id.select_tv);
        selectLay = (RelativeLayout) findViewById(R.id.select_lay);

    }

    @Override
    protected int getContentRes() {
        return R.layout.activity_country_code;
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
