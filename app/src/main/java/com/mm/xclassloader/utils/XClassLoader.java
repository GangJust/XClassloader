package com.mm.xclassloader.utils;

public class XClassLoader extends ClassLoader {
    private ClassLoader currLoader;
    private ClassLoader hookLoader;
    private ClassLoader lpparamLoader;

    public XClassLoader(ClassLoader currLoader, ClassLoader hookLoader) {
        this.currLoader = currLoader;
        this.hookLoader = hookLoader;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        //是否已经加载
        Class<?> clazz = findLoadedClass(name);
        if (clazz != null) return clazz;

        //HookApp寻找
        if (hookLoader != null) {
            try {
                return hookLoader.loadClass(name);
            } catch (ClassNotFoundException e) {
            }
        }

        //CurrApp寻找
        if (currLoader != null) {
            try {
                return currLoader.loadClass(name);
            } catch (ClassNotFoundException e) {
            }
        }

        // 没找到
        throw new ClassNotFoundException(name);
    }

    public void printClassLoader() {
        LogUtil.xLog("currLoader: " + currLoader);
        LogUtil.xLog("hookLoader: " + hookLoader);
    }
}
