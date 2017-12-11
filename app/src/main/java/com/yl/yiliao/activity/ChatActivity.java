package com.yl.yiliao.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.yl.yiliao.R;
import com.yl.yiliao.adapter.ChatAdapter;
import com.yl.yiliao.event.ChatFinishEvent;
import com.yl.yiliao.event.ChatItemEvent;
import com.yl.yiliao.event.ChatRefreshEvent;
import com.yl.yiliao.model.ChatMsgJson;
import com.yl.yiliao.model.LangCodeData;
import com.yl.yiliao.model.LangCodeItem;
import com.yl.yiliao.model.database.ChatItem;
import com.yl.yiliao.utils.Constants;
import com.yl.yiliao.utils.GsonUtils;
import com.yl.yiliao.utils.MyDataBaseUtils;
import com.yl.yiliao.utils.RequestUtils;
import com.yl.yiliao.utils.SPUtils;
import com.yl.yiliao.utils.StringUtils;
import com.yl.yiliao.widget.EmotionInputDetector;
import com.yl.yiliao.widget.recorder.AudioRecorderButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = ChatActivity.class.getSimpleName();

    // 对方手机号码
    private String hostPhone;
    // 对方昵称
    private String hostName;
    // 对方头像
    private String HostAvatar;
    // 自己手机号吗
    private String myPhone;

    public String fromType = "auto";
    private String fromName = "自动检测";
    private String toType = "en";
    private String toName = "英语";
    private List<String> fromNameList = new ArrayList<>();
    private List<String> fromTypeList = new ArrayList<>();
    private List<String> toNameList = new ArrayList<>();
    private List<String> toTypeList = new ArrayList<>();
    private List<String> showToNameList = new ArrayList<>();
    private List<String> showToTypeList = new ArrayList<>();
    private List<String> showfromNameList = new ArrayList<>();
    private List<String> showfromTypeList = new ArrayList<>();
    private TextView fromTv;
    private TextView toTv;
    // 软键盘操作
    private EmotionInputDetector mDetector;

    private ListView mListView;
    private ImageView mIvMicPhone;
    private RelativeLayout mVoiceLay;
    private EditText mEtInput;
    private ImageView mSenView;
    private float preFirstItem = 0;
    private AudioRecorderButton mBtnRecord;


    private ChatAdapter mAdapter;
    private List<ChatItem> items = new ArrayList<>();
    private String publishTopic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_chat);

        if (getIntent() != null) {
            hostName = getIntent().getStringExtra("nickName");
            hostPhone = getIntent().getStringExtra("phone");
            HostAvatar = getIntent().getStringExtra("avatar");
            myPhone = SPUtils.getPhoneNo(this);
            publishTopic = "/" + myPhone + "/" + hostPhone + "/message";
        }
        initView();
        requestPermission();
        requestWritePermission();
        EventBus.getDefault().register(this);

    }

    private void initView() {
        fromTv = (TextView) findViewById(R.id.from_tv);
        if (!TextUtils.isEmpty(SPUtils.getChatFromName(this, hostPhone))) {
            fromName = SPUtils.getChatFromName(this, hostPhone);
            fromType = SPUtils.getChatFromType(this, hostPhone);
            fromTv.setText(fromName);
        }
        toTv = (TextView) findViewById(R.id.to_tv);
        if (!TextUtils.isEmpty(SPUtils.getChatToName(this, hostPhone))) {
            toName = SPUtils.getChatToName(this, hostPhone);
            toType = SPUtils.getChatToType(this, hostPhone);
            toTv.setText(toName);
        }
        findViewById(R.id.from_lay).setOnClickListener(this);
        findViewById(R.id.to_lay).setOnClickListener(this);
        findViewById(R.id.change_iv).setOnClickListener(this);
        TextView titleTv = (TextView) findViewById(R.id.title);
        titleTv.setText("与 " + hostName + " 对话中");
        findViewById(R.id.icon_back).setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.listview);
        mIvMicPhone = (ImageView) findViewById(R.id.send_mic_phone);
        mVoiceLay = (RelativeLayout) findViewById(R.id.record_voice_layout);
        mEtInput = (EditText) findViewById(R.id.replay_edittext);
        mSenView = (ImageView) findViewById(R.id.btn_send);
        mAdapter = new ChatAdapter(this, items, HostAvatar, myPhone, hostPhone);
        mListView.setAdapter(mAdapter);
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                if (preFirstItem != 0 && preFirstItem > x) {
                    if (mDetector != null) {
                        mDetector.hideAll();
                        preFirstItem = 0;
                    }
                } else {
                    preFirstItem = x;
                }
                return false;
            }
        });
        initDatactor();
        initData();
        initRecordView();
        initCodeData();
    }

    private void initCodeData() {
        new RequestUtils(this, TAG, Constants.GET_LANG_CODE, new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {

                LangCodeData resultData = GsonUtils.getResult(result, LangCodeData.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        Toast.makeText(ChatActivity.this, resultData.getDescription(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (resultData.getData() != null) {
                        setList(resultData.getData());
                    }
                }
            }

            @Override
            public void fail(VolleyError volleyError) {
                if (volleyError != null) {
                    Toast.makeText(ChatActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void setList(List<LangCodeItem> items) {
        if (items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                LangCodeItem item = items.get(i);
                if (i != 0) {
                    toNameList.add(item.getName());
                    toTypeList.add(item.getCode());
                    showToNameList.add(item.getName());
                    showToTypeList.add(item.getCode());
                }
                fromNameList.add(item.getName());
                fromTypeList.add(item.getCode());
                showfromNameList.add(item.getName());
                showfromTypeList.add(item.getCode());
            }
        }

    }

    public void pushMessage(String msg) {
        ChatItem item = preChatItem(Constants.CHAT_ITEM_TYPE_SEND);
        long time = System.currentTimeMillis();
        item.setCreate_at(time + "");
        items.add(item);
        listViewNotify();
        ChatItemEvent event = new ChatItemEvent();
        event.setPublishTopic(publishTopic);
        ChatMsgJson chatMsgJson = new ChatMsgJson();
        chatMsgJson.setCatalog("text");
        chatMsgJson.setTime((time / 1000) + "");
        chatMsgJson.setFromUser(myPhone);
        chatMsgJson.setToUser(hostPhone);
        chatMsgJson.setFromLang(fromType);
        chatMsgJson.setToLang(toType);
        chatMsgJson.setFromText(msg);
        event.setChatMsgJson(chatMsgJson);
        mEtInput.setText("");
        EventBus.getDefault().post(event);
    }

    public void listViewNotify() {
//        mAdapter.updateSingleRow(mListView, items.size() - 1);
        mAdapter.notifyDataSetChanged();
        mListView.setSelection(mListView.getCount());
    }

    public void pushAudioMessage(String audioPath) {
        ChatItem item = preChatItem(Constants.CHAT_ITEM_TYPE_SEND);
        long time = System.currentTimeMillis();
        item.setCreate_at(time + "");
        items.add(item);
        listViewNotify();
        ChatItemEvent event = new ChatItemEvent();
        event.setPublishTopic(publishTopic);
        ChatMsgJson chatMsgJson = new ChatMsgJson();
        chatMsgJson.setCatalog("audio");
        chatMsgJson.setTime((time / 1000) + "");
        chatMsgJson.setFromUser(myPhone);
        chatMsgJson.setToUser(hostPhone);
        chatMsgJson.setFromLang(fromType);
        chatMsgJson.setToLang(toType);
        chatMsgJson.setFromAudio(StringUtils.fileToBase64(new File(audioPath)));
        event.setChatMsgJson(chatMsgJson);
        EventBus.getDefault().post(event);
    }

    private void initRecordView() {
        TextView mTvRecordHint = (TextView) findViewById(R.id.record_hint);
        mBtnRecord = (AudioRecorderButton) findViewById(R.id.voice_record_btn);
        mBtnRecord.setTitle(mTvRecordHint);
        mBtnRecord.setFinishRecorderCallBack(new AudioRecorderButton.AudioFinishRecorderCallBack() {
            @Override
            public void onFinish(float seconds, String filePath) {
                // seconds 录音时间长度
                long time = System.currentTimeMillis();
                if (time - mCurrentTime > 1000) {
                    mCurrentTime = time;
                    if (fromType.equals(toType)) {
                        Toast.makeText(ChatActivity.this, "语言类型不能一致，请修改后重新操作", Toast.LENGTH_SHORT).show();
                    } else {
                        pushAudioMessage(filePath);
                    }
                }
            }
        });
    }

    private long mCurrentTime;


    private void initData() {
        List<ChatItem> list = MyDataBaseUtils.queryChatDataList(hostPhone, myPhone);
        items.clear();
        if (list != null && list.size() > 0) {
            items.addAll(list);
        }
        mAdapter.notifyDataSetChanged();
        mListView.setSelection(mListView.getCount());
    }


    private void initDatactor() {
        mDetector = EmotionInputDetector.with(this)
                .bindToContent(mListView)
                .bindToEditText(mEtInput)
                // 设置录音
                .setVoiceView(mVoiceLay)
                .bindToVoiceButton(mIvMicPhone)
                .bindToOptionButton(mSenView)
                .build();
        mDetector.setSendMsgListener(new EmotionInputDetector.SendMessageListener() {
            @Override
            public void sendMessage() {
                // 发送消息
                String msg = mEtInput.getText().toString().trim();
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(ChatActivity.this, "消息不能为空吆", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (fromType.equals(toType)) {
                    Toast.makeText(ChatActivity.this, "语言类型不能一致，请修改后重新操作", Toast.LENGTH_SHORT).show();
                } else {
                    pushMessage(msg);
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icon_back:
                // 这里应该发送消息列表更新事件
                finish();
                break;
            case R.id.from_lay:
                // 初始文本语言类型选择
                showFromTypt();
                break;
            case R.id.to_lay:
                // 翻译结果语言类型选择
                showToTypt();
                break;
            case R.id.change_iv:
                // 切换语言
                if (!"auto".equals(fromType)) {
                    resetLanguage();
                } else {
                    Toast.makeText(this, "翻译类型不可设置为自动识别", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private ChatItem preChatItem(int type) {
        ChatItem item = new ChatItem();
        item.setSendPhone(hostPhone); // 数据库存储方便，同意描述为发送方为对方，接收方为自己
        item.setSourceType(type);
        item.setReceivePhone(myPhone);
        return item;
    }


    // 申请权限
    private void requestPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                    Toast.makeText(this, "语音聊天需要录音权限，否则将无法正常使用语音聊天", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                        PERMISSIONS_REQUEST_AUDIO);

            }
        }

    }

    // 申请权限
    public void requestWritePermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "语音聊天需要存储权限，否则将无法正常使用", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_WRITE);

            }
        }

    }

    @Override
    public void onBackPressed() {
        if (!mDetector.interceptBackPress()) {
            super.onBackPressed();
        }
    }

    private final int PERMISSIONS_REQUEST_AUDIO = 1;
    private final int PERMISSIONS_REQUEST_WRITE = 2;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_REQUEST_AUDIO:
                if (grantResults.length > 0 && !(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "语音聊天需要录音权限，否则将无法正常使用语音聊天", Toast.LENGTH_SHORT).show();
                    if (mDetector != null) {
                        mDetector.setShowVoice(false);
                    }
                }
                break;
            case PERMISSIONS_REQUEST_WRITE:
                if (grantResults.length > 0 && !(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "译聊需要存储权限，否则将无法正常使用", Toast.LENGTH_SHORT).show();
                    if (mDetector != null) {
                        mDetector.setShowVoice(false);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void resetLanguage() {
        String tempTypt = fromType;
        fromType = toType;
        toType = tempTypt;
        String tempName = fromName;
        fromName = toName;
        toName = tempName;
        fromTv.setText(fromName);
        toTv.setText(toName);
        SPUtils.setChatFromName(fromName, hostPhone, this);
        SPUtils.setChatFromType(fromType, hostPhone, this);
        SPUtils.setChatToName(toName, hostPhone, this);
        SPUtils.setChatToType(toType, hostPhone, this);
    }

    private void showFromTypt() {
        if (showfromNameList.size() == 0 && showfromTypeList.size() == 0) {
            Toast.makeText(this, "消息列表获取失败", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder fromBuilder = new AlertDialog.Builder(this);
        fromBuilder.setItems(showfromNameList.toArray(new String[showfromNameList.size()]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fromName = showfromNameList.get(i);
                fromType = showfromTypeList.get(i);
                fromTv.setText(fromName);
                SPUtils.setChatFromName(fromName, hostPhone, ChatActivity.this);
                SPUtils.setChatFromType(fromType, hostPhone, ChatActivity.this);
                showToNameList.clear();
                for (String name : toNameList) {
                    if (!fromName.equals(name)) {
                        showToNameList.add(name);
                    }
                }

                showToTypeList.clear();
                for (String type : toTypeList) {
                    if (!fromType.equals(type)) {
                        showToTypeList.add(type);
                    }
                }
            }
        });
        fromBuilder.show();
    }

    private void showToTypt() {
        if (showToNameList.size() == 0 && showToTypeList.size() == 0) {
            Toast.makeText(this, "消息列表获取失败", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder toBuilder = new AlertDialog.Builder(this);
        toBuilder.setItems(showToNameList.toArray(new String[showToNameList.size()]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                toName = showToNameList.get(i);
                toType = showToTypeList.get(i);
                toTv.setText(toName);
                SPUtils.setChatToName(toName, hostPhone, ChatActivity.this);
                SPUtils.setChatToType(toType, hostPhone, ChatActivity.this);
                showfromNameList.clear();
                for (String name : fromNameList) {
                    if (!toName.equals(name)) {
                        showfromNameList.add(name);
                    }
                }
                showfromTypeList.clear();
                for (String type : fromTypeList) {
                    if (!toType.equals(type)) {
                        showfromTypeList.add(type);
                    }
                }
            }
        });
        toBuilder.show();
    }

//    /**
//     * 主页面收到消息后发送的事件，首先应该判断是否是本页面消息，否 不处理
//     * 是 首先更新列表展示，存入本地
//     *
//     * @param event
//     */
//    public void onEventMainThread(ChatMsgJson event) {
//        if (event != null) {
//            if (myPhone.equals(event.getToUser()) && hostPhone.equals(event.getFromUser())) {
//                // 对方发送给我的
//                ChatItem item = preChatItem(Constants.CHAT_ITEM_TYPE_RECEIVE);
//                item.setCreate_at(event.getTime() + "000");// 时间需要放大1000倍 为毫秒值
//                item.setFromContent(event.getFromText());
//                item.setToContent(event.getToText());
//                item.setToVoice(event.getToAudioUrl());
//                MyDataBaseUtils.insertChatItem(item);
//                items.add(item);
//                listViewNotify();
//
//            } else if (myPhone.equals(event.getFromUser()) && hostPhone.equals(event.getToUser())) {
//                // 我发送给对方的
//                for (int i = 0; i < items.size(); i++) {
//                    ChatItem tempItem = items.get(i);
//                    if (tempItem.getSourceType() == Constants.CHAT_ITEM_TYPE_RECEIVE) {
//                        continue;
//                    }
//
//                    if (!TextUtils.isEmpty(tempItem.getFromContent())) {
//                        continue;
//                    }
//
//                    if (tempItem.getCreate_at().startsWith(event.getTime())) {
//                        tempItem.setFromContent(event.getFromText());
//                        tempItem.setToContent(event.getToText());
//                        tempItem.setToVoice(event.getToAudioUrl());
//                        MyDataBaseUtils.insertChatItem(tempItem);
//                        listViewNotify();
//                        break;
//                    }
//
//                }
//            }
//
//        }
//    }

    // 数据刷新
    public void onEventMainThread(ChatRefreshEvent event) {
        if (event != null) {
            initData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new ChatFinishEvent());

        EventBus.getDefault().unregister(this);

    }
}
