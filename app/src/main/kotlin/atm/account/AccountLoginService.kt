package atm.account

data class LoginAccount(val accountNumber: String, val pin: String)
class AccountLoginService(val repository: AccountRepository = AccountRepository.apply()) {
    fun login(loginAccount: LoginAccount): Boolean {
        val account = repository.findById(loginAccount.accountNumber)
        if(account === null) {
            return false
        }
        return account.pin == loginAccount.pin
    }

    fun addAccount(name: String, pin: String, balance: Int, accountNumber: String): Unit {
        repository.save(Account(name, pin, balance, accountNumber))
    }
}