package com.yl.yiliao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yl.yiliao.R;
import com.yl.yiliao.model.SearchUserData;
import com.yl.yiliao.utils.Constants;
import com.yl.yiliao.utils.GsonUtils;
import com.yl.yiliao.utils.ImageUtiles;
import com.yl.yiliao.utils.RequestUtils;

public class UserInfoActivity extends AppCompatActivity {
    private final String TAG = UserInfoActivity.class.getSimpleName();

    private TextView tvName;
    private SimpleDraweeView ivHead;
    private TextView userSexTv;
    private TextView userCountryTv;
    private TextView userSignTv;
    private SearchUserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
        if (getIntent() != null) {
            String phoneNo = getIntent().getStringExtra("phoneNo");
            initData(phoneNo);
        }
    }

    private void initView() {
        tvName = (TextView) findViewById(R.id.user_name);
        ivHead = (SimpleDraweeView) findViewById(R.id.user_icon);
        userSexTv = (TextView) findViewById(R.id.user_sex);
        userCountryTv = (TextView) findViewById(R.id.user_country);
        userSignTv = (TextView) findViewById(R.id.user_sign_tv);
        findViewById(R.id.qr_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userData != null) {
                    // 查看二维码
                    Intent intent = new Intent(UserInfoActivity.this, MyQrCodeActivity.class);
                    intent.putExtra("phonemo", userData.getPhoneNo());
                    intent.putExtra("nickname", userData.getNickName());
                    intent.putExtra("qrcodeurl", userData.getQRCodeUrl());
                    intent.putExtra("avatar", userData.getAvatar());
                    startActivity(intent);
                }
            }
        });
    }

    private void initData(String phoneNo) {
        new RequestUtils(this, TAG, Constants.GET_OTHER_USER_INFO + phoneNo, new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {

                SearchUserData resultData = GsonUtils.getResult(result, SearchUserData.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        Toast.makeText(UserInfoActivity.this, resultData.getDescription(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (resultData.getData() != null) {
                        userData = resultData.getData();
                        reset(resultData.getData());
                    }
                }
            }

            @Override
            public void fail(VolleyError volleyError) {
                if (volleyError != null) {
                    Toast.makeText(UserInfoActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void reset(SearchUserData data) {
        tvName.setText(data.getNickName());
        ImageUtiles.load(ivHead, data.getAvatar());
        if ("f".equals(data.getSex())) {
            userSexTv.setText("女");
        } else if ("m".equals(data.getSex())) {
            userSexTv.setText("男");
        } else {
            userSexTv.setText("保密");
        }
        if (!TextUtils.isEmpty(data.getRegion())) {
            userCountryTv.setText(data.getRegion());
        }
        userSignTv.setText(data.getSignature());
    }


    /**
     * GET http://server:port/users/get_user_info?phoneNo={phoneNo}
     Auth: token_ phoneNo_ imei
     请求,查询非朋友用户的信息：
     curl -H "Content-Type: application/json" -H "Auth: 18100805251_qQUXbv3guQRlPEr_356696080597029" http://127.0.0.1:8080/users/get_user_info?phoneNo=13730106216
     响应:
     {"Data":{"PhoneNo":"13730106216","NickName":"13730106216","Avatar":"","Region":"","Signature":"这家伙很懒，很神秘","Sex":"m","RegisterTime":"2017-11-04 21:55:55","LastLoginTime":"2017-11-04 21:55:55","IsFriend":false},"Code":0,"Description":"OK"}

     请求,查询朋友用户信息：
     curl -H "Content-Type: application/json" -H "Auth: 18100805251_qQUXbv3guQRlPEr_356696080597029" http://127.0.0.1:8080/users/get_user_info?phoneNo=18358183215
     响应:
     {"Data":{"PhoneNo":"18358183215","NickName":"18358183215","Avatar":"","Region":"","Signature":"这家伙很懒，很神秘","Sex":"m","RegisterTime":"2017-11-04 21:46:46","LastLoginTime":"2017-11-04 21:46:46","IsFriend":true},"Code":0,"Description":"OK"}
     */
}
