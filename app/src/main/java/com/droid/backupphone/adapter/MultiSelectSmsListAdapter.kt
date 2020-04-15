package com.droid.backupphone.adapter

import android.content.Context

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.droid.backupphone.R
import com.droid.backupphone.model.sms.Sms

import java.text.SimpleDateFormat
import java.util.*


class MultiSelectSmsListAdapter(private val mContext: Context?, contactList: MutableList<Sms?>?) : BaseAdapter() {
    private lateinit var checkedHolder: BooleanArray
    private val mInflater: LayoutInflater? = LayoutInflater.from(mContext)
    private val mSmsList: MutableList<Sms?>?
    private var contactCheckedBox = false
    override fun getCount(): Int {
        return mSmsList!!.size
    }

    override fun getItem(position: Int): Any? {
        return mSmsList?.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View? {
        var view = view
        val holder: ViewHolder

//        int listPosition=((ListView)parent).getPositionForView(view);
        if (view == null) {
            holder = ViewHolder()
            view = mInflater!!.inflate(R.layout.content_sms, null)
            holder.sms_cb = view.findViewById(R.id.cb_sms) as CheckBox
            holder.sms_address = view.findViewById(R.id.tv_sms_address) as TextView
            holder.sms_date = view.findViewById(R.id.tv_sms_date) as TextView
            holder.sms_message = view.findViewById(R.id.tv_sms_message) as TextView
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }
        val sms = mSmsList?.get(position)
        holder.sms_address?.setText(sms?.getAddress())
        holder.sms_date?.setText("Date : " + smsDateFormated(sms?.getTime()!!.toLong()))
        holder.sms_message?.setText(sms?.getMsg())
        holder.sms_cb?.setChecked(contactCheckedBox)
        val pos = position
        view!!.setOnClickListener(View.OnClickListener { v ->
            val currentStatus = checkedHolder.get(position)
            Log.d("check", "on view click : $position$currentStatus")
            if (currentStatus) {
                setCheckHolder(position, false)
                val tempHolder = v.tag as ViewHolder
                tempHolder.sms_cb!!.setChecked(false)
                v.invalidate()
            } else {
                setCheckHolder(position, true)
                val tempHolder = v.tag as ViewHolder
                tempHolder.sms_cb?.setChecked(true)
                v.invalidate()
            }
        })
        holder.sms_cb!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked -> setCheckHolder(position, isChecked) })
        return view
    }

    fun smsDateFormated(currentDate: Long?): String? {
        return SimpleDateFormat("MM/dd/yyyy").format(Date(currentDate!!))
    }

    private fun createCheckedHolder() {
        checkedHolder = BooleanArray(mSmsList!!.size)
        for (i in checkedHolder.indices) {
            setCheckHolder(i, false)
        }
    }

    fun setCheckHolder(position: Int, currentStatus: Boolean) {
        checkedHolder[position] = currentStatus
    }

    fun getCheckedHolder(): BooleanArray? {
        return checkedHolder
    }

    fun updateCheckBox(currentStatus: Boolean) {
        contactCheckedBox = currentStatus
        for (i in checkedHolder.indices) {
            setCheckHolder(i, currentStatus)
        }
        notifyDataSetChanged()
    }

    // The view holder class
    private class ViewHolder {
        var sms_cb: CheckBox? = null
        var sms_address: TextView? = null
        var sms_date: TextView? = null
        var sms_message: TextView? = null
    }

    init {
        mSmsList = contactList
        createCheckedHolder()
    }
}