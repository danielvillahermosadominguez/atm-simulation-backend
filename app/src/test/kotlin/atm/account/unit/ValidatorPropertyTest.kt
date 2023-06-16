package atm.account.unit

import atm.account.domain.Validator
import atm.account.domain.ValidatorErrors
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.core.spec.style.StringSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll
import io.kotest.property.forAll


class ValidatorPropertyTest : StringSpec({
    lateinit var validator: Validator

    beforeEach {
        val translator:  (ValidatorErrors)->String = {it.name}
        validator = Validator(translator)
    }

    "Should have a size of 6 chars" {
        forAll(Arb.positiveInt(999999).map { it.toString().padStart(6, '0') }) {
            validator.validate(it).isRight()
        }
    }

    "Should return an error if we have more than 6 chars" {
        checkAll(Arb.int(0, Int.MAX_VALUE).map { it.toString().padStart(7, '0') }) {
            validator.validate(it) shouldBeLeft ValidatorErrors.SIX_DIGITS.name
        }
    }

    "Should return an error if we have less than 6 chars" {
        checkAll(Arb.int(0, 99999).map { it.toString() }) {
            validator.validate(it) shouldBeLeft ValidatorErrors.SIX_DIGITS.name
        }
    }

    "Should return an error if we have an empty input" {
        validator.validate("") shouldBeLeft ValidatorErrors.SIX_DIGITS.name
    }

    "Should return an error if we have one character which is not a digit" {
        checkAll(Arb.string(6).filter { it.any { ch -> !ch.isDigit() } }) {
            validator.validate(it) shouldBeLeft ValidatorErrors.ALL_DIGITS.name
        }
    }
})