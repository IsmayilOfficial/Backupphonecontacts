package com.example.backupdata.activity.login

import android.app.AlertDialog
import android.os.Bundle

import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.appcompat.widget.Toolbar

import com.example.backupdata.util.CommonUtils
import com.example.backupdata.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import kotlinx.android.synthetic.main.activity_sign_up_view.*

class SignUpActvity : BaseSignInSignUpActivity() {
    private lateinit var mSignUpFormView: View
    private val mSignUpBtnClickListener: View.OnClickListener? = View.OnClickListener { view ->
        CommonUtils.hideSoftKeyboard(this@SignUpActvity, view, 0)
        createAccount(mEmailView.text.toString(), mPasswordView.text.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_view)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        // Set up the login form.
        mEmailView = findViewById(R.id.actv_email) as AutoCompleteTextView
        mPasswordView = findViewById(R.id.et_password) as EditText
        btn_create_account.setOnClickListener(mSignUpBtnClickListener)
        mSignUpFormView = findViewById(R.id.view_signup_parent)
        mProgressView = findViewById(R.id.view_signup_progress)
        mAuth = FirebaseAuth.getInstance()
        setAuthListener()
    }

    // set auth listener to listen if signIn or signUp operation get successful
    private fun setAuthListener() {
        mAuthListener = AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // User is signed in
                val userId = user.uid
                Log.d(TAG, "onAuthStateChanged:signed_up:")
                Log.d(TAG, "User Id : $userId")
                Log.d(TAG, "User Name : " + user.displayName)
                Log.d(TAG, "User Email : " + user.email)
                CommonUtils.signOutFromApp() // after sign up perform sign out operation is mandatory
                showProgress(false)
                showSignUpsuccessDialog()
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out")
            }
        }
    }

    // Perform signUp operation
    private fun createAccount(email: String?, password: String?) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }
        showProgress(true)
        clearCredentials()
        mAuth.createUserWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(this) { task ->
                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful)

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful) {
                        Log.e(TAG, "createUserWithEmail:failed", task.exception)
                        val snackbar = Snackbar.make(mSignUpFormView, R.string.signup_failed,
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
        mSignUpFormView.setVisibility(if (show) View.GONE else View.VISIBLE)
    }

    // show sign up successful dialog
    private fun showSignUpsuccessDialog() {
        val signupAlertDialog = AlertDialog.Builder(this@SignUpActvity)
        signupAlertDialog.setMessage(R.string.signup_success)
        signupAlertDialog.setCancelable(false)
        signupAlertDialog.setPositiveButton(R.string.ok) { dialog, which ->
            CommonUtils.signOutFromApp()
            finish()
        }
        signupAlertDialog.create().show()
    }

    companion object {
        private val TAG: String? = "SignUpActvity"
    }
}