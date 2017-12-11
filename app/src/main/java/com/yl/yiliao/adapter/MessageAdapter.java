package com.yl.yiliao.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yl.yiliao.R;
import com.yl.yiliao.activity.ChatActivity;
import com.yl.yiliao.model.SearchUserData;
import com.yl.yiliao.model.database.MessageItem;
import com.yl.yiliao.utils.Constants;
import com.yl.yiliao.utils.GsonUtils;
import com.yl.yiliao.utils.ImageUtiles;
import com.yl.yiliao.utils.MyDataBaseUtils;
import com.yl.yiliao.utils.RequestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MessageAdapter extends BaseAdapter {
    private final String TAG = MessageAdapter.class.getSimpleName();

    private Context mContext;
    private List<MessageItem> items;
    private String chatPhone;  //当前正在聊天对象手机号

    public MessageAdapter(Context mContext, List<MessageItem> items) {
        this.mContext = mContext;
        this.items = items;
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

    public String getChatPhone() {
        return chatPhone;
    }

    public void setChatPhone(String chatPhone) {
        this.chatPhone = chatPhone;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.message_list_item, null);
            holder.itemImg = view.findViewById(R.id.user_icon);
            holder.tvName = view.findViewById(R.id.item_name);
            holder.itemLay = view.findViewById(R.id.item_lay);
            holder.tvContent = view.findViewById(R.id.item_content);
            holder.tvCount = view.findViewById(R.id.item_count);
            holder.tvTime = view.findViewById(R.id.item_time);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final MessageItem item = items.get(i);
        holder.tvContent.setText(item.getTxt_content());
        if (item.getCount() == 0) {
            holder.tvCount.setVisibility(View.GONE);
        } else {
            holder.tvCount.setVisibility(View.VISIBLE);
            if (item.getCount() > 99) {
                holder.tvCount.setPadding(0, 0, 0, 0);
                holder.tvCount.setText(item.getCount() + "+");
            } else {
                holder.tvCount.setPadding(2, 2, 0, 0);
                holder.tvCount.setText(item.getCount() + "");
            }
        }
        holder.tvTime.setText(format.format(new Date(Long.parseLong(item.getCreate_at()))));
        ImageUtiles.load(holder.itemImg, item.getSendAvatar());

        if (!TextUtils.isEmpty(item.getSendAvatar())) {
            holder.tvName.setText(item.getSendName());
            holder.itemLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 跳转到聊天界面
                    if(!TextUtils.isEmpty(item.getSendName())){
                        Intent intent = new Intent(mContext, ChatActivity.class);
                        intent.putExtra("nickName", item.getSendName());
                        intent.putExtra("phone", item.getSendPhone());
                        if (!TextUtils.isEmpty(item.getSendAvatar())) {
                            intent.putExtra("avatar", item.getSendAvatar());
                        }
                        setChatPhone(item.getSendPhone());
                        mContext.startActivity(intent);
                        item.setCount(0);
                        MyDataBaseUtils.receiveMessageItem(item);
                        notifyDataSetChanged();
                    }
                }
            });
        } else {
            // 需要获取用户信息并展示
            getUserInfo(item, holder.itemImg, holder.tvName, holder.itemLay);
        }


        return view;
    }

    private void getUserInfo(final MessageItem item, final SimpleDraweeView itemImg, final TextView tvName, final RelativeLayout itemLay) {
        new RequestUtils(mContext, TAG, Constants.GET_OTHER_USER_INFO + item.getSendPhone(), new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {

                SearchUserData resultData = GsonUtils.getResult(result, SearchUserData.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        return;
                    }
                    if (resultData.getData() != null) {
                        item.setSendName(resultData.getData().getNickName());
                        item.setSendAvatar(resultData.getData().getAvatar());
                        ImageUtiles.load(itemImg, item.getSendAvatar());
                        tvName.setText(item.getSendName());
                        itemLay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // 跳转到聊天界面
                                Intent intent = new Intent(mContext, ChatActivity.class);
                                intent.putExtra("nickName", item.getSendName());
                                intent.putExtra("phone", item.getSendPhone());
                                if (!TextUtils.isEmpty(item.getSendAvatar())) {
                                    intent.putExtra("avatar", item.getSendAvatar());
                                }
                                setChatPhone(item.getSendPhone());
                                mContext.startActivity(intent);
                                item.setCount(0);
                                MyDataBaseUtils.receiveMessageItem(item);
                                notifyDataSetChanged();

                            }
                        });
                        MyDataBaseUtils.updateMessageItem(item);
                    }
                }
            }

            @Override
            public void fail(VolleyError volleyError) {

            }
        });
    }

    public static class ViewHolder {
        TextView tvName;
        TextView tvContent;
        TextView tvTime;
        TextView tvCount;
        SimpleDraweeView itemImg;
        RelativeLayout itemLay;
    }

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

}
