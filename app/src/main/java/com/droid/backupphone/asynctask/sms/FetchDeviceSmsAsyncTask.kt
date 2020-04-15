package com.droid.backupphone.asynctask.sms

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask

import com.droid.backupphone.model.sms.Sms

import java.util.*

/**
 * The async task class to fetch all the device contacts.
 */
open class FetchDeviceSmsAsyncTask(context: Context?) : AsyncTask<Void?, Void?, MutableList<Sms?>?>() {
    private val TAG: String? = "FetchDeviceSmsAsyncTask"
    private var mContext: Context? = null
    override fun doInBackground(vararg params: Void?): MutableList<Sms?>? {
        // create cursor for sms
        val cursor = mContext?.getContentResolver()?.query(Uri.parse("content://sms/"), null, null, null, null)
        return getSms(cursor)
    }

    // get list of sms object from cursor object
    private fun getSms(cursor: Cursor?): MutableList<Sms?>? {
        val totalSms: MutableList<Sms?> = ArrayList()
        if (cursor!!.moveToFirst()) {
            do {
                val objSms = Sms()
                objSms.address = cursor.getString(cursor
                        .getColumnIndexOrThrow("address"))
                objSms.msg = cursor.getString(cursor.getColumnIndexOrThrow("body"))
                objSms.time = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                totalSms.add(objSms)
            } while (cursor.moveToNext())
        } else {
            throw RuntimeException("You have no SMS")
        }
        cursor.close()
        return totalSms
    }


    init {
        mContext = context
    }
}