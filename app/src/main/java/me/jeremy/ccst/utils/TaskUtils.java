package me.jeremy.ccst.utils;

import android.os.AsyncTask;
import android.os.Build;

/**
 * Powered by stormzhang
 */
public class TaskUtils {
    public static <Params, Progress, Result> void executeAsyncTask(
            AsyncTask<Params, Params, Result> task, Params... paramses) {
        if (Build.VERSION.SDK_INT >= 11) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, paramses);
        } else {
            task.execute(paramses);
        }
    }
}
