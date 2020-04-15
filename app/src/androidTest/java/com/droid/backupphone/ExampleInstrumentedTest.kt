package com.droid.backupphone

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.droid.backupphone.util.CommonUtils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        Assert.assertEquals("com.droid.backupphone", appContext.packageName)
    }

    @Test
    fun testUserNotLogin() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        Assert.assertFalse(CommonUtils.isUserLogin(appContext))
    }

    @Test
    fun testUserLogin() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        Assert.assertTrue(CommonUtils.isUserLogin(appContext))
    }
}