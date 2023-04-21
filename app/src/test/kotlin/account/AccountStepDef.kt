package account

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

    val accountLoginService = mockk<AccountLoginService>()

    @Given("ATM with with the following records")
    fun atm_with_with_the_following_records(dataTable: DataTable) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
        // Double, Byte, Short, Long, BigInteger or BigDecimal.
        //
        // For other transformations you can register a DataTableType.
        val res = dataTable.asMaps()
        val name = res[0].get("Name")
    }

    @When("An User try to log with account number {int} and PIN {int}")
    fun an_user_try_to_log_with_account_number_and_pin(number: String, pin: String) {
        accountLoginService.login(Account(number, pin))
    }

    @Then("User with account number {int} is logged")
    fun user_with_account_number_is_logged(int1: Int?) {
        // Write code here that turns the phrase above into concrete actions
        throw PendingException()
    }

}