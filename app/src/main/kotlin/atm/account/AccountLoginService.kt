package atm.account

import arrow.core.Either
import arrow.core.flatMap

data class LoginAccount(val accountNumber: String, val pin: String)

class AccountLoginService constructor(
        private val repository: AccountRepository = AccountRepository.apply(),
        private val validatorAccount: Validator = Validator { translateAccountMessage(it) },
        private val validatorPin: Validator = Validator { translatePINMessage(it) }) {
    fun login(loginAccount: LoginAccount): Either<String, Unit> {
        return validatorAccount.validate(loginAccount.accountNumber)
                .flatMap { validatorPin.validate(loginAccount.pin) }
                .flatMap { loginService(loginAccount) }
    }

    private fun loginService(loginAccount: LoginAccount): Either<String, Unit> {
        val account = repository.findById(loginAccount.accountNumber)
        if (account === null) {
            return Either.Left("Invalid Account Number/PIN")
        }
        if (account.pin == loginAccount.pin) return Either.Right(Unit)
        return Either.Left("Invalid Account Number/PIN")
    }

    fun addAccount(name: String, pin: String, balance: Int, accountNumber: String): Unit {
        repository.save(Account(name, pin, balance, accountNumber))
    }
}