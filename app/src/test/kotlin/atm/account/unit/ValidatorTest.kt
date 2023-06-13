package atm.account.unit

import atm.account.Validator
import atm.account.ValidatorErrors
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec

class ValidatorTest: StringSpec( {

    lateinit var validator: Validator

    beforeEach {
        val translator:  (ValidatorErrors)->String = {it.name}
        validator = Validator(translator)
    }

    "Should have a size of 6 chars" {
        validator.validate("123456") shouldBeRight Unit
    }

    "Should return an error if we have more than 6 chars" {
        validator.validate("1234565555555") shouldBeLeft ValidatorErrors.SIX_DIGITS.name
    }

    "Should return an error if we have less than 6 chars" {
        validator.validate("1234") shouldBeLeft ValidatorErrors.SIX_DIGITS.name
    }

    "Should return an error if we have an empty input" {
        validator.validate("") shouldBeLeft ValidatorErrors.SIX_DIGITS.name
    }

    "Should return an error if we have one character which is not a digit" {
        validator.validate("a") shouldBeLeft ValidatorErrors.ALL_DIGITS.name
    }
})