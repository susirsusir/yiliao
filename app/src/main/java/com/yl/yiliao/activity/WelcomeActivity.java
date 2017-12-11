package com.yl.yiliao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.volley.VolleyError;
import com.yl.yiliao.R;
import com.yl.yiliao.helper.UserHelper;
import com.yl.yiliao.model.RegisterResponse;
import com.yl.yiliao.params.WelLoginUser;
import com.yl.yiliao.utils.Constants;
import com.yl.yiliao.utils.GsonUtils;
import com.yl.yiliao.utils.RequestUtils;
import com.yl.yiliao.utils.SPUtils;

public class WelcomeActivity extends AppCompatActivity {
    private final String TAG = WelcomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        timer.start();
        if(UserHelper.isLogin(this)){
            initLogin();
        }
    }

    private CountDownTimer timer = new CountDownTimer(3000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            startActivity();
        }
    };

    private void startActivity() {
        startActivity(new Intent(this, MainActivity.class));
        if (timer != null) {
            timer = null;
        }
        finish();
    }

    public void imagClick(View v) {
        // 点击跳转暂时不做处理
    }

    // 做登陆校验，失败则更新本地数据
    private void initLogin() {
        WelLoginUser user = new WelLoginUser();
        user.setPhoneNo(SPUtils.getPhoneNo(this));
        user.setImei(SPUtils.getImei(this));
        user.setToken(SPUtils.getToken(this));

        new RequestUtils(this, TAG, Constants.LOGIN, user, new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {

                RegisterResponse resultData = GsonUtils.getResult(result, RegisterResponse.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        resetInf();
                        return;
                    }
                    if (resultData.getData() != null) {
                        saveUserInfo(resultData.getData());
                    }
                }
            }

            @Override
            public void fail(VolleyError volleyError) {
                // 暂时不做统一处理，留待以后可能会有差异情况处理
                resetInf();
            }
        });
    }

    private void resetInf() {
        SPUtils.setPhoneNo("", this);
        SPUtils.setToken("", this);
    }

    private void saveUserInfo(RegisterResponse resultData){
        SPUtils.setToken(resultData.getToken(),this);
        SPUtils.setPhoneNo(resultData.getPhoneNo(),this);
    }

}
