package atm.account.unit

import atm.account.Account
import atm.account.AccountLoginService

import io.kotest.core.spec.style.FreeSpec

class AccountLoginServiceTest : FreeSpec({
    lateinit var accountService: AccountLoginService;
    beforeEach {
        accountService = AccountLoginService()
        accountService.addAccount("John Doe","012108", 100,"112233")
    }

    "should login in an existing account" {
        val account = Account("112233","012108")

        val result = accountService.login(account)

        assert(result)
    }

})