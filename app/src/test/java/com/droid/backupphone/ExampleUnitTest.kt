package com.droid.backupphone

import com.droid.backupphone.helper.LoginHelper
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    /**
     * Test to check password for invalid input.
     */
    @Test
    fun testValidatePasswordForInvalidCase() {
        Assert.assertFalse(LoginHelper.isPasswordValid(""))
        Assert.assertFalse(LoginHelper.isPasswordValid("a"))
        Assert.assertFalse(LoginHelper.isPasswordValid("ab"))
        Assert.assertFalse(LoginHelper.isPasswordValid("abc"))
        Assert.assertFalse(LoginHelper.isPasswordValid("abcd"))
    }

    /**
     * Test to check password for valid input.
     */
    @Test
    fun testValidatePasswordForValidCase() {
        Assert.assertTrue(LoginHelper.isPasswordValid("abcde"))
        Assert.assertTrue(LoginHelper.isPasswordValid("abcdef"))
        Assert.assertTrue(LoginHelper.isPasswordValid("@#$%^&*()"))
        Assert.assertTrue(LoginHelper.isPasswordValid("1234567"))
    }

    /**
     * Test to check email for invalid input.
     */
    @Test
    fun testValidateEmailForInvalidCase() {
        Assert.assertFalse(LoginHelper.isEmailValid(""))
        Assert.assertFalse(LoginHelper.isEmailValid("abc.g.com"))
    }

    /**
     * Test to check email for valid input.
     */
    @Test
    fun testValidateEmailForValidCase() {
        Assert.assertTrue(LoginHelper.isEmailValid("abc@gmail.com"))
        Assert.assertTrue(LoginHelper.isEmailValid("abc.efg@gmail.com"))
        Assert.assertTrue(LoginHelper.isEmailValid("@"))
        Assert.assertTrue(LoginHelper.isEmailValid("@@"))
    }
}