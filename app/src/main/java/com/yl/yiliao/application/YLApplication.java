package com.yl.yiliao.application;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.fresco.helper.Phoenix;
import com.yl.yiliao.fresco.config.PhoenixConfig;
import com.yl.yiliao.gen.DaoMaster;
import com.yl.yiliao.gen.DaoSession;
import com.yl.yiliao.utils.SPUtils;
import com.yl.yiliao.utils.StringUtils;


public class YLApplication extends Application {
    public static RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        mRequestQueue = Volley.newRequestQueue(this);
        mContext = getApplicationContext();

        Phoenix.init(this, PhoenixConfig.get(this).getImagePipelineConfig());
        if (TextUtils.isEmpty(SPUtils.getImei(this))) {
            SPUtils.setImei(StringUtils.getImei(), this);
        }
        setupDatabase();
    }

    /**
     * 配置数据库
     */
    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "yiliao.db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }

    private static DaoSession daoSession;
    private static Context mContext;//上下文

    public static Context getContext() {
        return mContext;
    }
}
