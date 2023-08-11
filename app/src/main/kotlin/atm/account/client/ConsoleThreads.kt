package atm.account.client

import kotlin.concurrent.thread

interface ConsoleCallback {
    fun userInput(value: String)

}


enum class ConsoleState(val nextStateAction: (event: Event) -> ConsoleState?, val action: (state: ConsoleState) -> Unit) {
    OtherWithdrawScreen({ null }, {
        println()
        var output = "Other Withdraw" + System.lineSeparator()
        output += "Enter amount to withdraw" + System.lineSeparator()
        print(output)
    }),
    WithdrawScreen(
        {
            when (it.data) {
                "5" -> TransactionScreen
                else -> OtherWithdrawScreen
            }
        },
        {
            println()
            var output = "1. $10" + System.lineSeparator()
            output += "2. $50" + System.lineSeparator()
            output += "3. $100" + System.lineSeparator()
            output += "4. Other" + System.lineSeparator()
            output += "5. Back" + System.lineSeparator()
            output += "Please choose options[5]:" + System.lineSeparator()
            print(output)
        }),
    TransactionScreen({
        when (it.data) {
            "2" -> TransferScreen1
            else -> WithdrawScreen
        }
    }, {
        println()
        val accountNumber = it.data?.get(0)
        val pin = it.data?.get(1)
        var output = "Account number $accountNumber, balance $pin" + System.lineSeparator() + System.lineSeparator()
        output += "1. Withdraw" + System.lineSeparator()
        output += "2. Fund Transfer" + System.lineSeparator()
        output += "3. Exit" + System.lineSeparator()
        output += "Please choose option[3]:" + System.lineSeparator()
        print(output)
    }),
    TransferScreen1({ null }, {
       println()
        var output =
            "Please enter destination account and press enter to continue or press enter to go back to Transaction:"
       print(output)
    }),
    InputAccountPin({ TransactionScreen }, {
        println()
        print("Enter PIN: ")
    }),
    InputAccountNumber({ InputAccountPin }, { print("Enter Account Number: ") });

    var data: List<String> = emptyList<String>()

    fun currentAction() {
        action(this)
    }

    fun nextState(event: Event): ConsoleState? {
        val data = event.data
        val nextState = this.nextStateAction(event)
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

    private var stateMachine = StateMachine()

    fun run() {
        thread = thread(isDaemon = true) {
            stateMachine.state?.currentAction()
            while (isAlive) {
                val input = readLine()
                if (input !== null && !input.isNullOrBlank()) {
                    callback.userInput(input)
                    stateMachine.nextState(Event(input))
                    stateMachine.state?.currentAction()
                }
            }
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