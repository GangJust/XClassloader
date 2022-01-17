package com.mm.xclassloader.utils;

import de.robv.android.xposed.XposedBridge;

public class LogUtil {
    public static final String X_LOG_TAG = "xLog - ";

    public static void xLog(Object log) {
        XposedBridge.log(X_LOG_TAG + log);
    }
}
