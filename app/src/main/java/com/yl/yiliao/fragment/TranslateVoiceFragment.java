package com.yl.yiliao.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.yl.yiliao.R;
import com.yl.yiliao.activity.MainActivity;
import com.yl.yiliao.model.LangCodeData;
import com.yl.yiliao.model.LangCodeItem;
import com.yl.yiliao.model.TranslateInfo;
import com.yl.yiliao.utils.Constants;
import com.yl.yiliao.utils.GsonUtils;
import com.yl.yiliao.utils.PlayVoiceUtils;
import com.yl.yiliao.utils.RequestUtils;
import com.yl.yiliao.utils.SPUtils;
import com.yl.yiliao.utils.StringUtils;
import com.yl.yiliao.widget.audio.AudioRecordManager;
import com.yl.yiliao.widget.audio.FileUtils;
import com.yl.yiliao.widget.audio.IAudioRecordListener;
import com.yl.yiliao.widget.recorder.AudioRecorderButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class TranslateVoiceFragment extends BaseFragment implements View.OnClickListener {

    private TextView fromTv;
    private TextView toTv;
    private ImageView fromeIv;
    private ImageView toIv;
    private EditText editText;
    private TextView showTv;
    private ImageView executeIv;
    private ImageView deleteIv;
    private View moreLay;
    private ImageView copyIv;
    private ImageView playIv;
    private View recordBg;
    private String voicePath;
    private String fromType = "zh";
    private String fromName = "中文";
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
    private Button audioBtn;
    private LinearLayout mLlRoot;

    public TranslateVoiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_translate_voice, container, false);
            initView();
        } else {
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
        }
        return mView;
    }

    private void initView() {
        fromTv = mView.findViewById(R.id.from_tv);
        if (!TextUtils.isEmpty(SPUtils.getVoiceFromName(mContext))) {
            fromName = SPUtils.getVoiceFromName(mContext);
            fromType = SPUtils.getVoiceFromType(mContext);
            fromTv.setText(fromName);
        }
        toTv = mView.findViewById(R.id.to_tv);
        if (!TextUtils.isEmpty(SPUtils.getVoiceToName(mContext))) {
            toName = SPUtils.getVoiceToName(mContext);
            toType = SPUtils.getVoiceToType(mContext);
            toTv.setText(toName);
        }
        fromeIv = mView.findViewById(R.id.frome_iv);
        toIv = mView.findViewById(R.id.to_iv);
        editText = mView.findViewById(R.id.input_edit);
        showTv = mView.findViewById(R.id.result_tv);
        executeIv = mView.findViewById(R.id.excute_iv);
        executeIv.setOnClickListener(this);
        deleteIv = mView.findViewById(R.id.delete_iv);
        deleteIv.setOnClickListener(this);
        moreLay = mView.findViewById(R.id.more_lay);
        moreLay.setVisibility(View.GONE);
        copyIv = mView.findViewById(R.id.copy_iv);
        copyIv.setOnClickListener(this);
        playIv = mView.findViewById(R.id.play_iv);
        playIv.setOnClickListener(this);
        mView.findViewById(R.id.from_lay).setOnClickListener(this);
        mView.findViewById(R.id.to_lay).setOnClickListener(this);
        recordBg = mView.findViewById(R.id.record_bg);
        editText.setInputType(InputType.TYPE_NULL);
        //editText.setInputType(InputType.TYPE_CLASS_TEXT); 用来开启输入框输入功能
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence)) {
                    deleteIv.setVisibility(View.GONE);
                    executeIv.setImageResource(R.drawable.ic_excute_off);
                    executeIv.setClickable(false);
                } else {
                    deleteIv.setVisibility(View.VISIBLE);
                    executeIv.setImageResource(R.drawable.to_do_bg);
                    executeIv.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ((MainActivity) mContext).requestPermission();
        ((MainActivity) mContext).requestWritePermission();

        mBtnRecord = mView.findViewById(R.id.record);
        mBtnRecord.setFinishRecorderCallBack(new AudioRecorderButton.AudioFinishRecorderCallBack() {
            @Override
            public void onFinish(float seconds, String filePath) {
                long time = System.currentTimeMillis();
                if (time - mCurrentTime > 1000) {
                    mCurrentTime = time;
                    if (fromType.equals(toType)) {
                        Toast.makeText(mContext, "语言类型不能一致，请修改后重新操作", Toast.LENGTH_SHORT).show();
                    } else {
                        translateVoice(filePath);
                    }

                }
            }
        });
        mView.findViewById(R.id.change_iv).setOnClickListener(this);
        audioBtn = mView.findViewById(R.id.record_btn);
        mLlRoot = mView.findViewById(R.id.llRoot);
        initAudioRecordManager();
        audioBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        AudioRecordManager.getInstance(mContext).startRecord();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isCancelled(view, motionEvent)) {
                            AudioRecordManager.getInstance(mContext).willCancelRecord();
                        } else {
                            AudioRecordManager.getInstance(mContext).continueRecord();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        AudioRecordManager.getInstance(mContext).stopRecord();
                        AudioRecordManager.getInstance(mContext).destroyRecord();
                        break;
                }
                return false;
            }
        });
        initData();
    }

    private boolean isCancelled(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        if (event.getRawX() < location[0] || event.getRawX() > location[0] + view.getWidth()
                || event.getRawY() < location[1] - 40) {
            return true;
        }

        return false;
    }

    private void initAudioRecordManager() {
        AudioRecordManager.getInstance(mContext).setMaxVoiceDuration(60);
        File audioDir = new File(FileUtils.getDir("audio"));
        if (!audioDir.exists()) {
            audioDir.mkdirs();
        }
        AudioRecordManager.getInstance(mContext).setAudioSavePath(audioDir.getAbsolutePath());
        AudioRecordManager.getInstance(mContext).setAudioRecordListener(new IAudioRecordListener() {

            private TextView mTimerTV;
            private TextView mStateTV;
            private ImageView mStateIV;
            private PopupWindow mRecordWindow;

            @Override
            public void initTipView() {
                View view = View.inflate(mContext, R.layout.popup_audio_wi_vo, null);
                mStateIV = view.findViewById(R.id.rc_audio_state_image);
                mStateTV = view.findViewById(R.id.rc_audio_state_text);
                mTimerTV = view.findViewById(R.id.rc_audio_timer);
                mRecordWindow = new PopupWindow(view, -1, -1);
                mRecordWindow.showAtLocation(mLlRoot, 17, 0, 0);
                mRecordWindow.setFocusable(true);
                mRecordWindow.setOutsideTouchable(false);
                mRecordWindow.setTouchable(false);
            }

            @Override
            public void setTimeoutTipView(int counter) {
                if (this.mRecordWindow != null) {
                    this.mStateIV.setVisibility(View.GONE);
                    this.mStateTV.setVisibility(View.VISIBLE);
                    this.mStateTV.setText(R.string.voice_rec);
                    this.mStateTV.setBackgroundResource(R.drawable.bg_voice_popup);
                    this.mTimerTV.setText(String.format("%s", new Object[]{Integer.valueOf(counter)}));
                    this.mTimerTV.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void setRecordingTipView() {
                if (this.mRecordWindow != null) {
                    this.mStateIV.setVisibility(View.VISIBLE);
                    this.mStateIV.setImageResource(R.drawable.ic_volume_1);
                    this.mStateTV.setVisibility(View.VISIBLE);
                    this.mStateTV.setText(R.string.voice_rec);
                    this.mStateTV.setBackgroundResource(R.drawable.bg_voice_popup);
                    this.mTimerTV.setVisibility(View.GONE);
                }
            }

            @Override
            public void setAudioShortTipView() {
                if (this.mRecordWindow != null) {
                    mStateIV.setImageResource(R.drawable.ic_volume_wraning);
                    mStateTV.setText(R.string.voice_short);
                }
            }

            @Override
            public void setCancelTipView() {
                if (this.mRecordWindow != null) {
                    this.mTimerTV.setVisibility(View.GONE);
                    this.mStateIV.setVisibility(View.VISIBLE);
                    this.mStateIV.setImageResource(R.drawable.ic_volume_cancel);
                    this.mStateTV.setVisibility(View.VISIBLE);
                    this.mStateTV.setText(R.string.voice_cancel);
                    this.mStateTV.setBackgroundResource(R.drawable.corner_voice_style);
                }
            }

            @Override
            public void destroyTipView() {
                if (this.mRecordWindow != null) {
                    this.mRecordWindow.dismiss();
                    this.mRecordWindow = null;
                    this.mStateIV = null;
                    this.mStateTV = null;
                    this.mTimerTV = null;
                }
            }

            @Override
            public void onStartRecord() {

            }

            @Override
            public void onFinish(Uri audioPath, int duration) {
                //发送文件
                File file = new File(audioPath.getPath());
                if (file.exists()) {
                    Log.e("message", "file:" + audioPath.getPath());
                    long time = System.currentTimeMillis();
                    if (time - mCurrentTime > 1000) {
                        mCurrentTime = time;
                        if (fromType.equals(toType)) {
                            Toast.makeText(mContext, "语言类型不能一致，请修改后重新操作", Toast.LENGTH_SHORT).show();
                        } else {
                            translateVoice(audioPath.getPath());
                        }

                    }
                }
            }

            @Override
            public void onAudioDBChanged(int db) {
                switch (db / 5) {
                    case 0:
                        this.mStateIV.setImageResource(R.drawable.ic_volume_1);
                        break;
                    case 1:
                        this.mStateIV.setImageResource(R.drawable.ic_volume_2);
                        break;
                    case 2:
                        this.mStateIV.setImageResource(R.drawable.ic_volume_3);
                        break;
                    case 3:
                        this.mStateIV.setImageResource(R.drawable.ic_volume_4);
                        break;
                    case 4:
                        this.mStateIV.setImageResource(R.drawable.ic_volume_5);
                        break;
                    case 5:
                        this.mStateIV.setImageResource(R.drawable.ic_volume_6);
                        break;
                    case 6:
                        this.mStateIV.setImageResource(R.drawable.ic_volume_7);
                        break;
                    default:
                        this.mStateIV.setImageResource(R.drawable.ic_volume_8);
                }
            }
        });
    }

    private long mCurrentTime;

    private void initData() {
        new RequestUtils(mContext, TAG, Constants.GET_LANG_CODE_VOICE, new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {

                LangCodeData resultData = GsonUtils.getResult(result, LangCodeData.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        Toast.makeText(mContext, resultData.getDescription(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void setList(List<LangCodeItem> items) {
        if (items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                LangCodeItem item = items.get(i);
                toNameList.add(item.getName());
                toTypeList.add(item.getCode());
                fromNameList.add(item.getName());
                fromTypeList.add(item.getCode());
                showToNameList.add(item.getName());
                showToTypeList.add(item.getCode());
                showfromNameList.add(item.getName());
                showfromTypeList.add(item.getCode());
            }
        }

    }

    private AudioRecorderButton mBtnRecord;

    private boolean isHaveRecordPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (mContext.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                    && mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
            return true;
        }
        return true;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.from_lay:
                // 初始文本语言类型选择
                showFromTypt();
                break;
            case R.id.to_lay:
                // 翻译结果语言类型选择
                showToTypt();
                break;
            case R.id.excute_iv:
                // 开始执行翻译
                String msg = editText.getText().toString().trim();
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(mContext, "翻译内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (fromType.equals(toType)) {
                    Toast.makeText(mContext, "语言类型不能一致，请修改后重新操作", Toast.LENGTH_SHORT).show();
                } else {
                    translate(msg);
                }
                hideSoft();
                break;
            case R.id.delete_iv:
                // 清空输入框文本
                editText.setText("");
                break;
            case R.id.copy_iv:
                // 复制翻译结果
                copyText(showTv.getText().toString());
                break;
            case R.id.play_iv:
                playVoice();
                // 播放语音
                break;
            case R.id.change_iv:
                // 语言类型切换
                if (!"auto".equals(fromType)) {
                    resetLanguage();
                } else {
                    Toast.makeText(mContext, "翻译类型不可设置为自动识别", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void hideSoft() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    private void translateVoice(String filePath) {
        TranslateInfo info = new TranslateInfo();
        info.setCatalog("audio");
        info.setFromLang(fromType);
        info.setToLang(toType);
        info.setFromAudio(StringUtils.fileToBase64(new File(filePath)));
        new RequestUtils(getContext(), TAG, Constants.TRANSLATE, new Gson().toJson(info), new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {

                TranslateInfo resultData = GsonUtils.getResult(result, TranslateInfo.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        Toast.makeText(mContext, resultData.getDescription(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (resultData.getData() != null) {
                        voicePath = resultData.getData().getToAudioUrl();
                        editText.setInputType(InputType.TYPE_CLASS_TEXT);
                        editText.setText(resultData.getData().getFromText());
                        editText.setSelection(resultData.getData().getFromText().length());
                        showTv.setText(resultData.getData().getToText());
                        moreLay.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void fail(VolleyError volleyError) {
                // 暂时不做统一处理，留待以后可能会有差异情况处理
                if (volleyError != null) {
                    Toast.makeText(getContext(), "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void translate(String msg) {
        TranslateInfo info = new TranslateInfo();
        info.setCatalog("text");
        info.setFromLang(fromType);
        info.setToLang(toType);
        info.setFromText(msg);
        new RequestUtils(getContext(), TAG, Constants.TRANSLATE, new Gson().toJson(info), new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {

                TranslateInfo resultData = GsonUtils.getResult(result, TranslateInfo.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        Toast.makeText(mContext, resultData.getDescription(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (resultData.getData() != null) {
                        voicePath = resultData.getData().getToAudioUrl();
                        showTv.setText(resultData.getData().getToText());
                        moreLay.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void fail(VolleyError volleyError) {
                // 暂时不做统一处理，留待以后可能会有差异情况处理
                if (volleyError != null) {
                    Toast.makeText(getContext(), "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void playVoice() {
        if (!TextUtils.isEmpty(voicePath)) {
            playIv.setImageResource(R.drawable.voice_tran_play);
            AnimationDrawable animation = (AnimationDrawable) playIv.getDrawable();
            animation.start();
            PlayVoiceUtils.playAudio(Constants.BASE_URL + "/" + voicePath, new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    playIv.setImageResource(R.drawable.ic_play);
                }
            });
        } else {
            Toast.makeText(mContext, "语音地址为空", Toast.LENGTH_SHORT).show();
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

        SPUtils.setVoiceFromName(fromName, mContext);
        SPUtils.setVoiceFromType(fromType, mContext);
        SPUtils.setVoiceToName(toName, mContext);
        SPUtils.setVoiceToType(toType, mContext);
    }

    private void copyText(String content) {
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(content);
        Toast.makeText(mContext, "成功复制到剪切板", Toast.LENGTH_SHORT).show();
    }

    private void showFromTypt() {
        if (showfromNameList.size() == 0 && showfromTypeList.size() == 0) {
            Toast.makeText(mContext, "消息列表获取失败", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder fromBuilder = new AlertDialog.Builder(mContext);
        fromBuilder.setItems(showfromNameList.toArray(new String[showfromNameList.size()]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fromName = showfromNameList.get(i);
                fromType = showfromTypeList.get(i);
                SPUtils.setVoiceFromName(fromName, mContext);
                SPUtils.setVoiceFromType(fromType, mContext);
                fromTv.setText(fromName);
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
            Toast.makeText(mContext, "消息列表获取失败", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder toBuilder = new AlertDialog.Builder(mContext);
        toBuilder.setItems(showToNameList.toArray(new String[showToNameList.size()]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                toName = showToNameList.get(i);
                toType = showToTypeList.get(i);
                toTv.setText(toName);
                SPUtils.setVoiceToName(toName, mContext);
                SPUtils.setVoiceToType(toType, mContext);
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

}
