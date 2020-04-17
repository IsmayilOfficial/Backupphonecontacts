package com.example.backupdata.adapter

import android.content.Context

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.backupdata.contact.Contact
import com.example.backupdata.R


class MultiSelectContactListAdapter(private val mContext: Context?, contactList: MutableList<Contact?>?) : BaseAdapter() {
     internal lateinit var checkedHolder: BooleanArray
    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private val mContactList: MutableList<Contact?>? = contactList
    lateinit var contact_cb: CheckBox
    lateinit var contact_name: TextView
    lateinit var contact_number: TextView
    private fun createCheckedHolder() {
        checkedHolder = BooleanArray(mContactList!!.size)
        for (i in checkedHolder.indices) {
            setCheckHolder(i, false)
        }
    }

    override fun getCount(): Int {

            return mContactList!!.size


      }

    override fun getItem(position: Int): Any? {

            return mContactList!![position]

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View? {
        var view = view
        val holder: RecyclerView.ViewHolder
        if (view == null) {
            view = mInflater.inflate(R.layout.content_contact, null)
            contact_cb = view.findViewById(R.id.cb_contact) as CheckBox
            contact_name = view.findViewById(R.id.tv_contact_name) as TextView
            contact_number = view.findViewById(R.id.tv_contact_number) as TextView

        } else {

        }
        val contact = mContactList!![position]

        this.contact_name.setText(contact?.getContactName())
        run {
            contact_name!!.setText(contact?.getContactName())
            contact_number.setText(contact?.getPhoneList()!![0]?.getPhoneNumber())
            contact_cb.setChecked(checkedHolder.get(position))
        }
        view?.setOnClickListener(View.OnClickListener { v ->
            Log.d("check", "on listener " + " : " + checkedHolder.get(position))
            setCheckHolder(position, !checkedHolder[position])
            contact_cb.setChecked(checkedHolder.get(position))
            v.invalidate()
        })
        contact_cb.setOnClickListener(View.OnClickListener { v ->
            val parent = v.parent as View
            parent.performClick()
        })
        return view
    }

    private fun setCheckHolder(position: Int, currentStatus: Boolean) {
        checkedHolder[position] = currentStatus
    }

    fun setClickedHolder(position: Int, currentStatus: Boolean) {
        checkedHolder[position] = currentStatus
        notifyDataSetChanged()
    }

    fun getCheckedHolder(): BooleanArray? {
        return checkedHolder
    }

    fun updateCheckBox(currentStatus: Boolean) {
        for (i in checkedHolder.indices) {
            setCheckHolder(i, currentStatus)
        }
        notifyDataSetChanged()
    }

    // The view holder class

    init {
        createCheckedHolder()
    }
}