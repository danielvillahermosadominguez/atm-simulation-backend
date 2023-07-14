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