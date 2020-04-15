package com.droid.backupphone.model.contact


/**
 * The contact model class.
 */
class Contact {
    private var mId: String? = null
    private var mContactName: String? = null
    private var mPhoneList: MutableList<PhoneDetail?>? = null

    constructor() {
        // do nothing
    }

    constructor(id: String?, contactName: String?, phoneList: MutableList<PhoneDetail?>?) {
        mId = id
        mContactName = contactName
        mPhoneList = phoneList
    }

    fun getId(): String? {
        return mId
    }

    fun setId(id: String?) {
        mId = id
    }

    fun getContactName(): String? {
        return mContactName
    }

    fun setContactName(contactName: String?) {
        mContactName = contactName
    }

    fun getPhoneList(): MutableList<PhoneDetail?>? {
        return mPhoneList
    }

    fun setPhoneList(phoneList: MutableList<PhoneDetail?>?) {
        mPhoneList = phoneList
    }
}