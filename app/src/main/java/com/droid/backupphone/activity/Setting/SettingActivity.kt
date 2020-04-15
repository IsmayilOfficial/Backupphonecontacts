package com.droid.backupphone.activity.Setting

import android.os.Bundle

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.Toolbar
import com.droid.backupphone.Fragment.SettingFragment
import com.droid.backupphone.Fragment.UpdatePasswordFramment
import com.droid.backupphone.R
import com.droid.backupphone.activity.BaseActivity

class SettingActivity : BaseActivity() {
    lateinit var ft: FragmentTransaction
    lateinit var  mFragmentManager: FragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        mFragmentManager = this.supportFragmentManager
        ft = mFragmentManager.beginTransaction()
        ft.add(R.id.setting_container, SettingFragment(), "main") //        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                .commit()
    }

    fun updatePasswordFragment() {
        mFragmentManager.beginTransaction().replace(R.id.setting_container, UpdatePasswordFramment(), "update Passwod")
                .addToBackStack("update Passwod")
                .commit()
    }

    fun updateEmailFragment() {
        mFragmentManager.beginTransaction().replace(R.id.setting_container, UpdatePasswordFramment(), "update Email")
                .addToBackStack("update Email")
                .commit()
    }

    override fun onBackPressed() {
        val count = mFragmentManager.getBackStackEntryCount()
        if (count == 0) {
            super.onBackPressed()
            //additional code
        } else {
            mFragmentManager.popBackStack()
        }
    }
}