package atm.account.integration

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ConsoleTest : FreeSpec( {

    "should show the welcome screen asking for the account number" {
        val console = Console()

        val (baos, old) = initCaptureOutput()
        console.run()
        val written = String(baos.toByteArray())
        written shouldBe "Enter Account Number:"
        endCaptureOutput(old)
    }

    "should read account number" {
        val console = Console()
        console.run()

        val bis = ByteArrayInputStream("123456\n".toByteArray())
        System.setIn(bis)

        console.readLine() shouldBe "123456"

    }
} )

class Console {
    fun run() {
        print("Enter Account Number:")
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

private fun endCaptureOutput(old: PrintStream) {
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
