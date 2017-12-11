package com.yl.yiliao.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.yl.yiliao.R;
import com.yl.yiliao.event.AddSubScribeEvent;
import com.yl.yiliao.event.ChangeAvatorEvent;
import com.yl.yiliao.event.ChatItemEvent;
import com.yl.yiliao.event.ChatRefreshEvent;
import com.yl.yiliao.event.LoginEvent;
import com.yl.yiliao.event.MessageRefreshEvent;
import com.yl.yiliao.event.NotSupportAmr;
import com.yl.yiliao.fragment.FriendsFragment;
import com.yl.yiliao.fragment.MessageFragment;
import com.yl.yiliao.fragment.MineFragment;
import com.yl.yiliao.fragment.TranslateFragment;
import com.yl.yiliao.helper.UserHelper;
import com.yl.yiliao.model.ChatMsgJson;
import com.yl.yiliao.model.FriendData;
import com.yl.yiliao.model.FriendsData;
import com.yl.yiliao.model.FriendsListData;
import com.yl.yiliao.model.database.ChatItem;
import com.yl.yiliao.model.database.MessageItem;
import com.yl.yiliao.qrcode.CodeUtils;
import com.yl.yiliao.qrcode.Constant;
import com.yl.yiliao.utils.Constants;
import com.yl.yiliao.utils.FragmentType;
import com.yl.yiliao.utils.GsonUtils;
import com.yl.yiliao.utils.MyDataBaseUtils;
import com.yl.yiliao.utils.RequestUtils;
import com.yl.yiliao.utils.SPUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import de.greenrobot.event.EventBus;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = MainActivity.class.getSimpleName();
    public final int REQUEST_CODE = 1;

    private ImageView mTranslateIv;
    private TextView mTranslateTv;
    private ImageView mMessageIv;
    private TextView mMessageTv;
    private ImageView mFriendsIv;
    private TextView mFriendsTv;
    private ImageView mMineIv;
    private TextView mMineTv;
    private int mBgColor;
    private int mBgSelectVolor;
    private TranslateFragment mTranslateFragment;
    private MessageFragment mMessageFragment;
    private FriendsFragment mFriendsFragment;
    private MineFragment mMineFragment;
    private int fragmentType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        mTranslateIv = (ImageView) findViewById(R.id.main_home_img);
        mTranslateTv = (TextView) findViewById(R.id.main_home_tv);
        mMessageIv = (ImageView) findViewById(R.id.main_message_img);
        mMessageTv = (TextView) findViewById(R.id.main_message_tv);
        mFriendsIv = (ImageView) findViewById(R.id.main_friends_img);
        mFriendsTv = (TextView) findViewById(R.id.main_friends_tv);
        mMineIv = (ImageView) findViewById(R.id.main_mian_img);
        mMineTv = (TextView) findViewById(R.id.main_mian_tv);
        findViewById(R.id.main_home_layout).setOnClickListener(this);
        findViewById(R.id.main_message_layout).setOnClickListener(this);
        findViewById(R.id.main_friends_layout).setOnClickListener(this);
        findViewById(R.id.main_me_layout).setOnClickListener(this);
        mBgColor = getResources().getColor(R.color.main_tab_bg);
        mBgSelectVolor = getResources().getColor(R.color.main_tab_select_bg);
        if (mTranslateFragment == null) {
            mTranslateFragment = new TranslateFragment();
        }
        if (!mTranslateFragment.isAdded()) {
            fragmentType = Constants.FRAGMENT_MAIN;
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_layout, mTranslateFragment).commit();
        }

        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_LOGS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.SET_DEBUG_APP,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 100);
        }
        initIM();
        EventBus.getDefault().register(this);
        initPermission();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_home_layout:
                setTabBg(0);
                if (mTranslateFragment == null) {
                    mTranslateFragment = new TranslateFragment();
                }
                if (!mTranslateFragment.isAdded()) {
                    fragmentType = Constants.FRAGMENT_MAIN;
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_layout, mTranslateFragment).commit();
                }
                break;
            case R.id.main_message_layout:
                setTabBg(1);
                if (mMessageFragment == null) {
                    mMessageFragment = new MessageFragment();
                }
                if (!mMessageFragment.isAdded()) {
                    fragmentType = Constants.FRAGMENT_MSG;
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_layout, mMessageFragment).commit();
                }
                break;
            case R.id.main_friends_layout:
                setTabBg(2);
                if (mFriendsFragment == null) {
                    mFriendsFragment = new FriendsFragment();
                }
                if (!mFriendsFragment.isAdded()) {
                    fragmentType = Constants.FRAGMENT_FRID;
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_layout, mFriendsFragment).commit();
                }
                break;
            case R.id.main_me_layout:
                setTabBg(3);
                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                }
                if (!mMineFragment.isAdded()) {
                    fragmentType = Constants.FRAGMENT_MINE;
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_layout, mMineFragment).commit();
                }
                break;
            default:
                break;
        }
    }

    private void setTabBg(int tabIndex) {
        mTranslateIv.setImageResource(tabIndex == 0 ? R.drawable.ic_translate_sel : R.drawable.ic_translate);
        mTranslateTv.setTextColor(tabIndex == 0 ? mBgSelectVolor : mBgColor);
        mMessageIv.setImageResource(tabIndex == 1 ? R.drawable.ic_message_sel : R.drawable.ic_message);
        mMessageTv.setTextColor(tabIndex == 1 ? mBgSelectVolor : mBgColor);
        mFriendsIv.setImageResource(tabIndex == 2 ? R.drawable.ic_friends_sel : R.drawable.ic_friends);
        mFriendsTv.setTextColor(tabIndex == 2 ? mBgSelectVolor : mBgColor);
        mMineIv.setImageResource(tabIndex == 3 ? R.drawable.ic_mine_sel : R.drawable.ic_mine);
        mMineTv.setTextColor(tabIndex == 3 ? mBgSelectVolor : mBgColor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTranslateFragment != null) {
            mTranslateFragment = null;
        }
        if (mMessageFragment != null) {
            mMessageFragment = null;
        }
        if (mFriendsFragment != null) {
            mFriendsFragment = null;
        }
        if (mMineFragment != null) {
            mMineFragment = null;
        }
        EventBus.getDefault().unregister(this);

    }

    private final int PERMISSIONS_REQUEST_AUDIO = 1;
    private final int PERMISSIONS_REQUEST_WRITE = 2;
    private final int PERMISSIONS_REQUEST_CAMERA = 3;


    public void initPermission() {
        requestWritePermission();
        requestPermission();
    }

    // 申请权限
    public void requestPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                    Toast.makeText(this, "语音翻译需要录音权限，否则将无法正常使用语音翻译", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "译聊需要存储权限，否则将无法正常使用", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_WRITE);

            }
        }

    }


    public boolean isAllowRecord() {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    // 二维码扫描权限
    public void requestPermissionCamera(FragmentType type) {
        mType = type;
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "扫一扫需要摄像头权限，否则将无法正常使用", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        PERMISSIONS_REQUEST_CAMERA);

            } else {
                // 表示已经拥有权限
                startSao(type);
            }
        } else {
            startSao(type);
        }
    }

    private FragmentType mType;

    private void startSao(FragmentType type) {
        if (type == FragmentType.MESSAGE) {
            if (mMessageFragment != null) {
                mMessageFragment.startSao();
            }
        } else if (type == FragmentType.FRIENDS) {
            if (mFriendsFragment != null) {
                mFriendsFragment.startSao();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_REQUEST_AUDIO:
                if (grantResults.length > 0 && !(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "语音翻译需要录音权限，否则将无法正常使用语音翻译", Toast.LENGTH_SHORT).show();
                }
                break;
            case PERMISSIONS_REQUEST_WRITE:
                if (grantResults.length > 0 && !(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "译聊需要存储权限，否则将无法正常使用", Toast.LENGTH_SHORT).show();
                }
                break;
            case PERMISSIONS_REQUEST_CAMERA:
                if (!(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "扫一扫需要摄像头权限，否则将无法正常使用扫一扫功能", Toast.LENGTH_SHORT).show();
                } else {
                    startSao(mType);
                }
                break;
            case PERMISSIONS_REQUEST_ALBUM:
                if (grantResults.length > 0 && !(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "查看本地相册需要本地数据读取权限，否则将无法正常上传", Toast.LENGTH_SHORT).show();
                } else {
                    startAlbum();
                }
                break;
            default:
                break;
        }
    }

    public void requestPermissionAlbum() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "查看本地相册需要本地数据读取权限，否则将无法正常上传", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_ALBUM);

            } else {
                // 表示已经拥有权限
                startAlbum();
            }
        } else {
            startAlbum();
        }

    }

    private void startAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            startActivityForResult(intent, Constant.REQUEST_IMAGE);
        } catch (Exception e) {
            Toast.makeText(this, "很抱歉，当前您的手机不支持相册选择功能，请安装相册软件", Toast.LENGTH_SHORT).show();
        }
    }

    private final int PERMISSIONS_REQUEST_ALBUM = 10;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.e(TAG, "解析结果" + result);
                    // 二维码结果也太简单粗暴了吧，直接就是手机号，不做校验的话很容易就崩掉了，目前校验，是否为纯数字
                    if (TextUtils.isEmpty(result)) {
                        Toast.makeText(MainActivity.this, "二维码不识别", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    boolean code = result.matches("[0-9]+");
                    if (!code) {
                        Toast.makeText(MainActivity.this, "二维码不识别", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (result.equals(SPUtils.getPhoneNo(MainActivity.this))) {
                        Toast.makeText(MainActivity.this, "当前用户二维码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(MainActivity.this, SaoResultActivity.class);
                    intent.putExtra("phoneno", result);
                    startActivity(intent);

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "扫描二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == Constant.REQUEST_IMAGE && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(MainActivity.this, "抱歉，选择失败,换个图片试试.", Toast.LENGTH_SHORT).show();
                return;
            }

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imagePath = cursor.getString(columnIndex);
                EventBus.getDefault().post(new ChangeAvatorEvent(imagePath));
                cursor.close();
            }
        }
    }


    // 发送消息
    public void onEventMainThread(ChatItemEvent event) {
        if (event != null) {
            Log.e(TAG, "Message want Published: " + event.getChatMsgJson().toString());
            try {
                MqttMessage message = new MqttMessage();
                String msgJson = new Gson().toJson(event.getChatMsgJson());
                message.setPayload(msgJson.getBytes());
                if (mqttAndroidClient != null) {
                    mqttAndroidClient.publish(event.getPublishTopic(), message);
                    Log.e(TAG, "Message Published: topic:" + event.getPublishTopic() + " msg:" + msgJson);
                }
            } catch (MqttException e) {
                Log.e(TAG, "Error Publishing: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private MqttAndroidClient mqttAndroidClient;
    private String myPhone;

    private void initIM() {
        if (UserHelper.isLogin(this)) {
            myPhone = SPUtils.getPhoneNo(this);
            String userName = SPUtils.getPhoneNo(this);
            String password = SPUtils.getToken(this);
            String clientId = SPUtils.getPhoneNo(this) + "10515223262656";
            mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), Constants.BASE_SERVICE, clientId);
            mqttAndroidClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {

                    if (reconnect) {
                        Log.e(TAG, "Reconnected to : " + serverURI);
                        // Because Clean Session is true, we need to re-subscribe
                        getFriends();
                    } else {
                        Log.e(TAG, "Connected to: " + serverURI);
                    }
                }

                @Override
                public void connectionLost(Throwable cause) {
                    Log.e(TAG, "The Connection was lost.");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String content = new String(message.getPayload());
                    Log.e("Message Receive:", "topic:" + topic + " content:" + content);
                    ChatMsgJson resultData = GsonUtils.getResult(content, ChatMsgJson.class);
                    if (resultData != null) {
                        // 聊天消息
                        if ("audio".equals(resultData.getCatalog()) || "text".equals(resultData.getCatalog())) {
                            if (fragmentType == Constants.FRAGMENT_MSG) {
                                EventBus.getDefault().post(resultData);
                                // 聊天纪录信息直接存入本地然后通知更新
                                saveChatMsg(resultData);
                                EventBus.getDefault().post(new ChatRefreshEvent());
                            } else {
                                saveMessage(resultData);
                                EventBus.getDefault().post(new MessageRefreshEvent());
                                saveChatMsg(resultData);
                                EventBus.getDefault().post(new ChatRefreshEvent());
                            }
                        } else if ("AddFriendRequest".equals(resultData.getCatalog())) {
                            // 添加好友消息
                            subscribeToTopic("/" + resultData.getFromPhoneNo() + "/" + myPhone + "/message");
                            subscribeToTopic("/" + myPhone + "/" + resultData.getFromPhoneNo() + "/message");
                        }
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setAutomaticReconnect(true);
            mqttConnectOptions.setCleanSession(false);
            mqttConnectOptions.setUserName(userName);
            mqttConnectOptions.setPassword(password.toCharArray());
            try {
                mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.e(TAG, "mqttAndroidClient connect Success");
                        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                        disconnectedBufferOptions.setBufferEnabled(true);
                        disconnectedBufferOptions.setBufferSize(100);
                        disconnectedBufferOptions.setPersistBuffer(false);
                        disconnectedBufferOptions.setDeleteOldestMessages(false);
                        mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                        getFriends();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.e(TAG, "mqttAndroidClient connect failure:" + exception.getMessage());
                        exception.printStackTrace();
                    }
                });
            } catch (MqttException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void saveChatMsg(ChatMsgJson resultData) {
        ChatItem item = new ChatItem();
        if (resultData.getFromUser().equals(myPhone)) {
            // 我是发送方
            item.setSourceType(Constants.CHAT_ITEM_TYPE_SEND);
            item.setSendPhone(resultData.getToUser()); // 数据库存储方便，同意描述为发送方为对方，接收方为自己
        } else if (resultData.getToUser().equals(myPhone)) {
            // 我是接收方
            item.setSourceType(Constants.CHAT_ITEM_TYPE_RECEIVE);
            item.setSendPhone(resultData.getFromUser()); // 数据库存储方便，同意描述为发送方为对方，接收方为自己
        }
        item.setReceivePhone(myPhone);
        item.setCreate_at(resultData.getTime() + "000");// 时间需要放大1000倍 为毫秒值
        item.setFromContent(resultData.getFromText());
        item.setToContent(resultData.getToText());
        item.setToVoice(resultData.getToAudioUrl());
        MyDataBaseUtils.insertChatItem(item);
    }

    private void saveMessage(ChatMsgJson resultData) {
        MessageItem newItem = new MessageItem();
        newItem.setCount(0);
        newItem.setCreate_at(resultData.getTime() + "000");
        newItem.setTxt_content(resultData.getFromText());
        newItem.setReceivePhone(myPhone);
        if (resultData.getFromUser().equals(myPhone)) {
            // 我是发送方
            newItem.setSendPhone(resultData.getToUser());
            MyDataBaseUtils.receiveMessageItem(newItem);
        } else if (resultData.getToUser().equals(myPhone)) {
            // 我是接收方
            newItem.setSendPhone(resultData.getFromUser());
            MyDataBaseUtils.saveMessageItem(newItem);
        }
    }

    private void getFriends() {
        new RequestUtils(this, TAG, Constants.GET_FRIEND_LIST, new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {
                FriendsListData resultData = GsonUtils.getResult(result, FriendsListData.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        return;
                    }
                    if (resultData.getData() != null) {
                        initFilledData(resultData.getData());
                    }
                }
            }

            @Override
            public void fail(VolleyError volleyError) {

            }
        });
    }

    // 筛选数据
    private void initFilledData(FriendsListData listData) {
        if (listData.getFriends() != null && listData.getFriends().size() > 0) {
            for (FriendsData friendsData : listData.getFriends()) {
                if (friendsData.getGroupFriends() != null && friendsData.getGroupFriends().size() > 0) {
                    for (FriendData friendData : friendsData.getGroupFriends()) {
                        // 在这里进行订阅 "/181/123/mesage";//181对方
                        subscribeToTopic("/" + friendData.getPhoneNo() + "/" + SPUtils.getPhoneNo(this) + "/message");
                        subscribeToTopic("/" + SPUtils.getPhoneNo(this) + "/" + friendData.getPhoneNo() + "/message");
                    }
                }

            }
        }

        subscribeToTopic("/sys/" + SPUtils.getPhoneNo(this) + "/message");

    }

    //订阅消息
    public void subscribeToTopic(final String subscriptionTopic) {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.e(TAG, "Success to Subscribed! :" + subscriptionTopic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "Failed to subscribe");
                }
            });
        } catch (MqttException ex) {
            Log.e(TAG, "Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    // 有新的好友的时候添加订阅
    public void onEventMainThread(AddSubScribeEvent event) {
        if (event != null) {
            subscribeToTopic("/" + event.getHostPhone() + "/" + SPUtils.getPhoneNo(this) + "/message");
            subscribeToTopic("/" + SPUtils.getPhoneNo(this) + "/" + event.getHostPhone() + "/message");

        }
    }


    // 不支持amr录音
    public void onEventMainThread(NotSupportAmr event) {
        if (event != null) {
            Toast.makeText(this, "没有录音权限或音频格式不支持，请退出重试以便于参数调整", Toast.LENGTH_SHORT).show();
        }
    }

    // 是否登陆
    public void onEventMainThread(LoginEvent event) {
        if (event != null) {
            if (event.isSueecss()) {
                // 登陆成功 获取个人信息
                initIM();
            } else {
                if (mqttAndroidClient != null) {
                    mqttAndroidClient.close();
                }
            }
        }
    }

}
