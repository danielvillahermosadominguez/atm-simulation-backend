package atm.account.integration

import io.kotest.core.spec.style.StringSpec

class ConsoleTest : StringSpec( {

    "should show the welcome screen asking for the account number" {
        val console = Console()

        console.run()

        // Capturar la consola y mirar que cumple esto : "Enter Account Number:"
    }
} )

class Console {
    fun run() {

    }
}
