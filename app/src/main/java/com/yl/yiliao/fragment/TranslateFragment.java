package com.yl.yiliao.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yl.yiliao.R;
import com.yl.yiliao.activity.MainActivity;
import com.yl.yiliao.adapter.ViewPagerFragmentAdapter;
import com.yl.yiliao.utils.IntegerUtils;
import com.yl.yiliao.widget.TabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TranslateFragment extends BaseFragment {


    public TranslateFragment() {
    }

    private TabStrip tabStrip;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_translate, container, false);
            initView();
        } else {
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
        }
        return mView;
    }

    private ImageView textIv;
    private TextView textTv;
    private ImageView voiceIv;
    private TextView voiceTv;
    private int qtrWidth;
    private ImageView tranIv;
    private int index = 0;

    private void initView() {
        tabStrip = mView.findViewById(R.id.tabs);
        viewPager = mView.findViewById(R.id.view_pager);

        int width = IntegerUtils.getScreenWidth(mContext);
        qtrWidth = width / 4 + 40;
        tranIv = mView.findViewById(R.id.translate_iv);
        tranIv.setTranslationX(qtrWidth);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TranslsteTextFragment());
        fragments.add(new TranslateVoiceFragment());

        ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getFragmentManager());

        adapter.setFragments(fragments);

        View textView = LayoutInflater.from(mContext).inflate(R.layout.trandlate_tab_item, null, false);
        textTv = textView.findViewById(R.id.translate_tv);
        textTv.setTextColor(getResources().getColor(R.color.white));
        textTv.setText("文字");
        textIv = textView.findViewById(R.id.translate_img);
        textIv.setImageResource(R.drawable.ic_text_tran);
        adapter.addView(textView);

        View voiceView = LayoutInflater.from(mContext).inflate(R.layout.trandlate_tab_item, null, false);
        voiceTv = voiceView.findViewById(R.id.translate_tv);
        voiceTv.setTextColor(getResources().getColor(R.color.blue0));
        voiceTv.setText("语音");
        voiceIv = voiceView.findViewById(R.id.translate_img);
        voiceIv.setImageResource(R.drawable.ic_voice_tran);
        adapter.addView(voiceView);

        viewPager.setAdapter(adapter);
        tabStrip.setViewPager(viewPager);
        tabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (index == position && positionOffsetPixels > 0) {
                    tranIv.setTranslationX(qtrWidth + ((positionOffsetPixels * 1.1f) / 3));
                } else {
                    index = position;
                }
            }

            @Override
            public void onPageSelected(int position) {
                resetTab(position);
                if (position == 1) {
                    if (!((MainActivity) mContext).isAllowRecord()) {
                        Toast.makeText(mContext, "缺少录音权限或存储权限，语音翻译暂无法使用", Toast.LENGTH_SHORT).show();
                        viewPager.setCurrentItem(0, true);
                        ((MainActivity) mContext).initPermission();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void resetTab(int position) {
        textIv.setImageResource(position == 0 ? R.drawable.ic_text_tran : R.drawable.ic_text_tran_sel);
        textTv.setTextColor(position == 0 ? getResources().getColor(R.color.white) : getResources().getColor(R.color.blue0));
        voiceIv.setImageResource(position == 1 ? R.drawable.ic_voice_tran_sel : R.drawable.ic_voice_tran);
        voiceTv.setTextColor(position == 1 ? getResources().getColor(R.color.white) : getResources().getColor(R.color.blue0));
    }

}
