package com.yl.yiliao.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.yl.yiliao.R;
import com.yl.yiliao.event.ChangeSexEvent;
import com.yl.yiliao.model.BaseData;
import com.yl.yiliao.utils.Constants;
import com.yl.yiliao.utils.GsonUtils;
import com.yl.yiliao.utils.RequestUtils;

import de.greenrobot.event.EventBus;

public class SexActivity extends BaseTopActivity {
    private final String TAG = SexActivity.class.getSimpleName();
    private View manHint;
    private View womanHint;
    private View baomiHint;
    private TextView manTv;
    private TextView womanTv;
    private TextView baomiTv;
    private String sex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("选择性别");
        init();
        if (getIntent() != null) {
            sex = getIntent().getStringExtra("sex");
            if ("f".equals(sex)) {
                setShowHint(1);
            } else if ("m".equals(sex)) {
                setShowHint(0);
            } else {
                setShowHint(2);
            }

        }
    }

    private void init() {
        manHint = findViewById(R.id.man_iv);
        womanHint = findViewById(R.id.woman_iv);
        baomiHint = findViewById(R.id.baomi_iv);
        findViewById(R.id.baomi_lay).setOnClickListener(this);
        findViewById(R.id.woman_lay).setOnClickListener(this);
        findViewById(R.id.man_lay).setOnClickListener(this);
        manTv = (TextView) findViewById(R.id.man_tv);
        womanTv = (TextView) findViewById(R.id.woman_tv);
        baomiTv = (TextView) findViewById(R.id.baomi_tv);

    }

    @Override
    protected int getContentRes() {
        return R.layout.activity_sex;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.baomi_lay:
                setShowHint(2);
                changeData("u");
                break;
            case R.id.woman_lay:
                setShowHint(1);
                changeData("f");
                break;
            case R.id.man_lay:
                setShowHint(0);
                changeData("m");
                break;
        }
    }

    private void setShowHint(int index) {
        manTv.setTextColor(index == 0 ? doColor(R.color.cursor_color) : doColor(R.color.edit_text_color));
        womanTv.setTextColor(index == 1 ? doColor(R.color.cursor_color) : doColor(R.color.edit_text_color));
        baomiTv.setTextColor(index == 2 ? doColor(R.color.cursor_color) : doColor(R.color.edit_text_color));
        manHint.setVisibility(index == 0 ? View.VISIBLE : View.GONE);
        womanHint.setVisibility(index == 1 ? View.VISIBLE : View.GONE);
        baomiHint.setVisibility(index == 2 ? View.VISIBLE : View.GONE);

    }

    public int doColor(int colorId) {
        return getResources().getColor(colorId);
    }

    private void changeData(final String sex) {
        String url = Constants.CHANGE_USER_INFO + "?type=sex&value=" + sex;
        new RequestUtils(this, TAG, url, new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {

                BaseData resultData = GsonUtils.getResult(result, BaseData.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        Toast.makeText(SexActivity.this, resultData.getDescription(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(SexActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new ChangeSexEvent(sex));

                }
            }

            @Override
            public void fail(VolleyError volleyError) {
                if (volleyError != null) {
                    Toast.makeText(SexActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
