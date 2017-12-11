package com.yl.yiliao.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.yl.yiliao.R;
import com.yl.yiliao.event.ChangeSignEvent;
import com.yl.yiliao.model.BaseData;
import com.yl.yiliao.utils.Constants;
import com.yl.yiliao.utils.GsonUtils;
import com.yl.yiliao.utils.RequestUtils;
import com.yl.yiliao.widget.EditTextLimitTextWatcher;

import de.greenrobot.event.EventBus;

public class SignActivity extends BaseTopActivity {
    private final String TAG = SignActivity.class.getSimpleName();

    private EditText mEdit;
    private String mySign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("签名");
        setShowRight();
        init();
        if (getIntent() != null) {
            mySign = getIntent().getStringExtra("sign");
            mEdit.setText(mySign);
            if (!TextUtils.isEmpty(mySign)) {
                mEdit.setSelection(mySign.length());
            }
        }
    }

    private void init() {
        mEdit = (EditText) findViewById(R.id.ver_code_ed);
        EditTextLimitTextWatcher mTextWatcher = new EditTextLimitTextWatcher(this, mEdit, 20, "字数超出限制了！");
        mEdit.addTextChangedListener(mTextWatcher);

    }

    @Override
    protected int getContentRes() {
        return R.layout.activity_sign;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.right_tv:
                // 签名修改确认
                mySign = mEdit.getText().toString().trim();
                changeData(mySign);
                break;

        }
    }


    private void changeData(final String signature) {
        String url = Constants.CHANGE_USER_INFO + "?type=signature&value=" + signature;
        new RequestUtils(this, TAG, url, new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {

                BaseData resultData = GsonUtils.getResult(result, BaseData.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        Toast.makeText(SignActivity.this, resultData.getDescription(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(SignActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new ChangeSignEvent(signature));
                    finish();

                }
            }

            @Override
            public void fail(VolleyError volleyError) {
                if (volleyError != null) {
                    Toast.makeText(SignActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
