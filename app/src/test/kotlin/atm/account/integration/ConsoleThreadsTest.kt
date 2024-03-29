package atm.account.integration

import atm.account.client.ConsoleCallback
import atm.account.client.ConsoleThreads
import io.kotest.core.spec.style.FreeSpec
import io.kotest.framework.concurrency.eventually
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldEndWith
import io.mockk.mockk
import io.mockk.verify
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream


fun transactionScreenOutput(accountNumber: String, pin: String) = "Account number $accountNumber, balance $pin" + System.lineSeparator() + System.lineSeparator() +
        "1. Withdraw" + System.lineSeparator() +
        "2. Fund Transfer" + System.lineSeparator() +
        "3. Exit" + System.lineSeparator() +
        "Please choose option[3]:" + System.lineSeparator()


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
        fakeUserInput(userInput("-")).use {
            val (fakeStandardOutput, old) = initCaptureOutput()
            console.run()
            eventually(1000L) {
                val written = String(fakeStandardOutput.toByteArray())
                written shouldBe "Enter Account Number: "
            }
            restoreOutput(old)
        }
    }

    "should ask for a pin" {
        fakeUserInput(userInput("123456")).use {
            val (fakeStandardOutput, old) = initCaptureOutput()
            console.run()

            eventually(1000L) {
                val written = String(fakeStandardOutput.toByteArray())
                written shouldContain System.lineSeparator() + "Enter PIN: "
            }

            restoreOutput(old)
        }
    }

    "should read user pin" {
        fakeUserInput(userInput("123456", "2345")).use {
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
        fakeUserInput(userInput(accountNumber, pin)).use {
            val (fakeStandardOutput, old) = initCaptureOutput()
            console.run()

            eventually(1000L) {
                val written = String(fakeStandardOutput.toByteArray())
                var expectedOutput = transactionScreenOutput(accountNumber, pin)
                written shouldContain expectedOutput
            }

            restoreOutput(old)
        }
    }

    "should show the Withdraw screen" {
        val accountNumber = "123456"
        val pin = "4345"
        fakeUserInput(userInput(accountNumber, pin, "1")).use {
            val (fakeStandardOutput, old) = initCaptureOutput()
            console.run()

            eventually(1000L) {
                val written = String(fakeStandardOutput.toByteArray())
                var expectedOutput = "1. $10" + System.lineSeparator()
                expectedOutput += "2. $50" + System.lineSeparator()
                expectedOutput += "3. $100" + System.lineSeparator()
                expectedOutput += "4. Other" + System.lineSeparator()
                expectedOutput += "5. Back" + System.lineSeparator()
                expectedOutput += "Please choose options[5]:" + System.lineSeparator()
                written shouldContain expectedOutput
                ""
            }

            restoreOutput(old)
        }
    }


    "should navigate to Transaction screen when user ask for back in Withdraw screen" {
        val accountNumber = "123456"
        val pin = "4345"
        fakeUserInput(userInput(accountNumber, pin, "1", "5")).use {
            val (fakeStandardOutput, old) = initCaptureOutput()
            console.run()

            eventually(1000L) {
                val written = String(fakeStandardOutput.toByteArray())
                var expectedOutput = "Account number $accountNumber, balance $pin" + System.lineSeparator() + System.lineSeparator()
                expectedOutput += "1. Withdraw" + System.lineSeparator()
                expectedOutput += "2. Fund Transfer" + System.lineSeparator()
                expectedOutput += "3. Exit" + System.lineSeparator()
                expectedOutput += "Please choose option[3]:" + System.lineSeparator()

                written shouldEndWith expectedOutput
                ""
            }

            restoreOutput(old)
        }
    }

    "should navigate to  Fund Transfer screen - Screen 1" {
        val accountNumber = "123456"
        val pin = "4345"
        fakeUserInput(userInput(accountNumber, pin, "2")).use {
            val (fakeStandardOutput, old) = initCaptureOutput()
            console.run()

            eventually(1000L) {
                val written = String(fakeStandardOutput.toByteArray())
                var expectedOutput = "Please enter destination account and press enter to continue or press enter to go back to Transaction:"

                written shouldEndWith expectedOutput
                ""
            }

            restoreOutput(old)
        }
    }

    "should navigate to transaction screen when user press enter" {
        val accountNumber = "123456"
        val pin = "4345"
        fakeUserInput(userInput(accountNumber, pin, "2", "")).use {
            val (fakeStandardOutput, old) = initCaptureOutput()
            console.run()

            eventually(1000L) {
                val written = String(fakeStandardOutput.toByteArray())
                var expectedOutput = transactionScreenOutput(accountNumber, pin)
                written shouldEndWith expectedOutput
                ""
            }

            restoreOutput(old)
        }
    }

    "should choose the Other screen" {
        fakeUserInput(userInput("123456", "2345", "1", "4")).use {
            console.run()
            eventually(1000L) {
                verify { callback.userInput("4") }
            }
        }
    }

    "should show the Other screen" {
        val accountNumber = "123456"
        val pin = "4345"
        fakeUserInput(userInput(accountNumber, pin, "1", "4")).use {
            val (fakeStandardOutput, old) = initCaptureOutput()
            console.run()

            eventually(1000L) {
                val written = String(fakeStandardOutput.toByteArray())
                var expectedOutput = "Other Withdraw" + System.lineSeparator()
                expectedOutput += "Enter amount to withdraw" + System.lineSeparator()
                written shouldEndWith expectedOutput
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
    val ps = PrintStream(baos, true)
    val old = System.out
    System.setOut(ps)
    return Pair(baos, old)
}

private fun restoreOutput(old: PrintStream) {
    System.out.flush()
    System.setOut(old)
}


fun userInput(vararg input: String): String = input.joinToString("") { it + System.lineSeparator() }
