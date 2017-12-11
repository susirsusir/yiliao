package com.yl.yiliao.utils;


import com.yl.yiliao.application.YLApplication;
import com.yl.yiliao.gen.ChatItemDao;
import com.yl.yiliao.gen.MessageItemDao;
import com.yl.yiliao.model.database.ChatItem;
import com.yl.yiliao.model.database.MessageItem;

import java.util.List;

public class MyDataBaseUtils {

    /**
     * 添加数据
     *
     * @param item 当消息在数据库中不存在时候直接插入
     */
    public static void insertMessageItem(MessageItem item) {
        YLApplication.getDaoInstant().getMessageItemDao().insert(item);
    }


    /**
     * 更新数据
     *
     * @param item
     */
    public static void updateMessageItem(MessageItem item) {
        List<MessageItem> items = queryMessageItemList(item.getSendPhone(), item.getReceivePhone());
        if (items != null && items.size() > 0) {
            MessageItem oldItem = items.get(0);
            oldItem.setSendAvatar(item.getSendAvatar());
            oldItem.setSendName(item.getSendName());
            YLApplication.getDaoInstant().getMessageItemDao().update(oldItem);
        }
    }


    /**
     * 查询条件为position数据  获取list而不是获取HomeData是因为可能在未知的情况下存入多个数据，在第二次获取的时候会失败崩溃
     *
     * @return
     */

    public static List<MessageItem> queryMessageItemList(String sendPhone, String receivrPhone) {
        return YLApplication.getDaoInstant().getMessageItemDao().queryBuilder().where(MessageItemDao.Properties.SendPhone.eq(sendPhone), MessageItemDao.Properties.ReceivePhone.eq(receivrPhone)).list();
    }

    public static List<MessageItem> queryMessageItemList(String receivrPhone) {
        return YLApplication.getDaoInstant().getMessageItemDao().queryBuilder().where(MessageItemDao.Properties.ReceivePhone.eq(receivrPhone)).list();
    }

    /**
     * 发送数据
     *
     * @param item 发送数据或者接收数据要首先判断数据库中是否存在，存在则更新不存在插入
     */
    public static void senMessageItem(MessageItem item) {

        List<MessageItem> items = queryMessageItemList(item.getSendPhone(), item.getReceivePhone());
        if (items == null || items.size() == 0) {
            insertMessageItem(item);
        } else {
            MessageItem oldItem = items.get(0);
            oldItem.setCount(0);
            oldItem.setCreate_at(item.getCreate_at());
            oldItem.setTxt_content(item.getTxt_content());
            updateMessageItem(oldItem);
        }
    }

    /**
     * 接收数据
     *
     * @param item 发送数据或者接收数据要首先判断数据库中是否存在，不存在插入 存在删除插入
     */
    public static void receiveMessageItem(MessageItem item) {

        List<MessageItem> items = queryMessageItemList(item.getSendPhone(), item.getReceivePhone());
        if (items != null && items.size() > 0) {
            MessageItem oldItem = items.get(0);
            YLApplication.getDaoInstant().getMessageItemDao().delete(oldItem);
        }
        insertMessageItem(item);
    }

    /**
     * 接收数据
     *
     * @param item 发送数据或者接收数据要首先判断数据库中是否存在，不存在插入 存在删除插入
     */
    public static void saveMessageItem(MessageItem item) {

        List<MessageItem> items = queryMessageItemList(item.getSendPhone(), item.getReceivePhone());
        if (items != null && items.size() > 0) {
            MessageItem oldItem = items.get(0);
            item.setCount(oldItem.getCount() + 1);
            YLApplication.getDaoInstant().getMessageItemDao().delete(oldItem);
        }
        insertMessageItem(item);
    }


    /**
     * 添加数据
     *
     * @param item
     */
    public static void insertChatItem(ChatItem item) {
        YLApplication.getDaoInstant().getChatItemDao().insert(item);
    }

    public static List<ChatItem> queryChatDataList(String hostPhone, String myPhone) {
        return YLApplication.getDaoInstant().getChatItemDao().queryBuilder().where(ChatItemDao.Properties.SendPhone.eq(hostPhone), ChatItemDao.Properties.ReceivePhone.eq(myPhone)).list();
    }


}
