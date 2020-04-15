package com.droid.backupphone.model.sms


class Sms {

    internal lateinit var address: String
    internal lateinit var msg: String
    internal lateinit var time: String
    fun getAddress(): String? {
        return address
    }

    fun setAddress(address: String?) {
        this.address = address.toString()
    }

    fun getMsg(): String? {
        return msg
    }

    fun setMsg(msg: String?) {
        this.msg = msg.toString()
    }

    fun getTime(): String? {
        return time
    }

    fun setTime(time: String?) {
        this.time = time.toString()
    }
}