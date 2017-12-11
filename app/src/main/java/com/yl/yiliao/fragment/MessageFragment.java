package com.yl.yiliao.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.yl.yiliao.R;
import com.yl.yiliao.activity.MainActivity;
import com.yl.yiliao.activity.SouActivity;
import com.yl.yiliao.adapter.MessageAdapter;
import com.yl.yiliao.event.ChatFinishEvent;
import com.yl.yiliao.event.LoginEvent;
import com.yl.yiliao.event.MessageRefreshEvent;
import com.yl.yiliao.helper.UserHelper;
import com.yl.yiliao.model.ChatMsgJson;
import com.yl.yiliao.model.database.MessageItem;
import com.yl.yiliao.qrcode.CaptureActivity;
import com.yl.yiliao.utils.FragmentType;
import com.yl.yiliao.utils.MyDataBaseUtils;
import com.yl.yiliao.utils.SPUtils;
import com.yl.yiliao.widget.NoLoginEmptyView2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends BaseFragment implements View.OnClickListener {
    public final int REQUEST_CODE = 1;

    private PopupWindow popupWindow;
    private View popView;

    private RelativeLayout emptyLay;
    private NoLoginEmptyView2 noLoginView;
    private ListView mLsitView;
    private List<MessageItem> items = new ArrayList<>();
    private MessageAdapter mAdapter;
    private String myPhone;

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_message, container, false);
            initView();
        } else {
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
        }
        EventBus.getDefault().register(this);
        if (UserHelper.isLogin(mContext)) {
            myPhone = SPUtils.getPhoneNo(mContext);
            initData();
        }else {
            noLoginView.setVisibility(View.VISIBLE);
            mLsitView.setVisibility(View.GONE);
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

        noLoginView = mView.findViewById(R.id.nologin);
        mLsitView = mView.findViewById(R.id.message_list);
        mAdapter = new MessageAdapter(mContext, items);
        mLsitView.setAdapter(mAdapter);
        if (UserHelper.isLogin(mContext)) {
            myPhone = SPUtils.getPhoneNo(mContext);
            initData();
        } else {
            noLoginView.setVisibility(View.VISIBLE);
            mLsitView.setVisibility(View.GONE);
            emptyLay.setVisibility(View.GONE);
        }
        getPopwindow();
    }

    private void initData() {
        // 读取本地数据库加载数据
        List<MessageItem> lists = MyDataBaseUtils.queryMessageItemList(myPhone);
        items.clear();
        if (lists != null && lists.size() > 0) {
            Collections.reverse(lists);
            items.addAll(lists);
        }
        frashLayout();

    }

    private void frashLayout() {
        if (items.size() > 0) {
            noLoginView.setVisibility(View.GONE);
            mLsitView.setVisibility(View.VISIBLE);
            emptyLay.setVisibility(View.GONE);
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        } else {
            noLoginView.setVisibility(View.GONE);
            mLsitView.setVisibility(View.GONE);
            emptyLay.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
                    ((MainActivity) mContext).requestPermissionCamera(FragmentType.MESSAGE);
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
                noLoginView.setVisibility(View.VISIBLE);
                mLsitView.setVisibility(View.GONE);
                emptyLay.setVisibility(View.GONE);
            }

        }
    }

    // 聊天界面回退 重置聊天对象手机号
    public void onEventMainThread(ChatFinishEvent event) {
        if (event != null) {
            if (mAdapter != null) {
                mAdapter.setChatPhone("");
            }

        }
    }

    // 刷新数据
    public void onEventMainThread(MessageRefreshEvent event) {
        if (event != null) {
            initData();
        }
    }

    /**
     * 主页面收到消息发送通知事件 判断是否存在当前消息类型，存在更新保存本地，不存在插入保存本地
     *
     * @param event
     */
    public void onEventMainThread(ChatMsgJson event) {
        if (event != null) {
            if (myPhone.equals(event.getFromUser())) {
                // 我发送的消息
                int isexit = isExit(event.getToUser());
                if (isexit > -1) {
                    // 表示存在需要更新数据
                    MessageItem tempItem = items.get(isexit);
                    MessageItem newItem = new MessageItem();
                    newItem.setCount(0);
                    newItem.setCreate_at(event.getTime() + "000");
                    newItem.setTxt_content(event.getFromText());
                    newItem.setSendPhone(event.getToUser());
                    newItem.setReceivePhone(myPhone);
                    newItem.setSendName(tempItem.getSendName());
                    newItem.setSendAvatar(tempItem.getSendAvatar());
                    items.remove(isexit);
                    items.add(0, newItem);
                    MyDataBaseUtils.receiveMessageItem(newItem);
                    mAdapter.notifyDataSetChanged();

                } else {
                    // 不存在直接插入数据
                    MessageItem tempItem = new MessageItem();
                    tempItem.setCount(0);
                    tempItem.setCreate_at(event.getTime() + "000");
                    tempItem.setTxt_content(event.getFromText());
                    tempItem.setSendPhone(event.getToUser());
                    tempItem.setReceivePhone(myPhone);
                    items.add(0, tempItem);
                    MyDataBaseUtils.insertMessageItem(tempItem);
                    mAdapter.notifyDataSetChanged();
                }


            } else if (myPhone.equals(event.getToUser())) {
                // 我收到的消息
                int isexit = isExit(event.getFromUser());
                if (isexit > -1) {
                    // 表示存在需要更新数据
                    MessageItem tempItem = items.get(isexit);
                    MessageItem newItem = items.get(isexit);

                    if (event.getFromUser().equals(mAdapter.getChatPhone())) {
                        newItem.setCount(0);
                    } else {
                        newItem.setCount(tempItem.getCount() + 1);
                    }
                    newItem.setCreate_at(event.getTime() + "000");
                    newItem.setTxt_content(event.getFromText());
                    newItem.setSendPhone(event.getFromUser());
                    newItem.setReceivePhone(myPhone);
                    newItem.setSendAvatar(tempItem.getSendAvatar());
                    newItem.setSendName(tempItem.getSendName());
                    items.remove(isexit);
                    items.add(0, newItem);
                    MyDataBaseUtils.receiveMessageItem(newItem);
                    frashLayout();
                } else {
                    // 不存在直接插入数据
                    MessageItem tempItem = new MessageItem();
                    tempItem.setCount(0);
                    tempItem.setCreate_at(event.getTime() + "000");
                    tempItem.setTxt_content(event.getFromText());
                    tempItem.setSendPhone(event.getFromUser());
                    tempItem.setReceivePhone(myPhone);
                    items.add(0, tempItem);
                    MyDataBaseUtils.insertMessageItem(tempItem);
                    frashLayout();
                }
            }
        }
    }

    private int isExit(String chatPhone) {
        for (int i = 0; i < items.size(); i++) {
            MessageItem tempItem = items.get(i);
            if (chatPhone.equals(tempItem.getSendPhone())) {
                return i;
            }
        }
        return -1;
    }
}
