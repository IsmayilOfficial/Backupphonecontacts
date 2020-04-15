package com.droid.backupphone.activity.sms

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.ListView
import android.widget.TextView
import com.droid.backupphone.R
import com.droid.backupphone.activity.BaseActivity
import com.droid.backupphone.activity.contact.CloudContactActivity
import com.droid.backupphone.activity.contact.PhoneContactActivity
import com.droid.backupphone.adapter.MultiSelectSmsListAdapter
import com.droid.backupphone.model.sms.Sms
open class BaseSmsActivity : BaseActivity() {
    protected lateinit var mFabUploadDownload: FloatingActionButton
    protected lateinit var mLvSms: ListView
    protected lateinit var mTvNoData: TextView
    protected lateinit var mLoadingProgress: View
    protected lateinit var mListAdapter: MultiSelectSmsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_sms)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        mFabUploadDownload = findViewById(R.id.fab_upload_download) as FloatingActionButton
        mFabUploadDownload.setOnClickListener(View.OnClickListener { view -> performUploadDownload(view) })
        mLvSms = findViewById(R.id.lv_sms) as ListView
        mTvNoData = findViewById(R.id.tv_no_data) as TextView
        mLoadingProgress = findViewById(R.id.view_contact_progress)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contact, menu)
        val selectAll = menu?.findItem(R.id.action_select_all)
        val cbSelectAll = selectAll?.actionView?.findViewById(R.id.action_item_checkbox) as CheckBox
        cbSelectAll?.setOnClickListener { v -> // update the menu item checked state based on checkbox checked state
            onOptionsItemSelected(selectAll?.setChecked((v as CheckBox).isChecked))
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.getItemId()) {
            R.id.action_select_all -> selectAllSms(item!!.isChecked())
        }
        return super.onOptionsItemSelected(item)
    }

    // select / deselect all contacts in listview.
    private fun selectAllSms(checked: Boolean) {
        mListAdapter.updateCheckBox(checked)
    }

    // the method detail is given in subclasses.
    protected open fun performUploadDownload(view: View?) {
        // do nothing
    }

    // show list of contacts in list view.
    protected fun showSms(contacts: MutableList<Sms?>?) {
        mLvSms.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE)
        mListAdapter = MultiSelectSmsListAdapter(this, contacts)
        mLvSms.setAdapter(mListAdapter)
        mLvSms.setVisibility(View.VISIBLE)
        mFabUploadDownload.setVisibility(View.VISIBLE)
    }

    // show progress bar & hide other UI components
    protected fun showProgress() {
        mFabUploadDownload.setVisibility(View.GONE)
        mLvSms.setVisibility(View.GONE)
        mLoadingProgress.setVisibility(View.VISIBLE)
    }

    // hide progress bar and show other UI components
    protected fun hideProgress() {
        mFabUploadDownload.setVisibility(View.VISIBLE)
        mLvSms.setVisibility(View.VISIBLE)
        mLoadingProgress.setVisibility(View.GONE)
    }
}