package com.droid.backupphone.activity.contact

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle

import android.support.v7.widget.Toolbar
import android.view.View
import com.droid.backupphone.R
import com.droid.backupphone.activity.BaseActivity
import com.droid.backupphone.activity.contact.ContactSyncActivity
import com.droid.backupphone.common.CommonConstants



class ContactSyncActivity : BaseActivity(), View.OnClickListener {
    private var mClickedButtonId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_sync)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        findViewById(R.id.btn_phone_contact).setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when (view?.getId()) {
            R.id.btn_phone_contact -> {
                mClickedButtonId = view!!.getId()
                if (mayRequestContacts()) {
                    openNextScreen()
                }
            }
            else -> {
            }
        }
    }

    // check for contact permission
    private fun mayRequestContacts(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        if (checkSelfPermission(permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        requestPermissions(arrayOf<String?>(permission.READ_CONTACTS), CommonConstants.REQUEST_READ_CONTACT)
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
                                            grantResults: IntArray) {
        if (requestCode == CommonConstants.REQUEST_READ_CONTACT) {
            if (grantResults.size > 0 && permission.READ_CONTACTS == permissions[0] && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openNextScreen()
            }
        }
    }

    // open next screen based on clicked button id.
    protected fun openNextScreen() {
        when (mClickedButtonId) {
            R.id.btn_phone_contact -> {
                val phoneContactIntent = Intent(this@ContactSyncActivity, PhoneContactActivity::class.java)
                startActivity(phoneContactIntent)
                mClickedButtonId = -1
            }

            else -> {
            }
        }
    }
}