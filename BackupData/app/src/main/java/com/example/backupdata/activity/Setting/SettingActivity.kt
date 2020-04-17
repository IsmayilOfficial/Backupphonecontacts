package com.example.backupdata.activity.Setting

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


import com.example.backupdata.Fragment.SettingFragment
import com.example.backupdata.Fragment.UpdatePasswordFramment
import com.example.backupdata.R

import com.example.backupdata.activity.BaseActivity

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