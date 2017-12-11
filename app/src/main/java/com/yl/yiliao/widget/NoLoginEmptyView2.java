package com.yl.yiliao.widget;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yl.yiliao.R;
import com.yl.yiliao.activity.LoginActivity;

public class NoLoginEmptyView2 extends RelativeLayout implements View.OnClickListener {
    private final String TAG = NoLoginEmptyView2.class.getSimpleName();


    public NoLoginEmptyView2(Context context) {
        super(context);
        init();
    }

    public NoLoginEmptyView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NoLoginEmptyView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.no_login_layout_new, this, true);
        findViewById(R.id.log_btn).setOnClickListener(this);
        final TextView regisbtn = findViewById(R.id.regis_btn);
        regisbtn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    regisbtn.setTextColor(getContext().getResources().getColor(R.color.blue2));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    regisbtn.setTextColor(getContext().getResources().getColor(R.color.cursor_color));
                }
                return false;
            }
        });
        regisbtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.log_btn:
                getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            case R.id.regis_btn:
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.putExtra("isLogin", false);
                getContext().startActivity(intent);
                break;
        }

    }
}