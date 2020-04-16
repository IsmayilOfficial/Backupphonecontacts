package com.example.backupdata.contact

import android.content.ContentProviderOperation
import android.content.Context
import android.content.OperationApplicationException
import android.os.AsyncTask
import android.os.RemoteException
import android.provider.ContactsContract

import android.util.Log
import com.example.backupdata.helper.DatabaseHelper

import java.util.*


open class WriteDeviceContactAsyncTask(applicationContext: Context?, selectedContacts: MutableList<Contact?>?) : AsyncTask<Void?, Void?, Int?>() {
    private var mSelectedContacts: MutableList<Contact?>? = null
    private var mApplicationContext: Context? = null
    override fun doInBackground(vararg params: Void?): Int? {
        var successFullWriteCount = 0
        for (contact in this!!.mSelectedContacts!!) {
            val contentProviderOperations = ArrayList<ContentProviderOperation?>()
            val contactIndex = 0 //Integer.parseInt(contact.getId());
            // A raw contact will be inserted ContactsContract.RawContacts table in contacts database.
            contentProviderOperations.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build())

            //Display name will be inserted in ContactsContract.Data table
            contentProviderOperations.add(
                DatabaseHelper.getAddNameOperation(contactIndex, contact
                    ?.getContactName()))
            for (phoneDetail in contact!!.getPhoneList()!!) {
                contentProviderOperations.add(
                    DatabaseHelper.getAddPhoneOperation(contactIndex,
                        phoneDetail?.getPhoneNumber(), phoneDetail!!.getPhoneType()))
            }
            try {
                // We will do batch operation to insert all above data Contains the output of the app of a
                // ContentProviderOperation.
                //It is sure to have exactly one of uri or count set
                val contentProviderResults = mApplicationContext?.contentResolver
                        ?.applyBatch(ContactsContract.AUTHORITY, contentProviderOperations)
                Log.d(TAG, "Contact write for " + contact.getContactName() + " success.")
                successFullWriteCount++
            } catch (exp: RemoteException) {
                //logs;
                Log.e(TAG, "write of " + contact.getContactName() + " failed. :" + exp.message)
            } catch (exp: OperationApplicationException) {
                //logs
                Log.e(TAG, "write of " + contact.getContactName() + " failed. :" + exp.message)
            }
        }
        return successFullWriteCount
    }

    companion object {
        private val TAG: String? = "WriteDeviceContactTask"
    }


    init {
        mApplicationContext = applicationContext
        mSelectedContacts = selectedContacts
    }
}