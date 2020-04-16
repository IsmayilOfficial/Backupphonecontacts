package com.example.backupdata.helper

import android.content.ContentProviderOperation
import android.provider.ContactsContract

import com.example.backupdata.contact.Contact

import com.example.backupdata.util.CommonUtils
import com.google.firebase.database.DatabaseReference



object DatabaseHelper {

    fun updateContact(userEndPoint: DatabaseReference?, contact: Contact?) {
        userEndPoint?.child(contact?.getContactName())?.setValue(contact)
    }





    fun deleteContact(userEndPoint: DatabaseReference?, contact: Contact?) {
        userEndPoint?.child(contact?.getContactName())?.removeValue()
    }





    fun writeNewContact(userEndPoint: DatabaseReference?, contacts: MutableList<Contact?>?) {
        for (contact in contacts!!) {
            userEndPoint?.child(contact?.getContactName())?.setValue(contact)
        }
    }



    fun getUserContactEndPoint(userId: String?): DatabaseReference? {
        return CommonUtils.getContactEndPoint()?.child("user$userId")
    }

    fun getUserSmsEndPoint(userId: String?): DatabaseReference? {
        return CommonUtils.getSmsEndPoint()?.child("user$userId")
    }

    // common method to create an operation.
    private fun getContentProviderOperation(contactRawId: Int?, mimeType: String?,
                                            whatToAdd: String?, data: String?,
                                            typeKey: String?, type: Int): ContentProviderOperation? {
        val contentProviderOperation = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
        if (contactRawId != null) {
            contentProviderOperation.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactRawId)
        }
        if (mimeType != null) {
            contentProviderOperation.withValue(ContactsContract.Data.MIMETYPE, mimeType)
        }
        if (whatToAdd != null && data != null) {
            contentProviderOperation.withValue(whatToAdd, data)
        }
        if (typeKey != null && type != -1) {
            contentProviderOperation.withValue(typeKey, type)
        }
        return contentProviderOperation.build()
    }

    fun getAddNameOperation(contactRawId: Int, strDisplayName: String?): ContentProviderOperation? {
        return getContentProviderOperation(contactRawId,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                strDisplayName,
                null,
                -1)
    }


    fun getAddPhoneOperation(contactRawId: Int?, number: String?, type: Int): ContentProviderOperation? {
        return getContentProviderOperation(contactRawId,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                number,
                ContactsContract.CommonDataKinds.Phone.TYPE,
                type)
    }
}