package com.mm.xclassloader.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mm.xclassloader.R

import android.widget.TextView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //Xposed 进行 Hook 调用
    private fun moduleHookSuccessText() {
        val moduleHint = findViewById<TextView>(R.id.moduleHint)
        moduleHint.setText(R.string.module_enabled)
    }
}