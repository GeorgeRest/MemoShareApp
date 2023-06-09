package com.george.memoshareapp.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.george.memoshareapp.activities.RegisterActivity;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.utils
 * @className: PermissionUtils
 * @author: George
 * @description: TODO
 * @date: 2023/4/27 13:04
 * @version: 1.0
 */
public class PermissionUtils {

    public static void permissionsGranted(Context context) {
        XXPermissions.with(context)
                .permission(Permission.SEND_SMS)
                .permission(Permission.ACCESS_COARSE_LOCATION)//网络
                .permission(Permission.ACCESS_FINE_LOCATION)//GPS定位
                .permission(Permission.READ_PHONE_STATE)//读取手机当前状态
                .permission(Permission.READ_EXTERNAL_STORAGE)//读取外部存储
                .permission(Permission.WRITE_EXTERNAL_STORAGE)//写入外部存储
                .permission(Permission.RECORD_AUDIO)
                .permission(Permission.WRITE_SETTINGS)
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .permission(Permission.CAMERA)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                        if (!allGranted) {
                            Toasty.info(context, "部分权限未正常授予,可能会导致某些功能无法正常使用", Toast.LENGTH_SHORT, true).show();
                            return;
                        }
                    }
                    @Override
                    public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
                        if (doNotAskAgain) {
                            Toasty.error(context, "获取权限失败，请手动授予权限", Toast.LENGTH_SHORT, true).show();
                            XXPermissions.startPermissionActivity(context, permissions);
                        }
                    }
                });

    }


}
