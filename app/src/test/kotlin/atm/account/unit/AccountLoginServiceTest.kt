package atm.account.unit

import arrow.core.Either
import atm.account.Account
import atm.account.AccountLoginService
import atm.account.AccountRepository
import atm.account.LoginAccount
import atm.account.Validator
import io.kotest.core.spec.style.FreeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue



class AccountLoginServiceTest : FreeSpec({

    lateinit var repository: AccountRepository
    lateinit var accountService: AccountLoginService
    lateinit var validator: Validator

    beforeEach {
        validator = mockk(relaxed = true)
        every { validator.validate(any()) } returns Either.Right(Unit)
        repository = mockk(relaxed = true)
        accountService = AccountLoginService(repository, validator)
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

    "should return a successful value when validation is ok" {
        every { validator.validate(any())} returns Either.Right(Unit)
        every { repository.findById("112233") } returns Account("John Doe", "012108", 100, "112233")
        val loginAccount = LoginAccount("112233", "012108")

        val result = accountService.login(loginAccount)

        assertTrue(result.isRight())
        verify { validator.validate("112233") }
    }

    "should return a error value when validation fails" {
        val message = "A message"
        every { validator.validate(any())} returns Either.Left(message)
        every { repository.findById("112233") } returns Account("John Doe", "012108", 100, "112233")

        val loginAccount = LoginAccount("112233", "012108")

        val result = accountService.login(loginAccount)

        assertTrue(result.isLeft())
        assertEquals(message, result.leftOrNull())
        verify(exactly = 0) { repository.findById(any()) }
        verify { validator.validate("112233") }
    }

    "should return a error value when pin validation fails" {
        val noCorrectPIN = "01210"
        val message = "PIN should have 6 digits length for invalid PIN"
        every { validator.validate(any())} returns Either.Left(message)
        every { repository.findById("112245") } returns Account("John Doe", "CORRECT_PASSWORD", 100, "112245")

        val loginAccount = LoginAccount("112233", noCorrectPIN)

        val result = accountService.login(loginAccount)

        assertTrue(result.isLeft())
        assertEquals(message, result.leftOrNull())
        verify { validator.validate("01210") }
    }
})