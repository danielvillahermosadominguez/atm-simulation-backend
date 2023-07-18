package atm.account.client

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.concurrent.thread

interface ConsoleCallback {
    fun userInput(value: String)

}

class ConsoleThreads(val callback: ConsoleCallback) {
    private var isAlive = true
    private lateinit var thread: Thread
    fun run() {
        thread = thread(isDaemon = true) {
            while (isAlive) {
                val input = readLine()
                if (input !== null && !input.isNullOrBlank()) {
                    callback.userInput(input)
                    println()
                    print("Enter PIN: ")
                }
            }
        }
        print("Enter Account Number: ")
    }

    fun readLine(): String? {
        return readlnOrNull()
    }

    fun stop() {
        isAlive = false
        thread.join()
    }
}