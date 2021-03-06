package com.yl.yiliao.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.yl.yiliao.model.database.ChatItem;
import com.yl.yiliao.model.database.MessageItem;

import com.yl.yiliao.gen.ChatItemDao;
import com.yl.yiliao.gen.MessageItemDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig chatItemDaoConfig;
    private final DaoConfig messageItemDaoConfig;

    private final ChatItemDao chatItemDao;
    private final MessageItemDao messageItemDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        chatItemDaoConfig = daoConfigMap.get(ChatItemDao.class).clone();
        chatItemDaoConfig.initIdentityScope(type);

        messageItemDaoConfig = daoConfigMap.get(MessageItemDao.class).clone();
        messageItemDaoConfig.initIdentityScope(type);

        chatItemDao = new ChatItemDao(chatItemDaoConfig, this);
        messageItemDao = new MessageItemDao(messageItemDaoConfig, this);

        registerDao(ChatItem.class, chatItemDao);
        registerDao(MessageItem.class, messageItemDao);
    }
    
    public void clear() {
        chatItemDaoConfig.clearIdentityScope();
        messageItemDaoConfig.clearIdentityScope();
    }

    public ChatItemDao getChatItemDao() {
        return chatItemDao;
    }

    public MessageItemDao getMessageItemDao() {
        return messageItemDao;
    }

}
