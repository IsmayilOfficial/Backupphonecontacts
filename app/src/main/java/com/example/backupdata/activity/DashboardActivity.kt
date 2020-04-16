package com.example.backupdata.activity

import android.Manifest.permission
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle


import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.example.backupdata.activity.contact.ContactSyncActivity
import com.example.backupdata.common.CommonConstants
import com.example.backupdata.util.CommonUtils
import com.example.backupdata.util.PreferenceUtils
import com.example.backupdata.R
import com.example.backupdata.activity.Setting.SettingActivity
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.content_dashboard.*


class DashboardActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        setSupportActionBar(toolbar as androidx.appcompat.widget.Toolbar?)
        val tvUser = findViewById(R.id.tv_user) as TextView
        tvUser.text = PreferenceUtils.getUserEmail(applicationContext)
       cv_contact.setOnClickListener(this)

        cv_photos.setOnClickListener(this)
    cv_setting.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dashboard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.getItemId()) {
            R.id.action_logout -> showLogOutDialog()
            R.id.action_settings -> Toast.makeText(this, "Feature Coming soon...", Toast.LENGTH_SHORT).show()
            R.id.action_help -> Toast.makeText(this, "Feature Coming soon...", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
//        showLogOutDialog();
        finish()
    }

    // Show log out option to the user with Yes/No option.
    private fun showLogOutDialog() {
        val logoutAlertDialog = AlertDialog.Builder(this@DashboardActivity)
        logoutAlertDialog.setMessage(R.string.logout_message)
        logoutAlertDialog.setNegativeButton(R.string.yes) { dialog, which ->
            CommonUtils.signOutFromApp()
            CommonUtils.logOut(this@DashboardActivity, applicationContext)
            finish()
        }
        logoutAlertDialog.setPositiveButton(R.string.no, null)
        logoutAlertDialog.create().show()
    }

    override fun onClick(view: View?) {
        when (view?.getId()) {
            R.id.cv_contact -> if (mayRequestContacts()) {
                openContactSyncScreen()
            }

            R.id.cv_photos -> Toast.makeText(this, "Feature Coming soon...", Toast.LENGTH_SHORT).show()
            R.id.cv_setting -> {
                val contactIntent = Intent(this@DashboardActivity, SettingActivity::class.java)
                startActivity(contactIntent)
            }
        }
    }

    // Open contact sync screen
    private fun openContactSyncScreen() {
        val contactIntent = Intent(this@DashboardActivity, ContactSyncActivity::class.java)
        startActivity(contactIntent)
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

    private fun mayRequestSms(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        if (checkSelfPermission(permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        requestPermissions(arrayOf<String?>(permission.READ_SMS), CommonConstants.REQUEST_READ_SMS)
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
                                            grantResults: IntArray) {
        if (requestCode == CommonConstants.REQUEST_READ_CONTACT) {
            if (grantResults.size > 0 && permission.READ_CONTACTS == permissions[0] && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openContactSyncScreen()
            }
        } else if (requestCode == CommonConstants.REQUEST_READ_SMS) {
            if (grantResults.size > 0 && permission.READ_SMS == permissions[0] && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openContactSyncScreen()
            }
        }
    }
}