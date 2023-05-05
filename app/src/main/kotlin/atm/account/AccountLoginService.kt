package atm.account

data class Account(val accountNumber: String, val pin: String)
class AccountLoginService {
    fun login(account: Account): Boolean = true
    fun addAccount(name: String, pin: String, balance: Int, accountNumber: String): Unit {}
}