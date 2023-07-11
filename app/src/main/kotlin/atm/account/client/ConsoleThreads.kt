package atm.account.client

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.concurrent.thread

interface ConsoleCallback {
    fun userInput(value: String)

}
class ConsoleThreads(val callback: ConsoleCallback) {
    fun run() {
        thread(isDaemon = true) {
            while (true) {
                val input = readLine()
                if (!input.isNullOrBlank()) {
                    callback.userInput(input)
                    println()
                    print("Enter PIN: ")
                }
            }
        }
        print("Enter Account Number: ")
    }

    fun readLine(): String {
        val reader = BufferedReader(
                InputStreamReader(System.`in`)
        )
        return reader.readLine()
    }
}