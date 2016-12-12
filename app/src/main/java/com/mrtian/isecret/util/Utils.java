package com.mrtian.isecret.util;

import android.content.ClipboardManager;
import android.content.Context;

import com.google.gson.Gson;

/**
 * Created by tianxiying on 16/12/8.
 */
public class Utils {
    public static Gson gson = new Gson();
    /**
     * 实现文本复制功能
     * add by wangqianzhou
     * @param content
     */
    public static void copy(String content, Context context)
    {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }
}
