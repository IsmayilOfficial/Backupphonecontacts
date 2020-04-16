package com.example.backupdata.util

import android.app.Activity
import android.content.Context
import android.content.Intent

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.example.backupdata.activity.login.LoginActivity
import com.example.backupdata.activity.login.SignUpActvity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase



object CommonUtils {

    fun hideSoftKeyboard(context: Context?, view: View?, flags: Int) {
        val inputManager = context?.getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.windowToken, flags)
        } else {
            inputManager.hideSoftInputFromWindow(null, flags)
        }
    }


    fun concatenateString(vararg args: Any?): String? {
        val sbConcatenated = StringBuilder()
        for (arg in args) {
            sbConcatenated.append(arg)
        }
        return sbConcatenated.toString()
    }


    fun signOutFromApp() {
        FirebaseAuth.getInstance().signOut()
    }


    fun isCollectionNullOrEmpty(collection: MutableCollection<*>?): Boolean {
        return collection == null || collection.isEmpty()
    }


    fun getDatabaseReference(): DatabaseReference? {
        return FirebaseDatabase.getInstance().reference
    }


    fun getContactEndPoint(): DatabaseReference? {
        return getDatabaseReference()?.child("contacts")
    }

    fun getSmsEndPoint(): DatabaseReference? {
        return getDatabaseReference()?.child("sms")
    }


    fun getUserId(activity: Activity?, applicationContext: Context?): String? {
        val userId = PreferenceUtils.getUserId(applicationContext)
        if (userId == null) {
            // logout from app and move to login screen
            logOut(activity, applicationContext)
        }
        return userId
    }

    fun logOut(activity: Activity?, applicationContext: Context?) {
        PreferenceUtils.removeUserId(applicationContext)
        PreferenceUtils.removeUserEmail(applicationContext)
        val intentNextActivity = Intent(activity, LoginActivity::class.java)
        intentNextActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        activity?.startActivity(intentNextActivity)
    }


    fun linkifyCallTextView(context: Context?, tvLinkify: TextView?, sequence: Spanned?) {
        var clickableSpan: ClickableSpan
        var start: Int
        var end: Int
        var flags: Int
        val strBuilder = SpannableStringBuilder(sequence)
        val underlines = strBuilder.getSpans(0, sequence!!.length, UnderlineSpan::class.java)
        for (span in underlines) {
            start = strBuilder.getSpanStart(span)
            end = strBuilder.getSpanEnd(span)
            flags = strBuilder.getSpanFlags(span)
            clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    val callIntent = Intent(context, SignUpActvity::class.java)
                    context?.startActivity(callIntent)
                }
            }
            strBuilder.setSpan(clickableSpan, start, end, flags)
        }
        tvLinkify?.setText(strBuilder)
        tvLinkify?.setMovementMethod(LinkMovementMethod.getInstance())
    }


    fun isUserLogin(context: Context?): Boolean {
        return PreferenceUtils.getUserId(context) != null
    }
}