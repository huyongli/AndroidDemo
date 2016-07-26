package cn.ittiger.demo;

import cn.ittiger.demo.util.CrashHandler;
import cn.ittiger.demo.util.ImageLoaderManager;

import android.app.Application;

/**
 * @author: laohu on 2016/7/24
 * @site: http://ittiger.cn
 */
public class App extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        ImageLoaderManager.init(getApplicationContext());
    }
}
