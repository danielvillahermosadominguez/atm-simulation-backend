package atm.account.client

import kotlin.concurrent.thread

interface ConsoleCallback {
    fun userInput(value: String)

}


enum class ConsoleState(private val nextState: ConsoleState?)
{

    TransactionScreen(null),
    InputAccountPin(TransactionScreen),
    InputAccountNumber(InputAccountPin);

    var data: List<String> = emptyList<String>()

    fun nextState(event: Event): ConsoleState? {
        val data = event.data
        val nextState = this.nextState
        nextState?.data = this.data + data
        return nextState
    }
}

data class Event(val data: String)


class StateMachine(var state: ConsoleState? = ConsoleState.InputAccountNumber) {
    fun nextState(event: Event) {
        state = state?.nextState(event)
    }
}

class ConsoleThreads(val callback: ConsoleCallback) {
    private var isAlive = true
    private lateinit var thread: Thread
    //private var state = ConsoleState.InputAccountNumber
    //private var accountNumber: String? = null
    //private var pin: String? = null

    private var stateMachine = StateMachine()

    fun run() {
        thread = thread(isDaemon = true) {
            while (isAlive) {
                val input = readLine()
                if (input !== null && !input.isNullOrBlank()) {
                    callback.userInput(input)
                    stateMachine.nextState(Event(input))

                    if (stateMachine.state == ConsoleState.InputAccountPin) {
                        println()
                        print("Enter PIN: ")
                    }
                    if (stateMachine.state == ConsoleState.TransactionScreen) {
                        println()
                        val accountNumber = stateMachine.state?.data?.get(0)
                        val pin = stateMachine.state?.data?.get(1)
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
        if (stateMachine.state == ConsoleState.InputAccountNumber) {
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