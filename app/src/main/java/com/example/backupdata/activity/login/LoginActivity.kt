package com.example.backupdata.activity.login

import android.content.Intent
import android.os.Bundle


import android.text.Html
import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView

import com.example.backupdata.activity.DashboardActivity
import com.example.backupdata.common.CommonConstants
import com.example.backupdata.util.CommonUtils
import com.example.backupdata.util.PreferenceUtils
import com.example.backupdata.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseSignInSignUpActivity(), View.OnClickListener {
    private lateinit var mLoginFormView: View

    //    private View.OnClickListener mSignInBtnClickListener = new View.OnClickListener() {
    //        @Override
    //        public void onClick(View view) {
    //
    //        }
    //    };
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Set up the login form.
        mEmailView = findViewById(R.id.actv_email) as AutoCompleteTextView
        mPasswordView = findViewById(R.id.et_password) as EditText
        btn_email_sign_in.setOnClickListener(this)
       tv_forget_password.setOnClickListener(this)
        mLoginFormView = findViewById(R.id.view_login_parent)
        mProgressView = findViewById(R.id.view_login_progress)
        mAuth = FirebaseAuth.getInstance()
        setAuthListener()
        val tvNoAccount = findViewById(R.id.tv_no_account) as TextView
        val sequenceCall = Html.fromHtml(
            CommonUtils.concatenateString(
            CommonConstants.UNDER_LINE_OPEN_TAG,
                getString(R.string.dont_have_account), CommonConstants.UNDER_LINE_CLOSE_TAG))
        CommonUtils.linkifyCallTextView(this, tvNoAccount, sequenceCall)
    }

    // set auth listener to listen if signIn or signUp operation get successful
    private fun setAuthListener() {
        mAuthListener = AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // User is signed in
                val userId = user.uid
                Log.d(TAG, "onAuthStateChanged:signed_in:")
                Log.d(TAG, "User Id : $userId")
                Log.d(TAG, "User Name : " + user.displayName)
                Log.d(TAG, "User Email : " + user.email)
                if (userId != null && PreferenceUtils.saveUserId(applicationContext, userId)) {
                    PreferenceUtils.saveUserEmail(applicationContext, user.email)
                    val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                showProgress(false)
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out")
            }
        }
    }

    // perform login operation
    private fun signIn(email: String?, password: String?) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }
        showProgress(true)
        clearCredentials()
        mAuth.signInWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(this) { task ->
                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful)

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful) {
                        Log.e(TAG, "signInWithEmail:failed", task.exception)
                        val snackbar = Snackbar.make(mLoginFormView, R.string.auth_failed,
                                Snackbar.LENGTH_INDEFINITE)
                        snackbar.show()
                        snackbar.setAction(R.string.cancel) { snackbar.dismiss() }
                        showProgress(false)
                    }
                }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private fun showProgress(show: Boolean) {
        mProgressView.visibility = if (show) View.VISIBLE else View.GONE
        mLoginFormView.setVisibility(if (show) View.GONE else View.VISIBLE)
    }

    override fun onClick(v: View?) {
        val clickId = v?.getId()
        when (clickId) {
            R.id.tv_forget_password -> {
                CommonUtils.hideSoftKeyboard(this@LoginActivity, v, 0)
                val intent = Intent(this@LoginActivity, ForgetPasswordActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_email_sign_in -> {
                CommonUtils.hideSoftKeyboard(this@LoginActivity, v, 0)
                signIn(mEmailView.text.toString(), mPasswordView.text.toString())
            }
        }
    }

    companion object {
        private val TAG: String? = "LoginActivityTag"
    }
}