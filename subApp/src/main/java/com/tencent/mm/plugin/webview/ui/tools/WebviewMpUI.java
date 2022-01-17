package com.tencent.mm.plugin.webview.ui.tools;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class WebviewMpUI extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        throw new RuntimeException("SubApp Error!");
    }
}
