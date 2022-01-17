package com.mm.xclassloader;

/// 该类下存放被hook的包名
public class PackNames {
    protected static final String MINE_PACK = PackNames.class.getPackage().getName();
    protected static final String HOOK_PACK = "com.tencent.mm"; // test pack is wechat

    protected static String[] packNames = new String[]{
            MINE_PACK,
            HOOK_PACK,
    };
}
