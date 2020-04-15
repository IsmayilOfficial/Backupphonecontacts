package com.droid.backupphone.activity.sms

import android.app.AlertDialog
import android.os.Bundle
import android.support.design.widget.Snackbar

import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import com.droid.backupphone.R
import com.droid.backupphone.activity.sms.PhoneSmsActivity
import com.droid.backupphone.asynctask.sms.FetchDeviceSmsAsyncTask
import com.droid.backupphone.helper.DatabaseHelper
import com.droid.backupphone.model.sms.Sms
import com.droid.backupphone.util.CommonUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

import java.util.*


class PhoneSmsActivity : BaseSmsActivity() {
    private lateinit var mFetchDeviceContactAsyncTask: FetchDeviceSmsAsyncTask

    // the database reference till contact >> user+id
    private lateinit var mUserEndPoint: DatabaseReference

    //
    private val valueEventListener: ValueEventListener? = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot?) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            if (dataSnapshot != null) {
                if (dataSnapshot.key != null && dataSnapshot.value == null) {
                    Log.d("ValueEventListener", "Account : " + dataSnapshot.key + " does not exist.")
                } else if (dataSnapshot.value != null) {
                    removeDatabaseListener()
                    hideProgress()
                    showConfirmationDialog(true)
                    val response = dataSnapshot.value.toString()
                    Log.d(TAG, "ValueEventListener : " + "Key : " + dataSnapshot.key)
                    Log.d(TAG, "ValueEventListener : Response : $response")
                } else {
                    showConfirmationDialog(false)
                }
            } else {
                showConfirmationDialog(false)
            }
        }

        override fun onCancelled(error: DatabaseError?) {
            // Failed to read value
            Log.w(TAG, "Failed to read value.", error?.toException())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFabUploadDownload.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.stat_sys_upload_done))
        startDeviceSmsTask()
    }

    public override fun onStop() {
        super.onStop()
        removeDatabaseListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        nullifyAsyncTasks(mFetchDeviceContactAsyncTask)
    }

    // start async task to fetch the contact list from device.
    private fun startDeviceSmsTask() {
        mFetchDeviceContactAsyncTask = object : FetchDeviceSmsAsyncTask(this) {
            override fun onPostExecute(smsList: MutableList<Sms?>?) {
                mLoadingProgress.visibility = View.GONE
                if (CommonUtils.isCollectionNullOrEmpty(smsList)) {
                    mTvNoData.visibility = View.VISIBLE
                } else {
                    showSms(smsList)
                }
            }
        }
        mFetchDeviceContactAsyncTask.execute()
    }

    override fun performUploadDownload(view: View?) {
        super.performUploadDownload(view)
        val selectedSms: MutableList<Sms?> = ArrayList()
        selectedSms.clear()
        val selectedContact_cb = mListAdapter.getCheckedHolder()
        for (i in 0 until mListAdapter.count) {
            if (selectedContact_cb!![i]) {
                Log.d("check", "check" + selectedContact_cb[i] + " : " + i)
                val contact = mLvSms.adapter.getItem(i) as Sms
                selectedSms.add(contact)
            }
        }
        if (CommonUtils.isCollectionNullOrEmpty(selectedSms)) {
            Snackbar.make(view!!, R.string.select_atleast_one, Snackbar.LENGTH_SHORT).show()
        } else {
//            uploadSmsToCloud(selectedSms);
        }
    }

    // method performs operation on cloud server to upload selected contacts
    private fun uploadSmsToCloud(selectedSms: MutableList<Sms?>?) {
        val userId = CommonUtils.getUserId(this@PhoneSmsActivity, applicationContext)
        if (userId != null) {
            mUserEndPoint = DatabaseHelper.getUserSmsEndPoint(userId)!!
            DatabaseHelper.writeNewSms(mUserEndPoint, selectedSms)
            addDatabaseListener()
            showProgress()
        }
    }

    //     add database listener
    private fun addDatabaseListener() {
        mUserEndPoint.addValueEventListener(valueEventListener)
    }

    // remove database listener
    private fun removeDatabaseListener() {
        if (mUserEndPoint != null) {
            if (valueEventListener != null) {
                mUserEndPoint.removeEventListener(valueEventListener)
            }
        }
    }

    // show confirmation dialog, whether Sms uploaded on cloud server successfully or not
    private fun showConfirmationDialog(uploadSuccessful: Boolean) {
        val uploadAlertDialog = AlertDialog.Builder(this)
        if (uploadSuccessful) {
            uploadAlertDialog.setMessage(R.string.sms_upload_success)
        } else {
            uploadAlertDialog.setMessage(R.string.err_sms_upload_fail)
        }
        uploadAlertDialog.setCancelable(false)
        uploadAlertDialog.setPositiveButton(R.string.ok) { dialog, which -> finish() }
        uploadAlertDialog.create().show()
    }

    companion object {
        private val TAG: String? = "PhoneSmsActivity"
    }
}