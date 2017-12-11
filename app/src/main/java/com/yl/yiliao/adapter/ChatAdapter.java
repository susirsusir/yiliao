package com.yl.yiliao.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yl.yiliao.R;
import com.yl.yiliao.activity.UserInfoActivity;
import com.yl.yiliao.model.database.ChatItem;
import com.yl.yiliao.utils.Constants;
import com.yl.yiliao.utils.ImageUtiles;
import com.yl.yiliao.utils.PlayVoiceUtils;
import com.yl.yiliao.utils.SPUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends BaseAdapter {

    private Context mContext;
    private List<ChatItem> items;
    private String otherImgPath;
    private String myImgPath;
    private final int RECEIVE = 1;
    private final int SEND = 0;
    private String myPhone;
    private String hostPhone;

    public ChatAdapter(Context mContext, List<ChatItem> items, String otherImgPath, String myPhone, String hostPhone) {
        this.mContext = mContext;
        this.items = items;
        this.otherImgPath = otherImgPath;
        this.myPhone = myPhone;
        this.hostPhone = hostPhone;
        myImgPath = SPUtils.getAvater(SPUtils.getPhoneNo(mContext), mContext);

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        ChatItem item = items.get(position);
        if (item.getSourceType() == RECEIVE) {
            return RECEIVE;
        } else {
            return SEND;
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final ViewHolder holder;
        int type = getItemViewType(position);
        if (view == null) {
            holder = new ViewHolder();
            switch (type) {
                case RECEIVE:
                    view = LayoutInflater.from(mContext).inflate(R.layout.chat_list_receive_item, null);
                    holder.Avater = view.findViewById(R.id.user_icon);
                    holder.tvCreate = view.findViewById(R.id.chat_time);
                    holder.itemLay = view.findViewById(R.id.item_lay);
                    holder.tvFromText = view.findViewById(R.id.from_tv);
                    holder.tvToText = view.findViewById(R.id.to_tv);
                    holder.line = view.findViewById(R.id.line);
                    holder.ivVoice = view.findViewById(R.id.iv_voice);
                    view.setTag(holder);
                    break;
                case SEND:
                    view = LayoutInflater.from(mContext).inflate(R.layout.chat_list_send_item, null);
                    holder.Avater = view.findViewById(R.id.user_icon);
                    holder.tvCreate = view.findViewById(R.id.chat_time);
                    holder.itemLay = view.findViewById(R.id.item_lay);
                    holder.tvFromText = view.findViewById(R.id.from_tv);
                    holder.tvToText = view.findViewById(R.id.to_tv);
                    holder.line = view.findViewById(R.id.line);
                    holder.ivVoice = view.findViewById(R.id.iv_voice);
                    view.setTag(holder);
                    break;
                default:
                    break;
            }
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ChatItem item = items.get(position);
        long time = formatTime(item.getCreate_at());
        String showTime = simpleDateFormat.format(new Date(time));
        if (time > 0) {
            if (position != 0) {
                if ((time - formatTime(items.get(position - 1).getCreate_at())) > 180000) {
                    holder.tvCreate.setVisibility(View.VISIBLE);
                    holder.tvCreate.setText(showTime);
                } else {
                    holder.tvCreate.setVisibility(View.GONE);
                }
            } else {
                holder.tvCreate.setVisibility(View.VISIBLE);
                holder.tvCreate.setText(showTime);
            }
        } else {
            holder.tvCreate.setVisibility(View.VISIBLE);
            holder.tvCreate.setText(showTime);
        }

        if (TextUtils.isEmpty(item.getFromContent())) {
            // 原始数据为空表明是发送数据，并且没有发送成功
            holder.tvFromText.setVisibility(View.GONE);
            holder.tvToText.setVisibility(View.GONE);
            holder.ivVoice.setVisibility(View.GONE);
            holder.line.setVisibility(View.GONE);
        } else {
            holder.tvFromText.setText(item.getFromContent());
            holder.tvToText.setText(item.getToContent());
            holder.tvFromText.setVisibility(View.VISIBLE);
            holder.tvToText.setVisibility(View.VISIBLE);
            holder.ivVoice.setVisibility(View.VISIBLE);
            holder.line.setVisibility(View.VISIBLE);
            playAudio(holder.ivVoice, item.getToVoice());
        }


        switch (type) {
            case RECEIVE:
                ImageUtiles.loadGlideHead(holder.Avater, otherImgPath);
                holder.Avater.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 跳转到用户空间
                        Intent intent = new Intent(mContext, UserInfoActivity.class);
                        intent.putExtra("phoneNo", hostPhone);
                        mContext.startActivity(intent);
                    }
                });
                break;
            case SEND:
                ImageUtiles.loadGlideHead(holder.Avater, myImgPath);
                holder.Avater.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 跳转到用户空间
                        Intent intent = new Intent(mContext, UserInfoActivity.class);
                        intent.putExtra("phoneNo", myPhone);
                        mContext.startActivity(intent);
                    }
                });
                break;
        }
        return view;
    }

    private void playAudio(final ImageView playIv, final String url) {
        if (TextUtils.isEmpty(url)) {
            playIv.setVisibility(View.GONE);
            return;
        }
        playIv.setVisibility(View.VISIBLE);
        playIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playIv.setImageResource(R.drawable.voice_tran_play);
                AnimationDrawable animation = (AnimationDrawable) playIv.getDrawable();
                animation.start();
                PlayVoiceUtils.playAudio(Constants.BASE_URL + "/" + url, new MediaPlayer.OnCompletionListener() {

                    public void onCompletion(MediaPlayer mp) {
                        playIv.setImageResource(R.drawable.ic_play);
                    }
                });
            }
        });
    }

    public static class ViewHolder {
        TextView tvCreate;
        TextView tvFromText;
        TextView tvToText;
        ImageView ivVoice;
        ImageView Avater;
        RelativeLayout itemLay;
        View line;
    }

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private long formatTime(String creat_at) {
        return Long.parseLong(creat_at);
    }

    public void updateSingleRow(ListView listview, long id) {
        int firstPosition = listview.getFirstVisiblePosition();
        int lasrPosition = listview.getLastVisiblePosition();
        for (int i = firstPosition; i <= lasrPosition; i++) {
            if (listview.getItemIdAtPosition(i) == id) {   //当传过来的id与遍历的id相同就更新
                View view = listview.getChildAt(i - firstPosition);  //如果可见的第一个item不是0
                getView(i, view, null);
            }
        }
    }
}
