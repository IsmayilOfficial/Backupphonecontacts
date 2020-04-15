package com.droid.backupphone.Fragment

import android.os.Bundle

import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.droid.backupphone.R
import com.droid.backupphone.helper.LoginHelper
import com.droid.backupphone.util.PreferenceUtils
import com.google.firebase.auth.FirebaseAuth



class UpdatePasswordFramment : Fragment(), View.OnClickListener {
    var updatePasswordView: View? = null
    var tv_currentUserName: TextView? = null
    var actv_newpassword: AutoCompleteTextView? = null
    var btn_udpatePassword: Button? = null
    var firebaseAuth: FirebaseAuth? = null
    protected var mProgressView: View? = null
    private var mLoginFormView: View? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        updatePasswordView = inflater?.inflate(R.layout.password_update_view, container, false)
        tv_currentUserName = updatePasswordView?.findViewById(R.id.tv_current_user_name) as TextView
        actv_newpassword = updatePasswordView!!.findViewById(R.id.actv_newpassword) as AutoCompleteTextView
        btn_udpatePassword = updatePasswordView!!.findViewById(R.id.btn_update_password) as Button
        mLoginFormView = updatePasswordView!!.findViewById(R.id.view_forget_parent) as View
        mProgressView = updatePasswordView!!.findViewById(R.id.view_update_progress) as View
        firebaseAuth = FirebaseAuth.getInstance()
        tv_currentUserName!!.setText(PreferenceUtils.getUserEmail(activity.applicationContext))
        btn_udpatePassword!!.setOnClickListener(this)
        return updatePasswordView
    }

    override fun onClick(v: View?) {
        when (v?.getId()) {
            R.id.btn_update_password -> updatePassword()
        }
    }

    fun updatePassword() {
        if (!validatePassword()) {
            return
        }
        showProgress(true)
        val user = firebaseAuth?.getCurrentUser()
        user?.updatePassword(actv_newpassword?.getText().toString().trim { it <= ' ' })
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, "Password is updated!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "Failed to update password!", Toast.LENGTH_SHORT).show()
                        showProgress(false)
                    }
                }
    }

    protected fun validatePassword(): Boolean {

        // Reset errors.
        actv_newpassword?.setError(null)

        // Store values at the time of the login attempt.
        val password = actv_newpassword?.getText().toString()
        var valid = true
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!LoginHelper.isPasswordValid(password)) {
            actv_newpassword?.setError(getString(R.string.error_invalid_password))
            focusView = actv_newpassword
            valid = false
        }
        if (!valid) {
            focusView?.requestFocus()
        }
        return valid
    }

    private fun showProgress(show: Boolean) {
        mProgressView?.setVisibility(if (show) View.VISIBLE else View.GONE)
        mLoginFormView?.setVisibility(if (show) View.GONE else View.VISIBLE)
    }
}