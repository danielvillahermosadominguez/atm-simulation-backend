package atm.account.unit

import atm.account.Account
import atm.account.AccountLoginService
import atm.account.AccountRepository
import atm.account.LoginAccount
import io.kotest.core.spec.style.FreeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AccountLoginServiceTest : FreeSpec({

    lateinit var repository: AccountRepository
    lateinit var accountService: AccountLoginService

    beforeEach {
        repository = mockk(relaxed = true)
        accountService = AccountLoginService(repository)
    }

    "should login in an existing account" {
        every { repository.findById("112233") } returns Account("John Doe", "012108", 100, "112233")

        val loginAccount = LoginAccount("112233", "012108")

        val result = accountService.login(loginAccount)

        assertTrue(result.isRight())
    }

    "addAccount should save in repository" {

        accountService.addAccount("John Doe", "012108", 100, "112233")

        verify { repository.save(Account("John Doe", "012108", 100, "112233")) }
    }

    "login should find account in repository" {

        val loginAccount = LoginAccount("112233", "012108")

        accountService.login(loginAccount)

        verify { repository.findById("112233") }

    }

    "should not login in an existing account with wrong pin" {
        every { repository.findById("112233") } returns Account("John Doe", "012108", 100, "112233")

        val loginAccount = LoginAccount("112233", "wrong pin")

        val result = accountService.login(loginAccount)

        assertFalse(result.isRight())
    }

    "should not login in a non existing account" {

        every { repository.findById("112233") } returns null

        val loginAccount = LoginAccount("non existing account", "wrong pin")

        val result = accountService.login(loginAccount)

        assertFalse(result.isRight())
    }

})