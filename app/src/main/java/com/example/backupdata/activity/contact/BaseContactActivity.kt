package com.example.backupdata.activity.contact

import android.annotation.SuppressLint
import android.os.Bundle

import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

import com.example.backupdata.activity.BaseActivity
import com.example.backupdata.adapter.MultiSelectContactListAdapter
import com.example.backupdata.contact.Contact
import com.example.backupdata.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


open class BaseContactActivity : BaseActivity() {
    protected var mFabUploadDownload: FloatingActionButton? = null
    protected var mLvContact: ListView? = null
    protected var mTvNoData: TextView? = null
    protected var mLoadingProgress: View? = null
    var mListAdapter: MultiSelectContactListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_contact)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        mFabUploadDownload = findViewById(R.id.fab_upload_download) as FloatingActionButton
        mFabUploadDownload!!.setOnClickListener(View.OnClickListener { view -> performUploadDownload(view) })
        mLvContact = findViewById(R.id.lv_contact) as ListView
        mTvNoData = findViewById(R.id.tv_no_data) as TextView
        mLoadingProgress = findViewById(R.id.view_contact_progress)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contact, menu)
        val selectAll = menu?.findItem(R.id.action_select_all)
        val cbSelectAll = selectAll?.actionView!!.findViewById(R.id.action_item_checkbox) as CheckBox
        cbSelectAll?.setOnClickListener { v -> // update the menu item checked state based on checkbox checked state
            onOptionsItemSelected(selectAll?.setChecked((v as CheckBox).isChecked))
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.getItemId()) {
            R.id.action_select_all -> selectAllContact(item.isChecked())
        }
        return super.onOptionsItemSelected(item)
    }

    // select / deselect all contacts in listview.
    private fun selectAllContact(checked: Boolean) {

//        Log.d("check","count "+mListAdapter.getCount() + " : " + checked );
        mListAdapter!!.updateCheckBox(checked)
    }

    // the method detail is given in subclasses.
    protected open fun performUploadDownload(view: View?) {
        // do nothing
    }

    // show list of contacts in list view.
    @SuppressLint("RestrictedApi")
    protected fun showContacts(contacts: MutableList<Contact?>?) {
        mLvContact?.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE)
        mListAdapter = MultiSelectContactListAdapter(this, contacts)
        mLvContact?.setAdapter(mListAdapter)
        mLvContact?.setVisibility(View.VISIBLE)
        mFabUploadDownload!!.visibility = View.VISIBLE
    }

    // show progress bar & hide other UI components
    @SuppressLint("RestrictedApi")
    protected fun showProgress() {
        mFabUploadDownload?.visibility = View.GONE
        mLvContact?.setVisibility(View.GONE)
        mLoadingProgress?.setVisibility(View.VISIBLE)
    }

    // hide progress bar and show other UI components
    @SuppressLint("RestrictedApi")
    protected fun hideProgress() {
        mFabUploadDownload?.setVisibility(View.VISIBLE)
        mLvContact?.setVisibility(View.VISIBLE)
        mLoadingProgress?.setVisibility(View.GONE)
    }
}