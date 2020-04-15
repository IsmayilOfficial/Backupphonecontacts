package com.droid.backupphone.activity.sms

import android.app.AlertDialog
import android.os.Bundle
import android.support.design.widget.Snackbar

import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import com.droid.backupphone.R
import com.droid.backupphone.activity.sms.CloudSmsActivity
import com.droid.backupphone.helper.DatabaseHelper
import com.droid.backupphone.model.sms.Sms
import com.droid.backupphone.util.CommonUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

import java.util.*


class CloudSmsActivity : BaseSmsActivity() {
    // the database reference till contact >> user+id
    private lateinit var mUserEndPoint: DatabaseReference
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
                    val response = dataSnapshot.value.toString()
                    Log.d(TAG, "ValueEventListener : " + "Key : " + dataSnapshot.key)
                    Log.d(TAG, "ValueEventListener : Response : $response")
                    if (dataSnapshot.children != null) {
                        val contacts: MutableList<Sms?> = ArrayList()
                        for (postSnapshot in dataSnapshot.children) {
                            Log.d(TAG, "ValueEventListener : " + "Key : " + postSnapshot.key)
                            val sms = postSnapshot.getValue(Sms::class.java)
                            if (sms != null) {
                                contacts.add(sms)
                            }
                        }
                        showSms(contacts)
                    }
                }
            }
        }

        override fun onCancelled(error: DatabaseError?) {
            // Failed to read value
            Log.w(TAG, "Failed to read value.", error?.toException())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFabUploadDownload.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.stat_sys_download_done))
        startCloudContactDownloadTask()
    }

    public override fun onStop() {
        super.onStop()
        removeDatabaseListener()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    // start fetching contacts from cloud server
    private fun startCloudContactDownloadTask() {
        val userId = CommonUtils.getUserId(this@CloudSmsActivity, applicationContext)
        if (userId != null) {
            mUserEndPoint = DatabaseHelper.getUserSmsEndPoint(userId)!!
            addDatabaseListener()
            showProgress()
            // perform a sample write operation on cloud and you will get the data in listener
            CommonUtils.getDatabaseReference()?.child("app_title")?.setValue("Realtime Database1")
        }
    }

    // add database listener
    private fun addDatabaseListener() {
        mUserEndPoint?.addValueEventListener(valueEventListener)
    }

    // remove database listener
    private fun removeDatabaseListener() {
        if (mUserEndPoint != null) {
            if (valueEventListener != null) {
                mUserEndPoint.removeEventListener(valueEventListener)
            }
        }
    }

    override fun performUploadDownload(view: View?) {
        super.performUploadDownload(view)
        val selectedSms: MutableList<Sms?> = ArrayList()
        selectedSms.clear()
        val selectedContact_cb = mListAdapter.getCheckedHolder()
        for (i in 0 until mListAdapter.count) {
            if (selectedContact_cb!![i]) {
                Log.d("check", "check" + selectedContact_cb!![i] + " : " + i)
                val contact = mLvSms.adapter.getItem(i) as Sms
                selectedSms.add(contact)
            }
        }
        if (CommonUtils.isCollectionNullOrEmpty(selectedSms)) {
            Snackbar.make(view!!, R.string.select_atleast_one, Snackbar.LENGTH_SHORT).show()
        }
    }

    // start async task to write selected cloud contacts in device
    // show confirmation dialog, whether contacts written on device successfully or not
    private fun showConfirmationDialog(uploadSuccessful: Boolean) {
        val uploadAlertDialog = AlertDialog.Builder(this)
        if (uploadSuccessful) {
            uploadAlertDialog.setMessage(R.string.contact_write_success)
        } else {
            uploadAlertDialog.setMessage(R.string.contact_write_partially_success)
        }
        uploadAlertDialog.setCancelable(false)
        uploadAlertDialog.setPositiveButton(R.string.ok) { dialog, which -> finish() }
        uploadAlertDialog.create().show()
    }

    companion object {
        private val TAG: String? = "CloudSmsActivity"
    }
}