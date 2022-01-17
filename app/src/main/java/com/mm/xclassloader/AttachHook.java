package com.mm.xclassloader;

import android.app.Application;
import android.content.Context;

import com.mm.xclassloader.utils.XClassLoader;

import java.lang.reflect.Field;
import java.util.Arrays;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class AttachHook implements IXposedHookLoadPackage {
    protected XClassLoader xClassLoader;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        //务必过滤掉非必要应用。
        if (Arrays.asList(PackNames.packNames).contains(lpparam.packageName)) {
            attach(lpparam);
        }
    }

    private void attach(XC_LoadPackage.LoadPackageParam lpparam) {
        //LogUtil.xLog(lpparam.packageName);
        XposedHelpers.findAndHookMethod(
                Application.class,
                "attach",
                Context.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Context context = (Context) param.args[0];
                        ClassLoader mineLoader = AttachHook.class.getClassLoader();
                        ClassLoader hookLoader = context.getClassLoader();

                        //获取当前ClassLoader
                        Field fParent = ClassLoader.class.getDeclaredField("parent");
                        fParent.setAccessible(true);
                        ClassLoader currLoader = (ClassLoader) fParent.get(mineLoader);
                        if (currLoader == null) currLoader = XposedBridge.class.getClassLoader();

                        //替换当前ClassLoader
                        if (!currLoader.getClass().getName().equals(XClassLoader.class.getName())) {
                            fParent.set(mineLoader, xClassLoader = new XClassLoader(currLoader, hookLoader));
                        }

                        startModule(lpparam);
                    }
                }
        );
    }

    private void startModule(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        //调用 MainHook
        if (xClassLoader != null) lpparam.classLoader = xClassLoader;
        new MainHook().handleLoadPackage(lpparam);
    }
}
