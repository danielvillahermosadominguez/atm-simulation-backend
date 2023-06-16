package atm.account.infrastructure

import atm.account.domain.Account
import atm.account.domain.AccountRepository

class MemoryAccountRepository : AccountRepository {
    private var accounts = emptyList<Account>()
    override fun save(account: Account) {
        this.accounts += account
    }

    override fun findById(id: String): Account? {
       return this.accounts.find { it.accountNumber == id }
    }
}