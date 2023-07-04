package atm.account.integration

import io.kotest.core.spec.style.FreeSpec
import io.kotest.framework.concurrency.eventually
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.GlobalScope
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class ConsoleChannelTest : FreeSpec( {

    "should show the welcome screen asking for the account number" {
        val channel = Channel<String>()
        val console = Console(channel)

        val (baos, old) = initCaptureOutput()
        console.run()
        val written = String(baos.toByteArray())
        written shouldBe "Enter Account Number: "
        restoreOutput(old)
    }

    "should read account number" {
        val channel = Channel<String>()
        val console = Console(channel)

        console.run()

        val bis = ByteArrayInputStream("123456\n".toByteArray())
        System.setIn(bis)

        val incomingNotification = withTimeout(10000L) {
            channel.receive()
        }

        incomingNotification shouldBe "123456"
    }

    "should show a text asking the PIN" {
        val channel = Channel<String>()
        val console = Console(channel)
        val (baos, old) = initCaptureOutput()
        val bis = ByteArrayInputStream("123456\n".toByteArray())
        System.setIn(bis)
        console.run()
        withTimeout(10000L) {
            channel.receive()
        }

        eventually(10000L) {
            val written = String(baos.toByteArray())
            written shouldBe "Enter Account Number: 123456${System.lineSeparator()}Enter PIN: "
        }

        restoreOutput(old)
    }
} )

class Console constructor(val channel: Channel<String>) {
    suspend fun run() {
        print("Enter Account Number: ")
        GlobalScope.launch {
            val accountNumber = readLine()
            println(accountNumber)
            channel.send(accountNumber)
            print("Enter PIN: ")
        }
    }

    fun readLine(): String {
        val stopChar = -1
        val carriageReturn: Int = '\n'.code
        val stringBuilder = StringBuilder()
        var currentChar = System.`in`.read()
        while (currentChar != stopChar && currentChar != carriageReturn) {
            stringBuilder.append(currentChar.toChar())
            currentChar = System.`in`.read()
        }
        return stringBuilder.toString()
    }
}

private fun restoreOutput(old: PrintStream) {
    System.out.flush()
    System.setOut(old)
}

private fun initCaptureOutput(): Pair<ByteArrayOutputStream, PrintStream> {
    val baos = ByteArrayOutputStream()
    val ps = PrintStream(baos)
    val old = System.out
    System.setOut(ps)
    return Pair(baos, old)
}
