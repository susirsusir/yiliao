package com.yl.yiliao.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yl.yiliao.R;

public abstract class BaseTopActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView titleTv;
    private TextView rightTv;
    private ImageView backIV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_top);
        backIV = (ImageView) findViewById(R.id.icon_back);
        backIV.setOnClickListener(this);
        titleTv = (TextView) findViewById(R.id.title);
        rightTv = (TextView) findViewById(R.id.right_tv);
        rightTv.setOnClickListener(this);
        RelativeLayout mainView = (RelativeLayout) findViewById(R.id.mian_view);
        mainView.addView(View.inflate(this, getContentRes(), null), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setBackRes(int resId){
        backIV.setImageResource(resId);
    }


    public void setTitle(String title){
        titleTv.setText(title);
    }

    public void setShowRight(){
        rightTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icon_back:
                finish();
                break;
        }

    }

    protected abstract int getContentRes();
}
