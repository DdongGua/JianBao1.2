package com.bgs.jianbao12.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import java.io.File;

/**
 * Created by Administrator on 2016/12/29.
 */

public class PackageUtils {

    public static String getVersionName(Context context) {
        try {
            // 管理手机的APP
            PackageManager packageManager = context.getPackageManager();
            // 得到APP功能清单文件
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);
            Log.e("MainActivity", "getVersionName: "+info.versionName);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 安装APP
     *
     * @param t File
     */
    public static void installAPK(Context context,File t) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


}
