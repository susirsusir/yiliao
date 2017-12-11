package com.yl.yiliao.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.yl.yiliao.R;
import com.yl.yiliao.event.LoginEvent;
import com.yl.yiliao.model.CountryCodeData;
import com.yl.yiliao.model.CountryCodeItem;
import com.yl.yiliao.model.RegisterResponse;
import com.yl.yiliao.model.ValidateData;
import com.yl.yiliao.params.LoginUser;
import com.yl.yiliao.params.RegisterUser;
import com.yl.yiliao.utils.Constants;
import com.yl.yiliao.utils.GsonUtils;
import com.yl.yiliao.utils.RequestUtils;
import com.yl.yiliao.utils.SPUtils;
import com.yl.yiliao.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * 暂时需要维护两套登陆模块，以后整合使用NoLoginEmptyView作为登陆模块
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = LoginActivity.class.getSimpleName();
    // 用来判断登陆或者注册
    private boolean isLogin;
    private TextView tvRegister;
    private View phoneHint;
    private View codeHint;
    private TextView phoneErrHint;
    private TextView codeErrorHint;
    private EditText phoneEd;
    private EditText codeEd;
    private TextView getCodeTv;
    private TextView countryCode;
    private CountDownTimer timer;
    // 默认国家区号为86
    private String defaultCode = "86";
    private List<String> codeList = new ArrayList<>();
    private List<CountryCodeItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getIntent() != null) {
            isLogin = getIntent().getBooleanExtra("isLogin", true);
        }
        initView();
    }

    private void initView() {
        tvRegister = (TextView) findViewById(R.id.register_hint);
        phoneHint = findViewById(R.id.phone_hint_tv);
        codeHint = findViewById(R.id.ver_code);
        phoneErrHint = (TextView) findViewById(R.id.phone_error_hint);
        codeErrorHint = (TextView) findViewById(R.id.code_error_hint);
        phoneEd = (EditText) findViewById(R.id.phont_ed);
        codeEd = (EditText) findViewById(R.id.ver_code_ed);
        TextView titleTv = (TextView) findViewById(R.id.titale);
        TextView pushBtn = (TextView) findViewById(R.id.push_btn);
        pushBtn.setOnClickListener(this);
        getCodeTv = (TextView) findViewById(R.id.get_code_hint);
        getCodeTv.setOnClickListener(this);
        findViewById(R.id.cencel).setOnClickListener(this);
        findViewById(R.id.country_code_lay).setOnClickListener(this);
        countryCode = (TextView) findViewById(R.id.country_code_tv);
        if (isLogin) {
            StringUtils.setRegisterText(this, tvRegister);
        } else {
            titleTv.setText("注册");
            pushBtn.setText("提交");
        }
        initPhoneEd();
        initCodeEd();
        initGetCode();
        initPhoneData();
    }

    private void initPhoneEd() {
        codeEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence)) {
                    codeHint.setVisibility(View.INVISIBLE);
                } else {
                    codeHint.setVisibility(View.VISIBLE);
                }
                if (codeErrorHint.getVisibility() == View.VISIBLE) {
                    codeErrorHint.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initCodeEd() {
        phoneEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence)) {
                    phoneHint.setVisibility(View.INVISIBLE);
                } else {
                    phoneHint.setVisibility(View.VISIBLE);
                }
                if (phoneErrHint.getVisibility() == View.VISIBLE) {
                    phoneErrHint.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initGetCode() {
        timer = new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                getCodeTv.setText((millisUntilFinished / 1000) + " s");

            }

            @Override
            public void onFinish() {
                getCodeTv.setEnabled(true);
                getCodeTv.setBackgroundResource(R.drawable.btn_get_code);
                getCodeTv.setTextColor(getResources().getColor(R.color.cursor_color));
                getCodeTv.setText("获取验证码");
            }
        };
    }

    private void initPhoneData() {
        new RequestUtils(this, TAG, Constants.GET_PHONE_CODE, new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {

                CountryCodeData resultData = GsonUtils.getResult(result, CountryCodeData.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        Toast.makeText(LoginActivity.this, resultData.getDescription(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (resultData.getData() != null) {
                        items = resultData.getData();
                        setList(resultData.getData());
                    }
                }
            }

            @Override
            public void fail(VolleyError volleyError) {
                if (volleyError != null) {
                    Toast.makeText(LoginActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void setList(List<CountryCodeItem> items) {
        if (items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                CountryCodeItem item = items.get(i);
                codeList.add(item.getName() + "  (+" + item.getCode() + ")");
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cencel:
                finish();
                break;
            case R.id.country_code_lay:
                showCodeDialog();
                break;
            case R.id.get_code_hint:
                String phoneNo = phoneEd.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNo)) {
                    phoneErrHint.setText("号码不能为空");
                    phoneErrHint.setVisibility(View.VISIBLE);
                    return;
                }
                getValidateCode(phoneNo);
                getCodeTv.setEnabled(false);
                getCodeTv.setBackgroundResource(R.drawable.btn_get_code_sel);
                getCodeTv.setTextColor(getResources().getColor(R.color.edit_hint_color));
                getCodeTv.setText("60s");
                timer.start();
                break;
            case R.id.push_btn:
                String phone = phoneEd.getText().toString().trim();
                if (!TextUtils.isEmpty(phone)) {
                    if (phone.length() > 20) {
                        Toast.makeText(this, "手机号长度不能大于20位", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String code = codeEd.getText().toString().trim();
                    if (!TextUtils.isEmpty(code)) {
                        if (isLogin) {
                            toLogin(phone, code);
                        } else {
                            toRegister(phone, code);
                        }
                    } else {
                        codeErrorHint.setText("验证码不能为空");
                        codeErrorHint.setVisibility(View.VISIBLE);
                    }
                } else {
                    phoneErrHint.setText("号码不能为空");
                    phoneErrHint.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private AlertDialog.Builder codeBuilder;

    private void showCodeDialog() {
        if (codeList.size() == 0) {
            return;
        }
        if (codeBuilder == null) {
            codeBuilder = new AlertDialog.Builder(this);
            codeBuilder.setItems(codeList.toArray(new String[codeList.size()]), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (items != null) {
                        defaultCode = items.get(i).getCode();
                    }
                    countryCode.setText(codeList.get(i));
                }
            });
        }
        codeBuilder.show();
    }

    private void getValidateCode(String phoneNo) {
        String url = Constants.GET_VALIDATE_CODE + defaultCode + phoneNo;
        new RequestUtils(this, TAG, url, new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {

                ValidateData resultData = GsonUtils.getResult(result, ValidateData.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        Toast.makeText(LoginActivity.this, resultData.getDescription(), Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    if (timer != null) {
//                        timer.onFinish();
//                        timer.cancel();
//                    }
                }
            }

            @Override
            public void fail(VolleyError volleyError) {
                if (volleyError != null) {
                    Toast.makeText(LoginActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private void toRegister(String phone, String code) {
        RegisterUser user = new RegisterUser();
        user.setPhoneNo(defaultCode + phone);
        user.setImei(SPUtils.getImei(this));
        user.setNickName("");
        user.setValidateCode(code);

        new RequestUtils(this, TAG, Constants.REGISTER, user, new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {

                RegisterResponse resultData = GsonUtils.getResult(result, RegisterResponse.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        Toast.makeText(LoginActivity.this, resultData.getDescription(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (resultData.getData() != null) {
                        saveUserInfo(resultData.getData());
                        // 登陆成功，跳转到主页，发送通知，需要更新数据的界面进行数据更新
                        jump();

                    }
                    Log.e("message", resultData.getData().getPhoneNo() + " " + resultData.getData().getExpireTime());
                }
            }

            @Override
            public void fail(VolleyError volleyError) {
                // 暂时不做统一处理，留待以后可能会有差异情况处理
                if (volleyError != null) {
                    Toast.makeText(LoginActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveUserInfo(RegisterResponse resultData) {
        SPUtils.setToken(resultData.getToken(), this);
        SPUtils.setPhoneNo(resultData.getPhoneNo(), this);
    }

    private void toLogin(String phone, String code) {
        LoginUser user = new LoginUser();
        user.setPhoneNo(defaultCode + phone);
        user.setImei(SPUtils.getImei(this));
        user.setToken("");
        user.setValidateCode(code);

        new RequestUtils(this, TAG, Constants.LOGIN, user, new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {

                RegisterResponse resultData = GsonUtils.getResult(result, RegisterResponse.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        Toast.makeText(LoginActivity.this, resultData.getDescription(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (resultData.getData() != null) {
                        saveUserInfo(resultData.getData());
                        // 登录成功，跳转到主页，发送通知，需要更新数据的界面进行数据更新
                        jump();
                    }
                    Log.e("message", resultData.getData().getPhoneNo() + " " + resultData.getData().getExpireTime());
                }
            }

            @Override
            public void fail(VolleyError volleyError) {
                // 暂时不做统一处理，留待以后可能会有差异情况处理
                if (volleyError != null) {
                    Toast.makeText(LoginActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void jump() {
        EventBus.getDefault().post(new LoginEvent(true));
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
