package atm.account

import arrow.core.Either

class Validator {
    fun validate(account: String) : Either<String, Unit> {
        //if(account.length < 6) return Either.Left("Account Number should have 6 digits length for invalid Account Number")
        TODO()
    }

}
