package com.example.backupdata.activity

import android.os.AsyncTask


import androidx.appcompat.app.AppCompatActivity


/**
 * The Base class for all the activities in app.
 */
open class BaseActivity : AppCompatActivity() {
    /**
     * Override this method to cancel any running async task and nullify it to free the memory.
     *
     * @param asyncTasks the async task to reset
     */
    protected fun nullifyAsyncTasks(vararg asyncTasks: AsyncTask<*, *, *>?) {
        if (asyncTasks != null && asyncTasks.size > 0) {
            for (asyncTask in asyncTasks) {
                if (asyncTask != null && asyncTask.status == AsyncTask.Status.RUNNING) {
                    asyncTask.cancel(true)
                }
            }
        }
    }
}