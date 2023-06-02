package atm.account.unit

import atm.account.Validator
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.core.spec.style.StringSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll
import io.kotest.property.forAll


class Validator2Test : StringSpec({
    "Should have a size of 6 chars" {
        val validator = Validator()
        forAll(Arb.positiveInt(999999).map { it.toString().padStart(6, '0') }) {
            validator.validate(it).isRight()
        }
    }

    "Should return an error if we have more than 6 chars" {
        val validator = Validator()
        checkAll(Arb.int(0, Int.MAX_VALUE).map { it.toString().padStart(7, '0') }) {
            validator.validate(it) shouldBeLeft "Account Number should have 6 digits length for invalid Account Number"
        }
    }

    "Should return an error if we have less than 6 chars" {
        val validator = Validator()
        checkAll(Arb.int(0, 99999).map { it.toString() }) {
            validator.validate(it) shouldBeLeft "Account Number should have 6 digits length for invalid Account Number"
        }
    }

    "Should return an error if we have an empty input" {
        val validator = Validator()
        validator.validate("") shouldBeLeft "Account Number should have 6 digits length for invalid Account Number"
    }

    "Should return an error if we have one character which is not a digit" {
        val validator = Validator()
        checkAll(Arb.string(6).filter { it.any { ch -> !ch.isDigit() } }) {
            validator.validate(it) shouldBeLeft "Account Number should only contains numbers for invalid Account Number"
        }
    }
})