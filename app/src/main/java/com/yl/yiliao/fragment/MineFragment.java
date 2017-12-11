package com.yl.yiliao.fragment;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yl.yiliao.R;
import com.yl.yiliao.activity.CountryCodeActivity;
import com.yl.yiliao.activity.MainActivity;
import com.yl.yiliao.activity.MyQrCodeActivity;
import com.yl.yiliao.activity.NickNameActivity;
import com.yl.yiliao.activity.SexActivity;
import com.yl.yiliao.activity.SignActivity;
import com.yl.yiliao.event.ChangeAvatorEvent;
import com.yl.yiliao.event.ChangeCountryEvent;
import com.yl.yiliao.event.ChangeNameEvent;
import com.yl.yiliao.event.ChangeSexEvent;
import com.yl.yiliao.event.ChangeSignEvent;
import com.yl.yiliao.event.LoginEvent;
import com.yl.yiliao.helper.UserHelper;
import com.yl.yiliao.model.AvaterData;
import com.yl.yiliao.model.BaseData;
import com.yl.yiliao.model.UserSelfInfo;
import com.yl.yiliao.utils.Constants;
import com.yl.yiliao.utils.DialogUtil;
import com.yl.yiliao.utils.GsonUtils;
import com.yl.yiliao.utils.ImageUtiles;
import com.yl.yiliao.utils.LocationUtils;
import com.yl.yiliao.utils.RequestUtils;
import com.yl.yiliao.utils.SPUtils;
import com.yl.yiliao.utils.StringUtils;
import com.yl.yiliao.utils.UpFileRequestUtils;

import de.greenrobot.event.EventBus;


