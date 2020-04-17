package com.example.backupdata.contact

import android.content.Context
import android.database.Cursor
import android.os.AsyncTask
import android.provider.ContactsContract

import android.util.Log
import com.example.backupdata.common.CommonConstants

import java.util.*


open class FetchDeviceContactAsyncTask(context: Context?, onlyDeviceContact: Boolean) : AsyncTask<Void?, Void?, MutableList<Contact?>?>() {
    private val TAG: String? = "FetchDeviceContactTask"
    private var mContext: Context? = null
    private var mIsOnlyDeviceContact = false
    override fun doInBackground(vararg params: Void?): MutableList<Contact?>? {
        val cursor = mContext?.getContentResolver()?.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
                null)
        return getContacts(cursor)
    }

    // get list of contacts from cursor object
    private fun getContacts(cursor: Cursor?): MutableList<Contact?>? {
        val contacts: MutableList<Contact?> = ArrayList()
        if (cursor != null && cursor.moveToFirst()) { // must check the result to prevent exception
            do { // iterate each contact here
                val contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneList: MutableList<PhoneDetail?> = ArrayList()

                // fetch phone number for each contact
                if (isContactHasPhoneNumber(cursor)) {
//                    Log.e(TAG, "Contact name : " + contactName);
                    retrievePhoneForContact(contactId, phoneList)
                }

                // add contact  if it has at least 1 phone number
                if (phoneList.size > 0) {
                    val contact = Contact(contactId, contactName, phoneList)
                    contacts.add(contact)
                }
            } while (cursor.moveToNext())
        }
        return contacts
    }

    // check if contact has phone number or not
    private fun isContactHasPhoneNumber(cursor: Cursor?): Boolean {
        return cursor!!.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)).toInt() > 0
    }

    // fetch all phone numbers for given contact id
    private fun retrievePhoneForContact(contactId: String?, phoneList: MutableList<PhoneDetail?>?) {
        //the below cursor will give you details for multiple contacts
        val phoneCursor = mContext!!.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf(contactId), null)
        if (phoneCursor != null) {

            // continue till this cursor reaches to all phone numbers which are associated with a contact
            // in the contact list
            if (phoneCursor.moveToFirst()) {
                do {
                    val phoneType = getPhoneType(phoneCursor)
                    val phoneNo = getPhoneNumber(phoneCursor)
                    val isSim = checkIsSim(phoneCursor)
                    var phone: PhoneDetail? = null
                    if (phoneType >= ContactsContract.CommonDataKinds.Phone.TYPE_HOME
                            && phoneType <= ContactsContract.CommonDataKinds.Phone.TYPE_MMS) {
                        phone = PhoneDetail(phoneType, phoneNo)
                    }
                    //                    showLog(phoneType, phoneNo, isSim);

                    // if onlyDeviceContact = true, only device contact will be saved here
                    if (phone != null && (if (mIsOnlyDeviceContact) !isSim else true)) {
                        phoneList?.add(phone)
                    }
                } while (phoneCursor.moveToNext())
            }
            phoneCursor.close()
        }
    }

    // get phone type constant from given cursor object
    private fun getPhoneType(phoneCursor: Cursor?): Int {
        return phoneCursor!!.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))
    }

    // get phone number from given cursor object
    private fun getPhoneNumber(phoneCursor: Cursor?): String? {
        return phoneCursor!!.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
    }

    // check if phone number saved in sim or device
    private fun checkIsSim(phoneCursor: Cursor?): Boolean {
        var isSim = true
        val isSimColumnIndex = phoneCursor!!.getColumnIndex(CommonConstants.IS_SIM)
        if (isSimColumnIndex != -1) {
            // value =0 for device contact and non-zero(1,2) for sim contacts
            if (phoneCursor.getInt(isSimColumnIndex) == 0) {
                isSim = false
            }
        } else {
            // label value be null or 'SIM'
            val label = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL))
            if (label == null) {
                isSim = false
            }
        }
        return isSim
    }

    // print log
    private fun showLog(phoneType: Int, phoneNo: String?, isSim: Boolean) {
        when (phoneType) {
            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> Log.e(TAG, ": TYPE_MOBILE : $phoneNo : IS_SIM : $isSim")
            ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> Log.e(TAG, ": TYPE_HOME : $phoneNo : IS_SIM : $isSim")
            ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> Log.e(TAG, ": TYPE_WORK : $phoneNo : IS_SIM : $isSim")
            ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE -> Log.e(TAG, ": TYPE_WORK_MOBILE : $phoneNo : IS_SIM : $isSim")
            ContactsContract.CommonDataKinds.Phone.TYPE_OTHER -> Log.e(TAG, ": TYPE_OTHER : $phoneNo : IS_SIM : $isSim")
            else -> {
            }
        }
    }


    init {
        mContext = context
        mIsOnlyDeviceContact = onlyDeviceContact
    }
}