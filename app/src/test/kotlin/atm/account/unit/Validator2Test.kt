package atm.account.unit

import atm.account.Validator
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.string.shouldHaveLength
import io.kotest.property.Arb
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import io.kotest.property.forAll


class Validator2Test: StringSpec({
    "Should have a size of 6 chars" {
        val validator = Validator()

        /*forAll(Arb.string(6..6).filter { it.all { char -> char.isDigit() } }) { account:String ->
            validator.validate(account).isRight()
        }*/
    }
})