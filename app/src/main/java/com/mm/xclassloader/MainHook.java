package com.mm.xclassloader;

import android.os.Bundle;
import android.widget.Toast;

import com.mm.xclassloader.activity.MainActivity;
import com.mm.xclassloader.utils.LogUtil;
import com.tencent.mm.plugin.webview.ui.tools.WebviewMpUI;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        //LogUtil.xLog(lpparam.packageName);
        if (PackNames.MINE_PACK.equals(lpparam.packageName)) {
            initModuleHookSuccess(lpparam);
        } else {
            hookApp(lpparam);
        }
    }

    //初始化，模块状态
    private void initModuleHookSuccess(XC_LoadPackage.LoadPackageParam lpparam) {
        LogUtil.xLog("XClassloader init start!");
        XposedHelpers.findAndHookMethod(
                MainActivity.class.getName(),
                lpparam.classLoader,
                "onCreate",
                Bundle.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Object object = param.thisObject;
                        Method method = object.getClass().getDeclaredMethod("moduleHookSuccessText");
                        method.setAccessible(true);
                        method.invoke(object);
                        LogUtil.xLog("XClassloader init finished!");
                    }
                }
        );
    }

    //Hook应用
    private void hookApp(XC_LoadPackage.LoadPackageParam lpparam) {
        LogUtil.xLog("classloader is: " + lpparam.classLoader);
        // 对微信的Webview进行测试是否能够直接加载
        XposedHelpers.findAndHookMethod(
                WebviewMpUI.class,
                "onCreate",
                Bundle.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        WebviewMpUI webviewMpUI = (WebviewMpUI) param.thisObject;
                        Toast.makeText(webviewMpUI, "webviewmpui hook success!", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}
