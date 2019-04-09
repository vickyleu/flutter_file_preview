package zzaning.com.filepreview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import com.tencent.smtt.sdk.QbSdk;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * FilePreviewPlugin
 */
public class FilePreviewPlugin implements MethodCallHandler {
    static Context context;
    static Activity activity;

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        context = registrar.context();
        activity = registrar.activity();
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "com.zzaning.file_preview");
        channel.setMethodCallHandler(new FilePreviewPlugin());
        QbSdk.initX5Environment(context, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                //x5内核初始化完成回调接口，此接口回调并表示已经加载起来了x5，有可能特殊情况下x5内核加载失败，切换到系统内核。
                TLog.d("初始化X5成功");
            }

            @Override
            public void onViewInitFinished(boolean b) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                TLog.e("**************", "加载内核是否成功:" + b);
            }
        });
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("openFile")) {
            String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            String filePath = call.argument("path");
            if (!EasyPermissions.hasPermissions(context, perms)) {
                EasyPermissions.requestPermissions(activity, activity.getResources().getString(R.string.storage_permission), 10086, perms);
            } else {
                FileDisplayActivity.show(context, filePath);
            }
            result.success("done");
        } else if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else {
            result.notImplemented();
        }
    }


}
