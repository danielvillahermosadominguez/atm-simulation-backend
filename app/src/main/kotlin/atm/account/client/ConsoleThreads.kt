package atm.account.client

import kotlin.concurrent.thread

interface ConsoleCallback {
    fun userInput(value: String)

}

enum class ConsoleState
{
    InputAccountNumber,
    InputAccountPin,
    TransactionScreen
}

class ConsoleThreads(val callback: ConsoleCallback) {
    private var isAlive = true
    private lateinit var thread: Thread
    private var state = ConsoleState.InputAccountNumber
    private var accountNumber: String? = null
    private var pin: String? = null
    fun run() {
        thread = thread(isDaemon = true) {
            while (isAlive) {
                val input = readLine()
                if (input !== null && !input.isNullOrBlank()) {
                    callback.userInput(input)
                    if (state == ConsoleState.InputAccountPin) {
                        state = ConsoleState.TransactionScreen
                        pin = input
                    }
                    if (state == ConsoleState.InputAccountNumber) {
                        state = ConsoleState.InputAccountPin
                        accountNumber = input
                    }

                    if (state == ConsoleState.InputAccountPin) {
                        println()
                        print("Enter PIN: ")
                    }
                    if (state == ConsoleState.TransactionScreen) {
                        println()
                        var output = "Account number $accountNumber, balance $pin" + System.lineSeparator() + System.lineSeparator()
                        output += "1. Withdraw" + System.lineSeparator()
                        output += "2. Fund Transfer" + System.lineSeparator()
                        output += "3. Exit" + System.lineSeparator()
                        output += "Please choose option[3]:" + System.lineSeparator()
                        print(output)
                    }

                }
            }
        }
        if (state == ConsoleState.InputAccountNumber) {
            print("Enter Account Number: ")
        }
    }

    fun readLine(): String? {
        return readlnOrNull()
    }

    fun stop() {
        isAlive = false
        thread.join()
    }
}