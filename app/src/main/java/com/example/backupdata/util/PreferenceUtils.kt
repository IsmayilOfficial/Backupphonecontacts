package com.example.backupdata.util

import android.content.Context

import com.example.backupdata.common.CommonConstants



object PreferenceUtils {
    private fun writeData(context: Context?, userId: String?, prefKey: String?): Boolean {
        val prefLocaleFile = context?.getSharedPreferences(
            CommonConstants.PREF_LOCALE_FILE,
                Context.MODE_PRIVATE)
        val editor = prefLocaleFile?.edit()
        if (editor != null) {
            editor.putString(prefKey, userId)
            return editor.commit()
        }
        return false
    }

    private fun getData(context: Context?, prefKey: String?): String? {
        val prefLocaleFile = context?.getSharedPreferences(
            CommonConstants.PREF_LOCALE_FILE,
                Context.MODE_PRIVATE)
        return prefLocaleFile?.getString(prefKey, null)
    }

    /**
     * Save user id in shared preference
     *
     * @param context the context
     * @param userId  the user id to save
     * @return returns true if user id saved successfully
     */
    fun saveUserId(context: Context?, userId: String?): Boolean {
        return writeData(context, userId, CommonConstants.PREF_USER_ID)
    }

    /**
     * Get user id saved in shared preference
     *
     * @param context the context
     * @return returns the user id
     */
    fun getUserId(context: Context?): String? {
        return getData(context, CommonConstants.PREF_USER_ID)
    }

    /**
     * Delete the user id from shared preference
     *
     * @param context the context
     * @return returns true if user id deleted successfully
     */
    fun removeUserId(context: Context?): Boolean {
        return saveUserId(context, null)
    }

    /**
     * Save user email in shared preference
     *
     * @param context   the context
     * @param userEmail the user id to save
     * @return returns true if user email saved successfully
     */
    fun saveUserEmail(context: Context?, userEmail: String?): Boolean {
        return writeData(context, userEmail, CommonConstants.PREF_USER_EMAIL)
    }

    /**
     * Get user id saved in shared preference
     *
     * @param context the context
     * @return returns the user id
     */
    fun getUserEmail(context: Context?): String? {
        return getData(context, CommonConstants.PREF_USER_EMAIL)
    }

    /**
     * Delete the user id from shared preference
     *
     * @param context the context
     * @return returns true if user id deleted successfully
     */
    fun removeUserEmail(context: Context?): Boolean {
        return saveUserEmail(context, null)
    }
}