package atm.account.integration

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.io.PrintStream
import kotlin.concurrent.thread

class ConsoleThreadsTest : FreeSpec({
    "should show the welcome screen asking for the account number" {
        val callback: ConsoleCallback = mockk(relaxed = true)
        val console = ConsoleThreads(callback)

        val (fakeStandardOutput, old) = initCaptureOutput()
        console.run()
        val written = String(fakeStandardOutput.toByteArray())
        written shouldBe "Enter Account Number: "
        restoreOutput(old)
    }

    "should read account number" {
        val callback: ConsoleCallback = mockk(relaxed = true)
        val console = ConsoleThreads(callback)
        val bis = ByteArrayInputStream("123456\n".toByteArray())
        System.setIn(bis)

        console.run()

        verify { callback.userInput("123456") }

        bis.close()
    }
})

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

private fun initCaptureOutput(): Pair<ByteArrayOutputStream, PrintStream> {
    val baos = ByteArrayOutputStream()
    val ps = PrintStream(baos)
    val old = System.out
    System.setOut(ps)
    return Pair(baos, old)
}

private fun restoreOutput(old: PrintStream) {
    System.out.flush()
    System.setOut(old)
}