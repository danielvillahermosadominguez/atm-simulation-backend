package atm.account.steps

import io.cucumber.datatable.DataTable
import io.cucumber.java.PendingException
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.mockk.mockk

data class Account(val accountNumber: String, val pin: String)

interface AccountLoginService {
    fun login(account: Account)
}

class AccountStepDef {

    val accountLoginService = mockk<AccountLoginService>( relaxed = true)
    var name = ""
    var pin = ""
    var balance = 0
    var accountNumber = ""
    var loginSuccessFull = false
    @Given("ATM with with the following records")
    fun atm_with_with_the_following_records(dataTable: DataTable) {
        val res = dataTable.asMaps()
        this.name = res[0].get("Name").toString()
        this.pin = res[0].get("PIN").toString()
        this.balance =  res[0].get("Balance").toString().toInt()
        this.accountNumber = res[0].get("Account Number").toString()
    }

    @When("An User try to log with account number {string} and PIN {string}")
    fun an_user_try_to_log_with_account_number_and_pin(number: String, pin: String) {
        accountLoginService.login(Account(number, pin))
        loginSuccessFull = true
    }

    @Then("User with account number {int} is logged")
    fun user_with_account_number_is_logged(int1: Int?) {
        assert(loginSuccessFull)
    }

}