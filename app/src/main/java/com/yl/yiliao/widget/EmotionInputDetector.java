package com.yl.yiliao.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.yl.yiliao.R;
import com.yl.yiliao.activity.ChatActivity;
import com.yl.yiliao.utils.SPUtils;

public class EmotionInputDetector {

    private ChatActivity mActivity;
    private InputMethodManager mInputManager;
    private View mVoiceLayout;
    private EditText mEditText;
    private ListView mContentView;
    private ImageView voiceBtn;
    private SendMessageListener listener;

    private EmotionInputDetector() {
    }

    public static EmotionInputDetector with(ChatActivity activity) {
        EmotionInputDetector emotionInputDetector = new EmotionInputDetector();
        emotionInputDetector.mActivity = activity;
        emotionInputDetector.mInputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        return emotionInputDetector;
    }

    public EmotionInputDetector bindToContent(ListView contentView) {
        mContentView = contentView;
        return this;
    }

    private void selectLast(View view) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                mContentView.setSelection(mContentView.getCount() - 1);
            }
        }, 200L);
    }

    public void hideAll() {
        hideSoftInput();
        hideVoiceLayout(false);
    }

    public EmotionInputDetector bindToEditText(EditText editText) {
        mEditText = editText;
        mEditText.requestFocus();
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && mVoiceLayout.isShown()) {
                    lockContentHeight();
                    hideVoiceLayout(true);
                    mEditText.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            unlockContentHeightDelayed();
                        }
                    }, 200L);
                }
                selectLast(mEditText);
                return false;
            }
        });
        return this;
    }

    public EmotionInputDetector bindToOptionButton(final View optionButton) {
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.sendMessage();
                }
                selectLast(optionButton);
            }
        });
        return this;
    }


    private boolean voiceShow = true;

    public void setShowVoice(boolean isShow) {
        voiceShow = isShow;
    }

    public EmotionInputDetector bindToVoiceButton(final ImageView voiceButton) {
        voiceBtn = voiceButton;
        voiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("auto".equals(mActivity.fromType)) {
                    Toast.makeText(mActivity, "语音翻译不支持自动检测，请设置语言类型", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (voiceShow) {
                    if (mVoiceLayout.isShown()) {
                        lockContentHeight();
                        hideVoiceLayout(true);
                        unlockContentHeightDelayed();
                    } else {
                        if (isSoftInputShown()) {
                            lockContentHeight();
                            showVoiceLayout();
                            unlockContentHeightDelayed();
                        } else {
                            showVoiceLayout();
                        }
                    }
                    selectLast(voiceBtn);
                } else {
                    Toast.makeText(mActivity, "缺少录音权限，无法正常使用语音聊天，请到权限设置中修改权限", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return this;
    }

    public EmotionInputDetector setVoiceView(View voiceView) {
        mVoiceLayout = voiceView;
        return this;
    }

    public EmotionInputDetector build() {
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        hideSoftInput();
        return this;
    }

    public boolean interceptBackPress() {

        if (mVoiceLayout.isShown()) {
            hideVoiceLayout(false);
            return true;
        }
        return false;
    }


    private void showVoiceLayout() {
        int softInputHeight = getSupportSoftInputHeight();
        if (softInputHeight == 0) {
            softInputHeight = SPUtils.getSoftHight(mActivity);
        }
        hideSoftInput();
        mVoiceLayout.getLayoutParams().height = softInputHeight;
        mVoiceLayout.setVisibility(View.VISIBLE);
        voiceBtn.setImageResource(R.drawable.chat_key_select);
    }


    private void hideVoiceLayout(boolean showSoftInput) {
        if (mVoiceLayout.isShown()) {
            mVoiceLayout.setVisibility(View.GONE);
            voiceBtn.setImageResource(R.drawable.chat_voice_select);
            if (showSoftInput) {
                showSoftInput();
            }
        }
    }

    private void lockContentHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        params.height = mContentView.getHeight();
        params.weight = 0.0F;
    }

    private void unlockContentHeightDelayed() {
        mEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout.LayoutParams) mContentView.getLayoutParams()).weight = 1.0F;
            }
        }, 200L);
    }

    private void showSoftInput() {
        mEditText.requestFocus();
        mEditText.post(new Runnable() {
            @Override
            public void run() {
                mInputManager.showSoftInput(mEditText, 0);
            }
        });
    }

    private void hideSoftInput() {
        mInputManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    private boolean isSoftInputShown() {
        return getSupportSoftInputHeight() != 0;
    }

    private int getSupportSoftInputHeight() {
        Rect r = new Rect();
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        int screenHeight = mActivity.getWindow().getDecorView().getRootView().getHeight();
        int softInputHeight = screenHeight - r.bottom;
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight - getSoftButtonsBarHeight();
        }
        if (softInputHeight < 0) {
            Log.w("EmotionInputDetector", "Warning: value of softInputHeight is below zero!");
        }
        if (softInputHeight > 0) {
            SPUtils.setSoftHight(softInputHeight, mActivity);
        }
        return softInputHeight;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        mActivity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    public void setSendMsgListener(SendMessageListener listener) {
        this.listener = listener;
    }

    public static interface SendMessageListener {
        void sendMessage();
    }

}
