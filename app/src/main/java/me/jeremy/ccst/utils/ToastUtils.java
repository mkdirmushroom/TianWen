package me.jeremy.ccst.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.devspark.appmsg.AppMsg;

import me.jeremy.ccst.App;

/**
 * Powered by stormzhang
 */
public class ToastUtils {

    private ToastUtils() {
    }

    private static void show(Context context, int resId, int duration) {
        Toast.makeText(context, resId, duration).show();
    }

    private static void show(Context context, String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

    public static void showShort(int resId) {
        Toast.makeText(App.getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(String message) {
        Toast.makeText(App.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(int resId) {
        Toast.makeText(App.getContext(), resId, Toast.LENGTH_LONG).show();
    }

    public static void showLong(String message) {
        Toast.makeText(App.getContext(), message, Toast.LENGTH_LONG).show();
    }

    public static void showAlert(Activity activity, String message) {
        AppMsg.makeText(activity, message, AppMsg.STYLE_ALERT,AppMsg.LENGTH_SHORT ).show();

    }
}
