package com.example.backupdata.activity.login


import android.text.TextUtils
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.EditText

import com.example.backupdata.activity.BaseActivity
import com.example.backupdata.helper.LoginHelper
import com.example.backupdata.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener



open class BaseSignInSignUpActivity : BaseActivity() {
    // UI references.
    protected lateinit var mEmailView: AutoCompleteTextView
    protected lateinit var mPasswordView: EditText
    protected lateinit var mProgressView: View
    protected lateinit var mAuth: FirebaseAuth
    protected lateinit var mAuthListener: AuthStateListener
    public override fun onStart() {
        super.onStart()
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener)
        }
    }

    public override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener)
        }
    }

    // validate form fields
    protected fun validateForm(): Boolean {

        // Reset errors.
        mEmailView.setError(null)
        mPasswordView.setError(null)

        // Store values at the time of the login attempt.
        val email = mEmailView.getText().toString()
        val password = mPasswordView.getText().toString()
        var valid = true
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!LoginHelper.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password))
            focusView = mPasswordView
            valid = false
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required))
            focusView = mEmailView
            valid = false
        } else if (!LoginHelper.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email))
            focusView = mEmailView
            valid = false
        }
        if (!valid) {
            focusView?.requestFocus()
        }
        return valid
    }

    // clear password
    protected fun clearCredentials() {
        mPasswordView.setText(R.string.empty)
    }
}