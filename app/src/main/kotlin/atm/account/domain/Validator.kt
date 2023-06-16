package atm.account.domain

import arrow.core.Either


class Validator(val translator: (ValidatorErrors) -> String) {

    fun validate(account: String): Either<String, Unit> {
        if (!account.all { char -> char.isDigit() }) {
            return Either.Left(translator(ValidatorErrors.ALL_DIGITS))
        }

        if (account.length !== 6) {
            return Either.Left(translator(ValidatorErrors.SIX_DIGITS))
        }

        return Either.Right(Unit)
    }
}
