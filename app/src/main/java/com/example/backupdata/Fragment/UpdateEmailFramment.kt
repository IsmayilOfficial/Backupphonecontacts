package com.example.backupdata.Fragment

import android.os.Bundle


import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

import com.example.backupdata.helper.LoginHelper
import com.example.backupdata.util.CommonUtils
import com.example.backupdata.util.PreferenceUtils
import com.example.backupdata.R
import com.google.firebase.auth.FirebaseAuth



class UpdateEmailFramment : Fragment(), View.OnClickListener {
    var updatePasswordView: View? = null
    var tv_currentUserName: TextView? = null
    var actv_newEmail: AutoCompleteTextView? = null
    var btn_udpatePassword: Button? = null
    var firebaseAuth: FirebaseAuth? = null
    protected var mProgressView: View? = null
    private var mLoginFormView: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        updatePasswordView = inflater?.inflate(R.layout.password_update_view, container, false)
        tv_currentUserName = updatePasswordView?.findViewById(R.id.tv_current_user_name) as TextView
        actv_newEmail = updatePasswordView!!.findViewById(R.id.actv_newpassword) as AutoCompleteTextView
        btn_udpatePassword = updatePasswordView!!.findViewById(R.id.btn_update_password) as Button
        mLoginFormView = updatePasswordView!!.findViewById(R.id.view_forget_parent) as View
        mProgressView = updatePasswordView!!.findViewById(R.id.view_update_progress) as View
        firebaseAuth = FirebaseAuth.getInstance()
        tv_currentUserName!!.setText(PreferenceUtils.getUserEmail(activity?.applicationContext))
        btn_udpatePassword!!.setOnClickListener(this)
        return updatePasswordView
    }

    override fun onClick(v: View?) {
        when (v?.getId()) {
            R.id.btn_update_password -> CommonUtils.hideSoftKeyboard(activity?.applicationContext, v, 0)
        }
    }

    fun updateEmail(Email: String?) {
        if (!validateEmail()) {
            return
        }
        val user = firebaseAuth?.getCurrentUser()
        user?.updateEmail(actv_newEmail!!.getText().toString().trim { it <= ' ' })
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, "Email address is updated.", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(activity, "Failed to update email!", Toast.LENGTH_LONG).show()
                    }
                }
    }

    private fun showProgress(show: Boolean) {
        mProgressView?.setVisibility(if (show) View.VISIBLE else View.GONE)
        mLoginFormView?.setVisibility(if (show) View.GONE else View.VISIBLE)
    }

    protected fun validateEmail(): Boolean {

        // Reset errors.
        actv_newEmail?.setError(null)


        // Store values at the time of the login attempt.
        val email = actv_newEmail?.getText().toString()
        var valid = true
        var focusView: View? = null

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            actv_newEmail?.setError(getString(R.string.error_field_required))
            focusView = actv_newEmail
            valid = false
        } else if (!LoginHelper.isEmailValid(email)) {
            actv_newEmail?.setError(getString(R.string.error_invalid_email))
            focusView = actv_newEmail
            valid = false
        }
        if (!valid) {
            focusView?.requestFocus()
        }
        return valid
    }
}