package com.yl.yiliao.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.yl.yiliao.R;
import com.yl.yiliao.activity.MainActivity;
import com.yl.yiliao.activity.SouActivity;
import com.yl.yiliao.adapter.FriendsAdapter;
import com.yl.yiliao.event.LoginEvent;
import com.yl.yiliao.event.RefreshFriendEvent;
import com.yl.yiliao.helper.UserHelper;
import com.yl.yiliao.model.FriendData;
import com.yl.yiliao.model.FriendsData;
import com.yl.yiliao.model.FriendsListData;
import com.yl.yiliao.qrcode.CaptureActivity;
import com.yl.yiliao.utils.Constants;
import com.yl.yiliao.utils.FragmentType;
import com.yl.yiliao.utils.GsonUtils;
import com.yl.yiliao.utils.RequestUtils;
import com.yl.yiliao.widget.NoLoginEmptyView2;
import com.yl.yiliao.widget.SideBar;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends BaseFragment implements View.OnClickListener {
    public final int REQUEST_CODE = 1;
    private final String TAG = FriendsFragment.class.getSimpleName();
    // 保留做原始数据，用于搜索使用
    private List<FriendData> initItems = new ArrayList<>();
    // 用来展示数据
    private List<FriendData> showItems = new ArrayList<>();

    private PopupWindow popupWindow;
    private View popView;
    private RelativeLayout emptyLay;
    private RelativeLayout friendLay;
    private NoLoginEmptyView2 noLoginView;
    private ListView mLsitView;
    private FriendsAdapter mAdapter;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_friends, container, false);
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
            noLoginView.setVisibility(View.VISIBLE);
            friendLay.setVisibility(View.GONE);
            emptyLay.setVisibility(View.GONE);
        }
        return mView;
    }

    private View mView;

    private void initView() {
        mView.findViewById(R.id.add_icon).setOnClickListener(this);
        popView = LayoutInflater.from(mContext).inflate(R.layout.message_menu_pop, null);
        popView.findViewById(R.id.lay_sao).setOnClickListener(this);
        popView.findViewById(R.id.lay_add_friends).setOnClickListener(this);
        emptyLay = mView.findViewById(R.id.empty_lay);
        friendLay = mView.findViewById(R.id.friendLay);
        noLoginView = mView.findViewById(R.id.nologin);
        mLsitView = mView.findViewById(R.id.listview);
        View headView = LayoutInflater.from(mContext).inflate(R.layout.friend_list_head, null);
        EditText editText = headView.findViewById(R.id.friend_edit);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence)) {
                    showItems.clear();
                    showItems.addAll(initItems);
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    filledData(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mLsitView.addHeaderView(headView);

        mAdapter = new FriendsAdapter(mContext, showItems);
        mLsitView.setAdapter(mAdapter);

        SideBar mSideBar = mView.findViewById(R.id.sidrbar);
        TextView mTvDialog = mView.findViewById(R.id.dialog);
        mSideBar.setTextView(mTvDialog);
        // 设置右侧触摸监听
        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mLsitView.setSelection(position);
                }

            }
        });

        getPopwindow();

    }

    private void getPopwindow() {
        popupWindow = new PopupWindow(popView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        popupWindow.setFocusable(true);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_icon:
                if (popupWindow != null) {
                    popupWindow.showAsDropDown(view, 0, 0, Gravity.RIGHT);
                }
                break;
            case R.id.lay_sao:
                if (UserHelper.autoLogin(mContext)) {
                    ((MainActivity) mContext).requestPermissionCamera(FragmentType.FRIENDS);
                }
                break;
            case R.id.lay_add_friends:
                if (UserHelper.autoLogin(mContext)) {
                    mContext.startActivity(new Intent(mContext, SouActivity.class));
                }
                break;
        }
    }

    public void startSao() {
        if (UserHelper.autoLogin(mContext)) {
            Intent intent = new Intent(getActivity(), CaptureActivity.class);
            getActivity().startActivityForResult(intent, REQUEST_CODE);
        }
        popupWindow.dismiss();
    }


    private void initData() {
        new RequestUtils(mContext, TAG, Constants.GET_FRIEND_LIST, new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {
                FriendsListData resultData = GsonUtils.getResult(result, FriendsListData.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        Toast.makeText(mContext, resultData.getDescription(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (resultData.getData() != null) {
                        initFilledData(resultData.getData());
                    } else {
                        noLoginView.setVisibility(View.GONE);
                        emptyLay.setVisibility(View.VISIBLE);
                        friendLay.setVisibility(View.GONE);
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

    // 根据搜索筛选数据
    private void filledData(String str) {
        showItems.clear();
        if (initItems != null && initItems.size() > 0) {
            for (FriendData data : initItems) {
                String name = data.getNickName();
                if (name.contains(str)) {
                    showItems.add(data);
                }
            }
        }

        mAdapter.notifyDataSetChanged();

    }

    // 筛选数据
    private void initFilledData(FriendsListData listData) {
        initItems.clear();
        if (listData.getFriends() != null && listData.getFriends().size() > 0) {
            for (FriendsData friendsData : listData.getFriends()) {
                if (friendsData.getGroupFriends() != null && friendsData.getGroupFriends().size() > 0) {
                    for (FriendData friendData : friendsData.getGroupFriends()) {
                        friendData.setGroupTitle(friendsData.getGroupTitle());
                        initItems.add(friendData);
                    }
                }

            }
        }
        showItems.clear();
        showItems.addAll(initItems);
        if (showItems.size() > 0) {
            noLoginView.setVisibility(View.GONE);
            emptyLay.setVisibility(View.GONE);
            friendLay.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        } else {
            noLoginView.setVisibility(View.GONE);
            emptyLay.setVisibility(View.VISIBLE);
            friendLay.setVisibility(View.GONE);
        }
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
                noLoginView.setVisibility(View.GONE);
                initData();
            } else {
                noLoginView.setVisibility(View.VISIBLE);
                friendLay.setVisibility(View.GONE);
                emptyLay.setVisibility(View.GONE);
            }
        }
    }

    // 是否登陆
    public void onEventMainThread(RefreshFriendEvent event) {
        if (event != null) {
            if (UserHelper.isLogin(mContext)) {
                initData();
            }
        }
    }
}
