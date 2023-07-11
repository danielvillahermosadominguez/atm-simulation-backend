package atm.account.integration

import atm.account.client.ConsoleCallback
import atm.account.client.ConsoleThreads
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ConsoleThreadsTest : FreeSpec({
    /*
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
     */

    "should ask for a pin" {
        val callback: ConsoleCallback = mockk(relaxed = true)
        val console = ConsoleThreads(callback)
        val bis = ByteArrayInputStream("123456\n".toByteArray())
        System.setIn(bis)

        val (fakeStandardOutput, old) = initCaptureOutput()
        console.run()
        Thread.sleep(1000)
        val written = String(fakeStandardOutput.toByteArray())
        written shouldBe """Enter Account Number: 
            |Enter PIN: """.trimMargin()
        restoreOutput(old)
    }
})



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