/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {
    private final String TAG = MineFragment.class.getSimpleName();


    public MineFragment() {
        // Required empty public constructor
    }

    private TextView tvName;
    private ImageView ivHead;
    private TextView userSexTv;
    private TextView userCountryTv;
    private TextView userSignTv;
    private ImageView outBtn;
    private TextView tvShowSign;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {

            mView = inflater.inflate(R.layout.fragment_mine, container, false);
            initView();
        } else {
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
        }
        EventBus.getDefault().register(this);
        if (UserHelper.isLogin(mContext)) {
            initData();
        } else {
            // 重置数据
            reset();
        }
        return mView;
    }

    private View mView;

    private void initView() {
        tvName = mView.findViewById(R.id.user_name);
        tvName.setOnClickListener(this);
        ivHead = mView.findViewById(R.id.user_icon);
        ivHead.setOnClickListener(this);
        userSexTv = mView.findViewById(R.id.user_sex);
        userSexTv.setOnClickListener(this);
        userCountryTv = mView.findViewById(R.id.user_country);
        userCountryTv.setOnClickListener(this);
        userSignTv = mView.findViewById(R.id.user_sign_tv);
        userSignTv.setOnClickListener(this);
        mView.findViewById(R.id.qr_iv).setOnClickListener(this);
        mView.findViewById(R.id.sex_lay).setOnClickListener(this);
        mView.findViewById(R.id.diqu_lay).setOnClickListener(this);
        mView.findViewById(R.id.qianming_lay).setOnClickListener(this);
        mView.findViewById(R.id.erweima_lay).setOnClickListener(this);
        outBtn = mView.findViewById(R.id.login_out);
        outBtn.setOnClickListener(this);
        tvShowSign = mView.findViewById(R.id.user_sign);
    }

    private void initData() {
        outBtn.setVisibility(View.VISIBLE);
        new RequestUtils(mContext, TAG, Constants.GET_SELF_INFO, new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {

                UserSelfInfo resultData = GsonUtils.getResult(result, UserSelfInfo.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        Toast.makeText(mContext, resultData.getDescription(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (resultData.getData() != null) {
                        mInfo = resultData.getData();
                        reset(resultData.getData());
                    }
                }
            }

            @Override
            public void fail(VolleyError volleyError) {
                if (volleyError != null) {
                    Toast.makeText(mContext, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_name:
                if (UserHelper.autoLogin(mContext)) {
                    // 修改昵称
                    Intent intent = new Intent(mContext, NickNameActivity.class);
                    if (mInfo != null) {
                        intent.putExtra("nickName", mInfo.getNickName());
                    }
                    startActivity(intent);
                }
                break;
            case R.id.user_icon:
                if (UserHelper.autoLogin(mContext)) {
                    // 修改头像
                    ((MainActivity) mContext).requestPermissionAlbum();
                }
                break;
            case R.id.user_sex:
            case R.id.sex_lay:
                if (UserHelper.autoLogin(mContext)) {
                    // 修改性别
                    Intent intent = new Intent(mContext, SexActivity.class);
                    if (mInfo != null) {
                        intent.putExtra("sex", mInfo.getSex());
                    }
                    startActivity(intent);
                }
                break;
            case R.id.user_sign_tv:
            case R.id.qianming_lay:
                if (UserHelper.autoLogin(mContext)) {
                    // 修改签名
                    Intent intent = new Intent(mContext, SignActivity.class);
                    if (mInfo != null) {
                        intent.putExtra("sign", mInfo.getSignature());
                    }
                    startActivity(intent);
                }
                break;
            case R.id.qr_iv:
            case R.id.erweima_lay:
                if (UserHelper.autoLogin(mContext)) {
                    // 查看二维码
                    Intent intent = new Intent(mContext, MyQrCodeActivity.class);
                    if (mInfo != null) {
                        intent.putExtra("phonemo", mInfo.getPhoneNo());
                        intent.putExtra("nickname", mInfo.getNickName());
                        intent.putExtra("qrcodeurl", mInfo.getQRCodeUrl());
                        intent.putExtra("avatar", mInfo.getAvatar());
                    }
                    startActivity(intent);
                }
                break;
            case R.id.user_country:
            case R.id.diqu_lay:
                if (UserHelper.autoLogin(mContext)) {
                    Location location = LocationUtils.getLocation(mContext);
                    Intent intent = new Intent(mContext, CountryCodeActivity.class);
                    if (mInfo != null && !TextUtils.isEmpty(mInfo.getRegion())) {
                        intent.putExtra("select", mInfo.getRegion());
                    }
                    if (location != null) {
                        intent.putExtra("latitude", location.getLatitude());
                        intent.putExtra("longitude", location.getLongitude());
                        intent.putExtra("select", mInfo.getRegion());
                    }
                    mContext.startActivity(intent);
                }
                break;
            case R.id.login_out:
                // 退出登录
                loginOff();
                break;
        }

    }

    private void loginOff() {
        DialogUtil.showDialogNoTitle(mContext, "确认退出当前账号？", "确定", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                SPUtils.setToken("", mContext);
                EventBus.getDefault().post(new LoginEvent(false));
                dialog.dismiss();
            }
        }, "取消", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    // 是否登陆
    public void onEventMainThread(LoginEvent event) {
        if (event != null) {
            if (event.isSueecss()) {
                // 登陆成功 获取个人信息
                initData();
            } else {
                // 重置数据
                reset();
            }
        }
    }

    // 修改性别
    public void onEventMainThread(ChangeSexEvent event) {
        if (event != null) {
            mInfo.setSex(event.getSex());
            if ("f".equals(event.getSex())) {
                userSexTv.setText("女");
            } else if ("m".equals(event.getSex())) {
                userSexTv.setText("男");
            } else {
                userSexTv.setText("保密");
            }
        }
    }

    // 修改昵称
    public void onEventMainThread(ChangeNameEvent event) {
        if (event != null) {
            mInfo.setNickName(event.getName());
            tvName.setText(event.getName());
        }
    }

    // 修改地区
    public void onEventMainThread(ChangeCountryEvent event) {
        if (event != null) {
            mInfo.setRegion(event.getCountry());
            userCountryTv.setText(event.getCountry());
            String url = Constants.CHANGE_USER_INFO + "?type=region&value=" + StringUtils.encode(event.getCountry());
            new RequestUtils(mContext, TAG, url, new RequestUtils.RequestCallback() {
                @Override
                public void success(String result) {

                    BaseData resultData = GsonUtils.getResult(result, BaseData.class);
                    if (resultData != null) {
                        if (resultData.getCode() != 0) {
                            Toast.makeText(mContext, resultData.getDescription(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(mContext, "地区修改成功", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void fail(VolleyError volleyError) {
                    if (volleyError != null) {
                        Toast.makeText(mContext, "网络异常，地区修改失败，请稍后重试", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    // 修改签名
    public void onEventMainThread(ChangeSignEvent event) {
        if (event != null) {
            mInfo.setSignature(event.getSignature());
            tvShowSign.setText(event.getSignature());
        }
    }

    // 修改头像
    public void onEventMainThread(ChangeAvatorEvent event) {
        if (event != null) {
            mInfo.setAvatar(event.getImgPath());
            ImageUtiles.loadGlideHeadPath(ivHead, event.getImgPath());
            UpFileRequestUtils.uploadingAvator(mContext, event.getImgPath(), new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    AvaterData requestData = GsonUtils.getResult(responseInfo.result, AvaterData.class);
                    if (requestData != null) {
                        if (requestData.getCode() != 0) {
                            Toast.makeText(mContext, requestData.getDescription(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(mContext, "头像修改成功", Toast.LENGTH_SHORT).show();
                        SPUtils.setAvater(SPUtils.getPhoneNo(mContext), requestData.getData().getAvatarUrl(), mContext);
                    }

                    Log.e("message", responseInfo.result);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(mContext, "头像修改失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private UserSelfInfo mInfo;

    private void reset(UserSelfInfo info) {
        tvName.setText(info.getNickName());
        ImageUtiles.loadGlideHead(ivHead, info.getAvatar());
        if ("f".equals(info.getSex())) {
            userSexTv.setText("女");
        } else if ("m".equals(info.getSex())) {
            userSexTv.setText("男");
        } else {
            userSexTv.setText("保密");
        }
        if (!TextUtils.isEmpty(info.getRegion())) {
            userCountryTv.setText(StringUtils.decode(info.getRegion()));
        }
        userSignTv.setText("点击修改签名");
        tvShowSign.setText(info.getSignature());

    }

    private void reset() {
        tvName.setText("请先登录");
        ImageUtiles.loadGlideHeadRes(ivHead, R.drawable.default_head_icon);
        userSexTv.setText("请先登录");
        userCountryTv.setText("中国");
        userSignTv.setText("请先登录");
        outBtn.setVisibility(View.GONE);
        tvShowSign.setText("沟通无国界，随心畅聊");

    }

}
