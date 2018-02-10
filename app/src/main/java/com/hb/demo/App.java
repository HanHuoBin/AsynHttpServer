package com.hb.demo;

import android.app.Application;
import android.content.Context;

/**
 * Created by hanbin on 2018/2/10.
 */

public class App extends Application {

    private static Context mContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    /**
     * 获取上下文
     *
     * @return Context
     */
    public static Context getContext() {
        return mContext;
    }

}
