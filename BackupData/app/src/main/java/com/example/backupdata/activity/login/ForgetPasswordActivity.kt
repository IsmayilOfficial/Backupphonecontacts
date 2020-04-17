package com.example.backupdata.activity.login

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast

import com.example.backupdata.helper.LoginHelper
import com.example.backupdata.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forget_password_view.*


class ForgetPasswordActivity : BaseSignInSignUpActivity() {
    private var mLoginFormView: View? = null
    private val mResetPassword: Button? = null
    private val mForgetPassword: View.OnClickListener? = View.OnClickListener { view ->
        com.example.backupdata.util.CommonUtils.hideSoftKeyboard(this@ForgetPasswordActivity, view, 0)
        forgetpassword(mEmailView.text.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password_view)
        mEmailView = findViewById(R.id.actv_email) as AutoCompleteTextView
        mLoginFormView = findViewById(R.id.view_forget_parent)
        mProgressView = findViewById(R.id.view_reset_progress)
       btn_reset_password.setOnClickListener(mForgetPassword)
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName("Jane Q. User")
//                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
//                .build();
    }

    protected fun validateEmail(): Boolean {

        // Reset errors.
        mEmailView.error = null


        // Store values at the time of the login attempt.
        val email = mEmailView.text.toString()
        var valid = true
        var focusView: View? = null

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.error = getString(R.string.error_field_required)
            focusView = mEmailView
            valid = false
        } else if (!LoginHelper.isEmailValid(email)) {
            mEmailView.error = getString(R.string.error_invalid_email)
            focusView = mEmailView
            valid = false
        }
        if (!valid) {
            focusView?.requestFocus()
        }
        return valid
    }

    private fun forgetpassword(email: String?) {
        Log.d(TAG, "signIn:$email")
        if (!validateEmail()) {
            return
        }
        mAuth.sendPasswordResetEmail(email!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@ForgetPasswordActivity, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@ForgetPasswordActivity, "Failed to send reset email!", Toast.LENGTH_SHORT).show()
                    }
                    showProgress(false)
                }
        showProgress(true)
    }

    private fun showProgress(show: Boolean) {
        mProgressView.visibility = if (show) View.VISIBLE else View.GONE
        mLoginFormView?.setVisibility(if (show) View.GONE else View.VISIBLE)
    }

    companion object {
        private val TAG = ForgetPasswordActivity::class.java.name
    }
}