package atm.account.steps

import atm.account.Account
import atm.account.AccountLoginService
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class AccountStepDef {

    private var loginOk: Boolean = false
    val accountLoginService = AccountLoginService()

    @Given("ATM with with the following records")
    fun atm_with_with_the_following_records(dataTable: DataTable) {
        val res = dataTable.asMaps()
        val name = res[0].get("Name").toString()
        val pin = res[0].get("PIN").toString()
        val balance = res[0].get("Balance").toString().toInt()
        val accountNumber = res[0].get("Account Number").toString()
        accountLoginService.addAccount(name, pin, balance, accountNumber)
    }

    @When("An User try to log with account number {string} and PIN {string}")
    fun an_user_try_to_log_with_account_number_and_pin(number: String, pin: String) {
        this.loginOk = accountLoginService.login(Account(number, pin))
    }

    @Then("User with account number {int} is logged")
    fun user_with_account_number_is_logged(int1: Int?) {
        assert(this.loginOk)
    }

}