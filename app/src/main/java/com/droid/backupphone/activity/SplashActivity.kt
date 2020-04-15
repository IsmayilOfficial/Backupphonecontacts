package com.droid.backupphone.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler

import com.droid.backupphone.R
import com.droid.backupphone.activity.login.LoginActivity
import com.droid.backupphone.util.CommonUtils


/**
 * The app splash screen.
 */
class SplashActivity : BaseActivity() {
    private var mSplashHandler: Handler? = null
    private val mRunnable: Runnable? = Runnable {
        if (CommonUtils.isUserLogin(applicationContext)) {
            startNextActivity(DashboardActivity::class.java)
        } else {
            startNextActivity(LoginActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delay in splash
        val splashDelay = getString(R.string.splash_time_delay).toLong()

        // Delay the closing of splash screen
        mSplashHandler = Handler()
        mSplashHandler!!.postDelayed(mRunnable, splashDelay)
    }

    /*
     * The method to push next activity to the top of the activity stack clearing other activities at
     * its top.
     */
    private fun startNextActivity(cls: Class<*>?) {
        mSplashHandler!!.removeCallbacks(mRunnable)
        val intentNextActivity = Intent(this@SplashActivity, cls)
        intentNextActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intentNextActivity)
        finish()
    }

    override fun onBackPressed() {
        // Splash screen need not be destroyed using this key
    }
}