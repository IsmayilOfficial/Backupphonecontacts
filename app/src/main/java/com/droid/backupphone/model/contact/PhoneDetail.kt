package com.droid.backupphone.model.contact


/**
 * The phone detail model class.
 */
class PhoneDetail {
    private var mPhoneType = 0
    private var mPhoneNumber: String? = null

    constructor() {
        // do nothing
    }

    constructor(phoneType: Int, phoneNumber: String?) {
        mPhoneType = phoneType
        mPhoneNumber = phoneNumber
    }

    fun getPhoneType(): Int {
        return mPhoneType
    }

    fun setPhoneType(phoneType: Int) {
        mPhoneType = phoneType
    }

    fun getPhoneNumber(): String? {
        return mPhoneNumber
    }

    fun setPhoneNumber(phoneNumber: String?) {
        mPhoneNumber = phoneNumber
    }
}