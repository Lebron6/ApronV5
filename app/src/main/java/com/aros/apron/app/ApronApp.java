package com.aros.apron.app;

import android.app.Application;
import android.content.Context;
import com.aros.apron.xclog.XcFileLog;
import com.aros.apron.xclog.XcLogConfig;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

public class ApronApp extends Application {
    public static String SERIAL_NUMBER ;//测试
    private static Context context;
    public static Context getApplication() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        initConfig();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        com.secneo.sdk.Helper.install(this);
    }

    /**
     * Logger 初始化配置
     */
    private void initConfig() {
        PrettyFormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // 隐藏线程信息 默认：显示
                .methodCount(0)         // 决定打印多少行（每一行代表一个方法）默认：2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
//              .tag("JASON_LOGGER")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
//                return super.isLoggable(priority, tag);
                return true;
            }
        });
        XcFileLog.init(new XcLogConfig());
//        CrashHandler.getInstance().init();
    }
}