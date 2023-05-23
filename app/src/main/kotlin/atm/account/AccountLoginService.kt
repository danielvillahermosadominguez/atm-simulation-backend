package atm.account

import arrow.core.Either
import arrow.core.right

data class LoginAccount(val accountNumber: String, val pin: String)

class AccountLoginService(val repository: AccountRepository = AccountRepository.apply(), val validator: Validator = Validator()) {
    fun login(loginAccount: LoginAccount): Either<String, Unit> {
        validator.validate(loginAccount.accountNumber)
        val account = repository.findById(loginAccount.accountNumber)
        if(account === null) {
            return Either.Left("Unimplemented (account null)")
        }
        if( account.pin == loginAccount.pin ) return Either.Right(Unit)
        return Either.Left("Unimplemented (pin not equal)")

    }

    fun addAccount(name: String, pin: String, balance: Int, accountNumber: String): Unit {
        repository.save(Account(name, pin, balance, accountNumber))
    }
}