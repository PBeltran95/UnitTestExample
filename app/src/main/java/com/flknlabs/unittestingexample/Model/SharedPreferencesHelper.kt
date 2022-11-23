package com.flknlabs.unittestingexample.Model

import android.content.SharedPreferences
import java.util.*

const val KEY_NAME = "key_name"
const val KEY_DOB = "key_dob_millis"
const val KEY_EMAIL = "key_email"

class SharedPreferencesHelper(
    private val mSharedPreferences: SharedPreferences
) {
    fun savePersonalInfo(sharedPreferenceEntry: SharedPreferenceEntry): Boolean {
        val editor = mSharedPreferences.edit()
        editor.putString(KEY_NAME, sharedPreferenceEntry.name)
        editor.putLong(
            KEY_DOB,
            sharedPreferenceEntry.dateOfBirth.timeInMillis
        )
        editor.putString(KEY_EMAIL, sharedPreferenceEntry.email)
        return editor.commit()
    }

    val personalInfo: SharedPreferenceEntry
        get() {
            val name = mSharedPreferences.getString(KEY_NAME, "")
            val dobMillis = mSharedPreferences.getLong(
                KEY_DOB,
                Calendar.getInstance().timeInMillis
            )
            val dateOfBirth: Calendar = Calendar.getInstance()
            dateOfBirth.timeInMillis = dobMillis
            val email =
                mSharedPreferences.getString(KEY_EMAIL, "")
            return SharedPreferenceEntry(name!!, dateOfBirth, email!!)
        }
}