package atm.account.unit

import atm.account.Validator
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec

class ValidatorTest: StringSpec( {

    "Should have a size of 6 chars" {
        val validator = Validator()
        validator.validate("123456") shouldBeRight Unit
    }

    "Should return an error if we have more than 6 chars" {
        val validator = Validator()
        validator.validate("1234565555555") shouldBeLeft "Account Number should have 6 digits length for invalid Account Number"
    }

    "Should return an error if we have less than 6 chars" {
        val validator = Validator()
        validator.validate("1234") shouldBeLeft "Account Number should have 6 digits length for invalid Account Number"
    }

    "Should return an error if we have an empty input" {
        val validator = Validator()
        validator.validate("") shouldBeLeft "Account Number should have 6 digits length for invalid Account Number"
    }

    "Should return an error if we have one character which is not a digit" {
        val validator = Validator()
        validator.validate("a") shouldBeLeft "Account Number should only contains numbers for invalid Account Number"
    }
})