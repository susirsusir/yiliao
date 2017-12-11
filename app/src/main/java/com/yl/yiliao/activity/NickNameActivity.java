package com.yl.yiliao.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.yl.yiliao.R;
import com.yl.yiliao.event.ChangeNameEvent;
import com.yl.yiliao.model.BaseData;
import com.yl.yiliao.utils.Constants;
import com.yl.yiliao.utils.GsonUtils;
import com.yl.yiliao.utils.RequestUtils;
import com.yl.yiliao.widget.EditTextLimitTextWatcher;

import de.greenrobot.event.EventBus;

public class NickNameActivity extends BaseTopActivity {
    private final String TAG = NickNameActivity.class.getSimpleName();
    private String nickName;
    private EditText mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("修改昵称");
        setShowRight();
        init();
        if (getIntent() != null) {
            nickName = getIntent().getStringExtra("nickName");
            mEdit.setText(nickName);
            if (!TextUtils.isEmpty(nickName)) {
                mEdit.setSelection(nickName.length());
            }
        }
    }

    private void init() {
        mEdit = (EditText) findViewById(R.id.ed_name);
        EditTextLimitTextWatcher mTextWatcher = new EditTextLimitTextWatcher(this, mEdit, 20, "字数超出限制了！");
        mEdit.addTextChangedListener(mTextWatcher);

    }


    @Override
    protected int getContentRes() {
        return R.layout.activity_nick_name;
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.right_tv:
                // 昵称修改确认
                nickName = mEdit.getText().toString().trim();
                changeData(nickName);
                break;

        }
    }


    private void changeData(final String nickname) {
        String url = Constants.CHANGE_USER_INFO + "?type=nickname&value=" + nickname;
        new RequestUtils(this, TAG, url, new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {

                BaseData resultData = GsonUtils.getResult(result, BaseData.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        Toast.makeText(NickNameActivity.this, resultData.getDescription(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(NickNameActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new ChangeNameEvent(nickname));
                    finish();

                }
            }

            @Override
            public void fail(VolleyError volleyError) {
                if (volleyError != null) {
                    Toast.makeText(NickNameActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
