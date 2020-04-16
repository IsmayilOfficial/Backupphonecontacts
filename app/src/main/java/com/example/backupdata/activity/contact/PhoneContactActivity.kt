package com.example.backupdata.activity.contact

import android.app.AlertDialog
import android.os.Bundle

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat

import com.example.backupdata.contact.FetchDeviceContactAsyncTask
import com.example.backupdata.helper.DatabaseHelper
import com.example.backupdata.contact.Contact
import com.example.backupdata.util.CommonUtils
import com.example.backupdata.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

import java.util.*


class PhoneContactActivity : BaseContactActivity() {
    private val isOnlyDeviceContact = true
    private lateinit var mFetchDeviceContactAsyncTask: FetchDeviceContactAsyncTask

    // the database reference till contact >> user+id
    private var mUserEndPoint: DatabaseReference? = null
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
        mFabUploadDownload?.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.stat_sys_upload_done))
        startDeviceContactTask()
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
    private fun startDeviceContactTask() {
        mFetchDeviceContactAsyncTask = object : FetchDeviceContactAsyncTask(this, isOnlyDeviceContact) {
            override fun onPostExecute(contacts: MutableList<Contact?>?) {

//                Log.d("check","size " + contacts.size());
                mLoadingProgress!!.visibility = View.GONE
                if (CommonUtils.isCollectionNullOrEmpty(contacts)) {
                    mTvNoData!!.visibility = View.VISIBLE
                } else {
                    showContacts(contacts)
                }
            }
        }
        mFetchDeviceContactAsyncTask.execute()
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
            uploadContactsToCloud(selectedContacts)
        }
    }

    // method performs operation on cloud server to upload selected contacts
    private fun uploadContactsToCloud(selectedContacts: MutableList<Contact?>?) {
        val userId = CommonUtils.getUserId(this@PhoneContactActivity, applicationContext)
        if (userId != null) {
            mUserEndPoint = DatabaseHelper.getUserContactEndPoint(userId)
            DatabaseHelper.writeNewContact(mUserEndPoint, selectedContacts)
            addDatabaseListener()
            showProgress()
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
                mUserEndPoint?.removeEventListener(valueEventListener)
            }
        }
    }

    // show confirmation dialog, whether contacts uploaded on cloud server successfully or not
    private fun showConfirmationDialog(uploadSuccessful: Boolean) {
        val uploadAlertDialog = AlertDialog.Builder(this)
        if (uploadSuccessful) {
            uploadAlertDialog.setMessage(R.string.contact_upload_success)
        } else {
            uploadAlertDialog.setMessage(R.string.err_contact_upload_fail)
        }
        uploadAlertDialog.setCancelable(false)
        uploadAlertDialog.setPositiveButton(R.string.ok) { dialog, which -> finish() }
        uploadAlertDialog.create().show()
    }

    companion object {
        private val TAG: String? = "PhoneContactActivity"
    }
}