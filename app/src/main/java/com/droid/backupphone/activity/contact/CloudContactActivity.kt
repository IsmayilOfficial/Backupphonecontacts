package com.droid.backupphone.activity.contact

import android.app.AlertDialog
import android.os.Bundle
import android.support.design.widget.Snackbar

import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import com.droid.backupphone.R
import com.droid.backupphone.activity.contact.CloudContactActivity
import com.droid.backupphone.asynctask.contact.WriteDeviceContactAsyncTask
import com.droid.backupphone.helper.DatabaseHelper
import com.droid.backupphone.model.contact.Contact
import com.droid.backupphone.util.CommonUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

import java.util.*


class CloudContactActivity : BaseContactActivity() {
    // the database reference till contact >> user+id
    private lateinit var mUserEndPoint: DatabaseReference
    private lateinit var mWriteDeviceContactAsyncTask: WriteDeviceContactAsyncTask
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
                        val contacts: MutableList<Contact?> = ArrayList()
                        for (postSnapshot in dataSnapshot.children) {
                            Log.d(TAG, "ValueEventListener : " + "Key : " + postSnapshot.key)
                            val contact = postSnapshot.getValue(Contact::class.java)
                            if (contact != null) {
                                contacts.add(contact)
                                //Log.d(TAG, "ValueEventListener : " + "contact : " + contact.contactName + "," +
                                 //       contact.id)
                            }
                        }
                        showContacts(contacts)
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
        mFabUploadDownload?.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.stat_sys_download_done))
        startCloudContactDownloadTask()
    }

    public override fun onStop() {
        super.onStop()
        removeDatabaseListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        nullifyAsyncTasks(mWriteDeviceContactAsyncTask)
    }

    // start fetching contacts from cloud server
    private fun startCloudContactDownloadTask() {
        val userId = CommonUtils.getUserId(this@CloudContactActivity, applicationContext)
        if (userId != null) {
            mUserEndPoint = DatabaseHelper.getUserContactEndPoint(userId)!!
            addDatabaseListener()
            showProgress()
            // perform a sample write operation on cloud and you will get the data in listener
            CommonUtils.getDatabaseReference()!!.child("app_title").setValue("Realtime Database1")
        }
    }

    // add database listener
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

    override fun performUploadDownload(view: View?) {
        super.performUploadDownload(view)
        val selectedContacts: MutableList<Contact?> = ArrayList()
        selectedContacts.clear()
        val selectedContact_cb = mListAdapter?.getCheckedHolder()
        for (i in 0 until mListAdapter!!.count) {
            if (selectedContact_cb!![i]) {
                val contact = mLvContact?.adapter?.getItem(i) as Contact
                selectedContacts.add(contact)
            }
        }
        if (CommonUtils.isCollectionNullOrEmpty(selectedContacts)) {
            Snackbar.make(view!!, R.string.select_atleast_one, Snackbar.LENGTH_SHORT).show()
        } else {
            writeContactsInDevice(selectedContacts)
        }
    }

    // start async task to write selected cloud contacts in device
    private fun writeContactsInDevice(selectedContacts: MutableList<Contact?>?) {
        mWriteDeviceContactAsyncTask = object : WriteDeviceContactAsyncTask(applicationContext, selectedContacts) {
            override fun onPreExecute() {
                super.onPreExecute()
                showProgress()
            }

            override fun onPostExecute(successFullWriteCount: Int?) {
                hideProgress()
                showConfirmationDialog(selectedContacts!!.size == successFullWriteCount)
            }
        }
        mWriteDeviceContactAsyncTask.execute()
    }

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
        private val TAG: String? = "CloudContactActivity"
    }
}