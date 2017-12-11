package com.yl.yiliao.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yl.yiliao.R;
import com.yl.yiliao.utils.ImageUtiles;

public class MyQrCodeActivity extends BaseTopActivity {
    private final String TAG = MyQrCodeActivity.class.getSimpleName();
    private SimpleDraweeView userIcon;
    private TextView tvName;
    private TextView tvPhone;
    private SimpleDraweeView qrIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("我的二维码");
        initView();
        initData();
    }

    @Override
    protected int getContentRes() {
        return R.layout.activity_my_qr_code;
    }

    private void initView() {
        userIcon = (SimpleDraweeView) findViewById(R.id.user_icon);
        tvName = (TextView) findViewById(R.id.nametv);
        tvPhone = (TextView) findViewById(R.id.phonetv);
        qrIcon = (SimpleDraweeView) findViewById(R.id.user_qr);
    }

    private void initData() {
        if (getIntent() != null) {
            String phoneno = getIntent().getStringExtra("phonemo");
            String nickname = getIntent().getStringExtra("nickname");
            String qrcodeurl = getIntent().getStringExtra("qrcodeurl");
            String avatar = getIntent().getStringExtra("avatar");
            ImageUtiles.load(userIcon, avatar);
            ImageUtiles.load(qrIcon, qrcodeurl);
            tvName.setText(nickname);
            tvPhone.setText("(" + phoneno.substring(0, 2) + ") " + phoneno.substring(2));

        }

    }
}
