package com.droid.backupphone.Fragment

import android.os.Bundle

import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.droid.backupphone.R
import com.droid.backupphone.activity.Setting.SettingActivity



class SettingFragment : Fragment(), View.OnClickListener {
    var viewSetting: View? = null
    var cv_UserProfile: CardView? = null
    var cv_changeUserProfileInfo: CardView? = null
    var cv_passwordUpdate: CardView? = null
    var cv_changeEmail: CardView? = null
    var cv_deleteAccount: CardView? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (inflater != null) {
            viewSetting = inflater.inflate(R.layout.setting_dashboard, container, false)
        }
        cv_UserProfile = viewSetting?.findViewById(R.id.cv_current_user_profile) as CardView
        cv_changeUserProfileInfo = viewSetting!!.findViewById(R.id.cv_user_profile_update) as CardView
        cv_passwordUpdate = viewSetting!!.findViewById(R.id.cv_update_password) as CardView
        cv_changeEmail = viewSetting!!.findViewById(R.id.cv_change_email) as CardView
        cv_deleteAccount = viewSetting!!.findViewById(R.id.cv_delete_account) as CardView
        cv_UserProfile!!.setOnClickListener(this)
        cv_changeUserProfileInfo!!.setOnClickListener(this)
        cv_passwordUpdate!!.setOnClickListener(this)
        cv_changeEmail!!.setOnClickListener(this)
        cv_deleteAccount!!.setOnClickListener(this)
        return viewSetting
    }

    override fun onClick(v: View?) {
        val clickId = v?.getId()
        when (clickId) {
            R.id.cv_current_user_profile -> {
            }
            R.id.cv_user_profile_update -> {
            }
            R.id.cv_update_password -> (activity as SettingActivity).updatePasswordFragment()
            R.id.cv_change_email -> (activity as SettingActivity).updateEmailFragment()
            R.id.cv_delete_account -> {
            }
        }
    }
}