package atm.account.steps

import atm.account.Account
import atm.account.AccountLoginService
import atm.account.AccountRepository
import atm.account.LoginAccount
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlin.test.assertTrue

class AccountStepDef {

    private var loginOk: Boolean = false
    val accountLoginService = AccountLoginService(MemoryAccountRepository())

    @Given("ATM ready to be used")
    fun atm_ready_to_be_used() {
    }

    @Given("ATM with with the following records")
    fun atm_with_with_the_following_records(dataTable: DataTable) {
        val res = dataTable.asMaps()
        for (entry in res) {
            val name = entry.get("Name").toString()
            val pin = entry.get("PIN").toString()
            val balance = entry.get("Balance").toString().toInt()
            val accountNumber = entry.get("Account Number").toString()
            accountLoginService.addAccount(name, pin, balance, accountNumber)
        }
    }

    @When("An User try to log with account number {string} and PIN {string}")
    fun an_user_try_to_log_with_account_number_and_pin(number: String, pin: String) {
        this.loginOk = accountLoginService.login(LoginAccount(number, pin))
    }

    @Then("User with account number {int} is logged")
    fun user_with_account_number_is_logged(int1: Int?) {
        assertTrue(this.loginOk)
    }
    @Then("the user should see the message {string}")
    fun the_user_should_see_the_message(errorMessage: String?) {
    }
}

class MemoryAccountRepository : AccountRepository {
    private var accounts = emptyList<Account>()
    override fun save(account: Account) {
        this.accounts += account
    }

    override fun findById(id: String): Account? {
       return this.accounts.find { it.accountNumber == id }
    }
}
