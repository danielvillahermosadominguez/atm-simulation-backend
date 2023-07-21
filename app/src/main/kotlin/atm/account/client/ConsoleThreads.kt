package atm.account.client

import kotlin.concurrent.thread

interface ConsoleCallback {
    fun userInput(value: String)

}

enum class ConsoleState { InputAccountNumber, InputAccountPin }

class ConsoleThreads(val callback: ConsoleCallback) {
    private var isAlive = true
    private lateinit var thread: Thread
    private var state = ConsoleState.InputAccountNumber
    fun run() {
        thread = thread(isDaemon = true) {
            while (isAlive) {
                val input = readLine()
                if (input !== null && !input.isNullOrBlank()) {
                    callback.userInput(input)
                    if (state == ConsoleState.InputAccountNumber) {
                        state = ConsoleState.InputAccountPin
                    }

                    if (state == ConsoleState.InputAccountPin) {
                        println()
                        print("Enter PIN: ")
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