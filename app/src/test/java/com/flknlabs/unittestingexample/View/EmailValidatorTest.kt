package com.flknlabs.unittestingexample.View

import com.flknlabs.unittestingexample.View.EmailValidator
import io.kotlintest.shouldBe
import org.junit.Assert
import org.junit.Test

class EmailValidatorTest {
    @Test
    fun emailValidator_CorrectEmailSimple_ReturnsTrue() {
        // Arrange
        val email = "name@email.com"

        // Act
        val validation = EmailValidator.isValidEmail(email)

        // Assertion
        Assert.assertTrue(validation)
    }

    @Test
    fun emailValidator_CorrectEmailSubDomain_ReturnsTrue() {
        EmailValidator.isValidEmail("name@email.co.uk") shouldBe true
    }

    @Test
    fun emailValidator_InvalidEmailNoTld_ReturnsFalse() {
        Assert.assertFalse(EmailValidator.isValidEmail("name@email"))
    }

    @Test
    fun emailValidator_InvalidEmailDoubleDot_ReturnsFalse() {
        //EmailValidator.isValidEmail("name@email..com") shouldNotBe true
        EmailValidator.isValidEmail("name@email..com") shouldBe false
    }

    @Test
    fun emailValidator_EmptyString_ReturnsFalse() {
        Assert.assertFalse(EmailValidator.isValidEmail(""))
    }

    @Test
    fun emailValidator_NullEmail_ReturnsFalse() {
        Assert.assertFalse(EmailValidator.isValidEmail(null))
    }

    @Test
    fun emailValidator_OnlyHat_ReturnsFalse() {
        Assert.assertFalse(EmailValidator.isValidEmail("@"))
    }
}