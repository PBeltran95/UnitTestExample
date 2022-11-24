package com.flknlabs.unittestingexample.Model


import android.content.SharedPreferences
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.kotlin.mock
import java.util.*
import org.hamcrest.CoreMatchers.`is` as itIs
import org.mockito.Mockito.`when` as whenHappens


class SharedPreferencesHelperTest {
    private val TEST_NAME = "Test name"
    private val TEST_EMAIL = "test@email.com"
    private val TEST_DATE_OF_BIRTH: Calendar = Calendar.getInstance()

    private lateinit var mSharedPreferenceEntry: SharedPreferenceEntry

    private lateinit var mMockSharedPreferencesHelper: SharedPreferencesHelper

    private lateinit var mMockBrokenSharedPreferencesHelper: SharedPreferencesHelper

    @Mock
    private lateinit var mMockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mMockBrokenSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mMockEditor: SharedPreferences.Editor

    @Mock
    private lateinit var mMockBrokenEditor: SharedPreferences.Editor

    @Before
    fun initMocks() {
        mMockSharedPreferences = mock()
        mMockEditor = mock()
        mMockBrokenEditor = mock()
        mMockBrokenSharedPreferences = mock()
        mSharedPreferenceEntry = SharedPreferenceEntry(
            TEST_NAME, TEST_DATE_OF_BIRTH,
            TEST_EMAIL
        )

        mMockSharedPreferencesHelper = createMockSharedPreference()

        mMockBrokenSharedPreferencesHelper = createBrokenMockSharedPreference()
    }

    @After
    fun freeMocks() {
//        Mockito.clearInvocations(mSharedPreferenceEntry,
//                mMockSharedPreferencesHelper,
//                mMockBrokenSharedPreferencesHelper,
//                mMockSharedPreferences,
//                mMockBrokenSharedPreferences,
//                mMockEditor,
//                mMockBrokenEditor)
    }

    @Test
    fun sharedPreferencesHelper_SaveAndReadPersonalInformation() {
        val success = mMockSharedPreferencesHelper.savePersonalInfo(mSharedPreferenceEntry)
        assertThat(
            "Checking that SharedPreferenceEntry.save... returns true",
            success, itIs(true)
        )

        val savedSharedPreferenceEntry = mMockSharedPreferencesHelper.personalInfo
        assertThat(
            "Checking that SharedPreferenceEntry.name has been persisted and read correctly",
            mSharedPreferenceEntry.name,
            itIs(equalTo(savedSharedPreferenceEntry.name))
        )
        assertThat(
            "Checking that SharedPreferenceEntry.dateOfBirth has been persisted and read "
                    + "correctly",
            mSharedPreferenceEntry.dateOfBirth,
            itIs(equalTo(savedSharedPreferenceEntry.dateOfBirth))
        )
        assertThat(
            "Checking that SharedPreferenceEntry.email has been persisted and read "
                    + "correctly",
            mSharedPreferenceEntry.email,
            itIs(equalTo(savedSharedPreferenceEntry.email))
        )
    }

    @Test
    fun sharedPreferencesHelper_SavePersonalInformationFailed_ReturnsFalse() {
        val success =
            mMockBrokenSharedPreferencesHelper.savePersonalInfo(mSharedPreferenceEntry)
        assertThat(
            "Makes sure writing to a broken SharedPreferencesHelper returns false", success,
            itIs(false)
        )
    }

    /**
     * Creates a mocked SharedPreferences.
     */
    private fun createMockSharedPreference(): SharedPreferencesHelper {
        whenHappens(
            mMockSharedPreferences.getString(
                eq(KEY_NAME),
                anyString()
            )
        )
            .thenReturn(mSharedPreferenceEntry.name)
        whenHappens(
            mMockSharedPreferences.getString(
                eq(KEY_EMAIL),
                anyString()
            )
        )
            .thenReturn(mSharedPreferenceEntry.email)
        whenHappens (
            mMockSharedPreferences.getLong(
                eq(KEY_DOB),
                anyLong()
            )
        ).thenReturn(mSharedPreferenceEntry.dateOfBirth.timeInMillis)

        // Mocking a successful commit.
        whenHappens(mMockEditor.commit()).thenReturn(true)

        // Return the MockEditor when requesting it.
        whenHappens(mMockSharedPreferences.edit()).thenReturn(mMockEditor)
        return SharedPreferencesHelper(mMockSharedPreferences)
    }

    /**
     * Creates a mocked SharedPreferences that fails when writing.
     */
    private fun createBrokenMockSharedPreference(): SharedPreferencesHelper {
        // Mocking a commit that fails.
        whenHappens(mMockBrokenEditor.commit()).thenReturn(false)

        // Return the broken MockEditor when requesting it.
        whenHappens(mMockBrokenSharedPreferences.edit()).thenReturn(mMockBrokenEditor)
        return SharedPreferencesHelper(mMockBrokenSharedPreferences)
    }
}