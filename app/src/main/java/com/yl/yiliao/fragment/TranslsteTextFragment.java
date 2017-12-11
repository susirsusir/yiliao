package com.yl.yiliao.fragment;


import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.yl.yiliao.R;
import com.yl.yiliao.model.LangCodeData;
import com.yl.yiliao.model.LangCodeItem;
import com.yl.yiliao.model.TranslateInfo;
import com.yl.yiliao.utils.Constants;
import com.yl.yiliao.utils.GsonUtils;
import com.yl.yiliao.utils.PlayVoiceUtils;
import com.yl.yiliao.utils.RequestUtils;
import com.yl.yiliao.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TranslsteTextFragment extends BaseFragment implements View.OnClickListener {
    private final String TAG = TranslsteTextFragment.class.getSimpleName();


    public TranslsteTextFragment() {
        // Required empty public constructor
    }

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
    private String fromType = "auto";
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_translste_text, container, false);
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
        if (!TextUtils.isEmpty(SPUtils.getTextFromName(mContext))) {
            fromName = SPUtils.getTextFromName(mContext);
            fromType = SPUtils.getTextFromType(mContext);
            fromTv.setText(fromName);
        }
        toTv = mView.findViewById(R.id.to_tv);
        if (!TextUtils.isEmpty(SPUtils.getTextToName(mContext))) {
            toName = SPUtils.getTextToName(mContext);
            toType = SPUtils.getTextToType(mContext);
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
        mView.findViewById(R.id.change_iv).setOnClickListener(this);
        initData();
    }

    private void initData() {
        new RequestUtils(mContext, TAG, Constants.GET_LANG_CODE, new RequestUtils.RequestCallback() {
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
                // 播放语音
                playVoice();
                break;
            case R.id.change_iv:
                // 切换语言
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
                        voiceUrl = resultData.getData().getToAudioUrl();
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

    private String voiceUrl;

    private void playVoice() {
        if (!TextUtils.isEmpty(voiceUrl)) {
            playIv.setImageResource(R.drawable.voice_tran_play);
            AnimationDrawable animation = (AnimationDrawable) playIv.getDrawable();
            animation.start();
            PlayVoiceUtils.playAudio(Constants.BASE_URL + "/" + voiceUrl, new MediaPlayer.OnCompletionListener() {

                public void onCompletion(MediaPlayer mp) {
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

        SPUtils.setTextFromName(fromName,mContext);
        SPUtils.setTextFromType(fromType,mContext);
        SPUtils.setTextToName(toName,mContext);
        SPUtils.setTextToType(toType,mContext);

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
                fromTv.setText(fromName);
                SPUtils.setTextFromName(fromName,mContext);
                SPUtils.setTextFromType(fromType,mContext);
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
                SPUtils.setTextToName(fromName,mContext);
                SPUtils.setTextToType(fromType,mContext);
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
