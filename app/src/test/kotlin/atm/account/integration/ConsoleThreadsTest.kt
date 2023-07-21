package atm.account.integration

import atm.account.client.ConsoleCallback
import atm.account.client.ConsoleThreads
import io.kotest.core.spec.style.FreeSpec
import io.kotest.framework.concurrency.eventually
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ConsoleThreadsTest : FreeSpec({

    lateinit var callback: ConsoleCallback
    lateinit var console: ConsoleThreads

    beforeEach {
        callback = mockk(relaxed = true)
        console = ConsoleThreads(callback)
    }

    afterEach {
        console.stop()
    }

    "should show the welcome screen asking for the account number" {
        val (fakeStandardOutput, old) = initCaptureOutput()
        console.run()
        val written = String(fakeStandardOutput.toByteArray())
        written shouldBe "Enter Account Number: "
        restoreOutput(old)
    }

    "should read account number" {
        fakeUserInput("123456" + System.lineSeparator()).use {
            console.run()
            eventually(1000L) {
                verify { callback.userInput("123456") }
            }
        }
    }

    "should ask for a pin" {
        fakeUserInput("123456" + System.lineSeparator()).use {
            val (fakeStandardOutput, old) = initCaptureOutput()
            console.run()

            eventually(1000L) {
                val written = String(fakeStandardOutput.toByteArray())
                written shouldBe "Enter Account Number: " + System.lineSeparator() + "Enter PIN: "
            }

            restoreOutput(old)
        }
    }

    "should read user pin" {
        fakeUserInput("123456" + System.lineSeparator() + "2345" + System.lineSeparator()).use {
            console.run()
            eventually(1000L) {
                verify { callback.userInput("123456") }
            }
            eventually(1000L) {
                verify { callback.userInput("2345") }
            }
        }
    }

    "should show the transaction screen" {
         val accountNumber = "123456"
         val pin = "4345"
         fakeUserInput(accountNumber + System.lineSeparator() +pin).use {
             val (fakeStandardOutput, old) = initCaptureOutput()
             console.run()

             eventually(1000L) {
                 val written = String(fakeStandardOutput.toByteArray())
                 var expectedOutput = "Enter Account Number: " + System.lineSeparator() + "Enter PIN: " + System.lineSeparator()
                 expectedOutput += "Account number $accountNumber, balance $pin" + System.lineSeparator() + System.lineSeparator()
                 expectedOutput += "1. Withdraw" + System.lineSeparator()
                 expectedOutput += "2. Fund Transfer" + System.lineSeparator()
                 expectedOutput += "3. Exit" + System.lineSeparator()
                 expectedOutput += "Please choose option[3]:" + System.lineSeparator()
                 written shouldBe "Enter Account Number: " + System.lineSeparator() + "Enter PIN: " + System.lineSeparator()+ expectedOutput
                         ""
             }

             restoreOutput(old)
         }
     }
})

private fun fakeUserInput(input: String): ByteArrayInputStream {
    val bis = ByteArrayInputStream(input.toByteArray())
    System.setIn(bis)
    return bis
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