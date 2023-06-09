package atm.account.unit

import arrow.core.Either
import atm.account.domain.Account
import atm.account.AccountLoginService
import atm.account.domain.AccountRepository
import atm.account.LoginAccount
import atm.account.domain.Validator
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
    lateinit var validatorAccount: Validator
    lateinit var validatorPin: Validator

    beforeEach {
        validatorAccount = mockk(relaxed = true)
        every { validatorAccount.validate(any()) } returns Either.Right(Unit)
        validatorPin = mockk(relaxed = true)
        every { validatorPin.validate(any()) } returns Either.Right(Unit)
        repository = mockk(relaxed = true)
        accountService = AccountLoginService(repository, validatorAccount, validatorPin)
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
        assertEquals("Invalid Account Number/PIN", result.leftOrNull())
    }

    "should not login in a non existing account" {

        every { repository.findById("non existing account" ) } returns null

        val loginAccount = LoginAccount("non existing account", "wrong pin")

        val result = accountService.login(loginAccount)

        assertFalse(result.isRight())
        assertEquals("Invalid Account Number/PIN", result.leftOrNull())
    }

    "should return a successful value when validation is ok" {
        every { validatorAccount.validate(any())} returns Either.Right(Unit)
        every { repository.findById("112233") } returns Account("John Doe", "012108", 100, "112233")
        val loginAccount = LoginAccount("112233", "012108")

        val result = accountService.login(loginAccount)

        assertTrue(result.isRight())
        verify { validatorAccount.validate("112233") }
    }

    "should return a error value when validation fails" {
        val message = "A message"
        every { validatorAccount.validate("112233")} returns Either.Left(message)
        every { repository.findById("112233") } returns Account("John Doe", "012108", 100, "112233")

        val loginAccount = LoginAccount("112233", "012108")

        val result = accountService.login(loginAccount)

        assertTrue(result.isLeft())
        assertEquals(message, result.leftOrNull())
        verify(exactly = 0) { repository.findById(any()) }
        verify { validatorAccount.validate("112233") }
    }

    "should return a error value when pin validation fails" {
        val noCorrectPIN = "01210"
        val accountNumber = "112233"
        val message = "PIN should have 6 digits length for invalid PIN"
        every { validatorPin.validate(noCorrectPIN)} returns Either.Left(message)
        every { validatorAccount.validate(accountNumber)} returns Either.Right(Unit)
        every { repository.findById("112245") } returns Account("John Doe", "CORRECT_PASSWORD", 100, "112245")

        val loginAccount = LoginAccount(accountNumber, noCorrectPIN)

        val result = accountService.login(loginAccount)

        assertTrue(result.isLeft())
        assertEquals(message, result.leftOrNull())
        verify(exactly = 0) { repository.findById(any()) }
        verify { validatorPin.validate(noCorrectPIN) }
    }
})