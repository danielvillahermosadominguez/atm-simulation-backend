package atm.account.unit

import atm.account.Validator
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec

class ValidatorTest: StringSpec( {
    "Should have 6 digits" {
        val validator = Validator()
        validator.validate("123456") shouldBeRight Unit
    }
})