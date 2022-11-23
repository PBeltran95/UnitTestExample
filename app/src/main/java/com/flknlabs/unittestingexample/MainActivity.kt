package com.flknlabs.unittestingexample

import android.app.Activity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.flknlabs.unittestingexample.Model.SharedPreferenceEntry
import com.flknlabs.unittestingexample.Model.SharedPreferencesHelper
import com.flknlabs.unittestingexample.View.EmailValidator
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : Activity() {

    private val mSharedPreferencesHelper by lazy {
        SharedPreferencesHelper(PreferenceManager.getDefaultSharedPreferences(this))
    }

    private val mEmailValidator by lazy {
        EmailValidator()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailInput.addTextChangedListener(mEmailValidator)

        populateUi()
    }

    /**
     * Initialize all fields from the personal info saved in the SharedPreferences.
     */
    private fun populateUi() {
        val sharedPreferenceEntry = mSharedPreferencesHelper.personalInfo
        userNameInput.setText(sharedPreferenceEntry.name)
        val dateOfBirth: Calendar = sharedPreferenceEntry.dateOfBirth
        dateOfBirthInput.init(
            dateOfBirth.get(Calendar.YEAR), dateOfBirth.get(Calendar.MONTH),
            dateOfBirth.get(Calendar.DAY_OF_MONTH), null
        )
        emailInput.setText(sharedPreferenceEntry.email)
    }

    fun onSaveClick(view: View?) {
        if (!mEmailValidator.isValid) {
            emailInput.error = "Invalid email"
            Log.w(TAG, "Not saving personal information: Invalid email")
            return
        }
        val name = userNameInput.text.toString()
        val dateOfBirth: Calendar = Calendar.getInstance()
        dateOfBirth.set(dateOfBirthInput.year, dateOfBirthInput.month, dateOfBirthInput.dayOfMonth)
        val email = emailInput.text.toString()

        val sharedPreferenceEntry = SharedPreferenceEntry(name, dateOfBirth, email)

        val isSuccess: Boolean = mSharedPreferencesHelper.savePersonalInfo(sharedPreferenceEntry)
        if (isSuccess) {
            Toast.makeText(this, "Personal information saved", Toast.LENGTH_LONG).show()
            Log.i(TAG, "Personal information saved")
        } else {
            Log.e(TAG, "Failed to write personal information to SharedPreferences")
        }
    }

    fun onRevertClick(view: View?) {
        populateUi()
        Toast.makeText(this, "Personal information reverted", Toast.LENGTH_LONG).show()
        Log.i(TAG, "Personal information reverted")
    }

    companion object {
        // Logger for this class.
        private const val TAG = "MainActivity"
    }
}