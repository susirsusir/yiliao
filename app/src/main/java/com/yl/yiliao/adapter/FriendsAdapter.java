package com.yl.yiliao.adapter;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yl.yiliao.R;
import com.yl.yiliao.activity.ChatActivity;
import com.yl.yiliao.activity.UserInfoActivity;
import com.yl.yiliao.model.FriendData;
import com.yl.yiliao.utils.ImageUtiles;

import java.util.List;

public class FriendsAdapter extends BaseAdapter {

    private Context mContext;
    private List<FriendData> datas;

    public FriendsAdapter(Context mContext, List<FriendData> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.friend_list_item, null);
            holder.itemImg = view.findViewById(R.id.user_icon);
            holder.itemName = view.findViewById(R.id.item_name);
            holder.itemLay = view.findViewById(R.id.item_lay);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final FriendData data = datas.get(position);
        ImageUtiles.loadGlideHead(holder.itemImg, data.getAvatar());
        holder.itemName.setText(data.getNickName());
        holder.itemImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳转到用户空间
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                intent.putExtra("phoneNo", data.getPhoneNo());
                mContext.startActivity(intent);
            }
        });

        holder.itemLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳转到聊天界面
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("nickName", data.getNickName());
                intent.putExtra("phone", data.getPhoneNo());
                if (!TextUtils.isEmpty(data.getAvatar())) {
                    intent.putExtra("avatar", data.getAvatar());
                }
                mContext.startActivity(intent);
            }
        });


        return view;
    }

    public static class ViewHolder {
        TextView itemName;
        ImageView itemImg;
        RelativeLayout itemLay;
    }


    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<FriendData> list) {
        this.datas = list;
        notifyDataSetChanged();
    }


    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = datas.get(i).getGroupTitle();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }


}
