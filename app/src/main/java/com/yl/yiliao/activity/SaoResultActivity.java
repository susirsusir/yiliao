package com.yl.yiliao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yl.yiliao.R;
import com.yl.yiliao.event.AddSubScribeEvent;
import com.yl.yiliao.event.RefreshFriendEvent;
import com.yl.yiliao.model.BaseData;
import com.yl.yiliao.model.SearchUserData;
import com.yl.yiliao.utils.Constants;
import com.yl.yiliao.utils.GsonUtils;
import com.yl.yiliao.utils.ImageUtiles;
import com.yl.yiliao.utils.RequestUtils;

import de.greenrobot.event.EventBus;

public class SaoResultActivity extends BaseTopActivity {
    private final String TAG = SaoResultActivity.class.getSimpleName();

    private RelativeLayout emptyLay;
    private RelativeLayout userLay;
    private SimpleDraweeView userIcon;
    private TextView userName;
    private TextView userNo;
    private TextView addBtn;
    private String phoneno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("扫一扫");
        initView();
        if (getIntent() != null) {
            phoneno = getIntent().getStringExtra("phoneno");
            searchData(phoneno);
        }
    }

    private void initView() {
        emptyLay = (RelativeLayout) findViewById(R.id.empty_lay);
        userLay = (RelativeLayout) findViewById(R.id.user_lay);
        userIcon = (SimpleDraweeView) findViewById(R.id.user_icon);
        userIcon.setOnClickListener(this);
        userName = (TextView) findViewById(R.id.user_name);
        userNo = (TextView) findViewById(R.id.user_phone);
        addBtn = (TextView) findViewById(R.id.add_btn);
        addBtn.setOnClickListener(this);
    }

    @Override
    protected int getContentRes() {
        return R.layout.activity_sao_result;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.add_btn:
                // 添加好友
                addFriend(mData.getPhoneNo());
                break;
            case R.id.user_icon:
                Intent intent = new Intent(this, UserInfoActivity.class);
                if (mData != null) {
                    intent.putExtra("phoneNo", mData.getPhoneNo());
                }
                startActivity(intent);
                break;
        }
    }

    private void addFriend(final String phone) {
        new RequestUtils(this, TAG, Constants.ADD_FRIEND + phone, new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {

                BaseData resultData = GsonUtils.getResult(result, BaseData.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        Toast.makeText(SaoResultActivity.this, resultData.getDescription(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    EventBus.getDefault().post(new AddSubScribeEvent(phone));
                    EventBus.getDefault().post(new RefreshFriendEvent());
                    addBtn.setText("已添加");
                    addBtn.setBackgroundResource(R.drawable.btn_get_code_sel);
                    addBtn.setTextColor(getResources().getColor(R.color.edit_hint_color));
                    addBtn.setClickable(false);
                }
            }

            @Override
            public void fail(VolleyError volleyError) {
                if (volleyError != null) {
                    Toast.makeText(SaoResultActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                }
                emptyLay.setVisibility(View.VISIBLE);
                userLay.setVisibility(View.GONE);
            }
        });
    }


    private void searchData(String phone) {
        new RequestUtils(this, TAG, Constants.GET_OTHER_USER_INFO + phone, new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {

                SearchUserData resultData = GsonUtils.getResult(result, SearchUserData.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        Toast.makeText(SaoResultActivity.this, resultData.getDescription(), Toast.LENGTH_SHORT).show();
                        emptyLay.setVisibility(View.VISIBLE);
                        userLay.setVisibility(View.GONE);
                        return;
                    }
                    if (resultData.getData() != null) {
                        mData = resultData.getData();
                        setData(resultData.getData());
                    } else {
                        emptyLay.setVisibility(View.VISIBLE);
                        userLay.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void fail(VolleyError volleyError) {
                if (volleyError != null) {
                    Toast.makeText(SaoResultActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                }
                emptyLay.setVisibility(View.VISIBLE);
                userLay.setVisibility(View.GONE);
            }
        });
    }

    private SearchUserData mData;

    private void setData(SearchUserData data) {
        emptyLay.setVisibility(View.GONE);
        userLay.setVisibility(View.VISIBLE);
        ImageUtiles.load(userIcon, data.getAvatar());
        userName.setText(data.getNickName());
        userNo.setText("(+" + data.getPhoneNo().substring(0, 2) + ") " + data.getPhoneNo().substring(2));
        if (data.isFriend()) {
            addBtn.setText("已添加");
            addBtn.setBackgroundResource(R.drawable.btn_get_code_sel);
            addBtn.setTextColor(getResources().getColor(R.color.edit_hint_color));
            addBtn.setClickable(false);
        } else {
            addBtn.setText("加好友");
            addBtn.setBackgroundResource(R.drawable.btn_get_code);
            addBtn.setTextColor(getResources().getColor(R.color.cursor_color));
            addBtn.setClickable(true);
        }
        addBtn = (TextView) findViewById(R.id.add_btn);
    }
}
