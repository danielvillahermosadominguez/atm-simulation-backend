package atm.account.client

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.concurrent.thread
import kotlinx.coroutines.runBlocking
import ru.nsk.kstatemachine.DefaultState
import ru.nsk.kstatemachine.Event
import ru.nsk.kstatemachine.FinalState
import ru.nsk.kstatemachine.addFinalState
import ru.nsk.kstatemachine.addInitialState
import ru.nsk.kstatemachine.createStateMachine
import ru.nsk.kstatemachine.onEntry
import ru.nsk.kstatemachine.onExit
import ru.nsk.kstatemachine.onFinished
import ru.nsk.kstatemachine.onTriggered
import ru.nsk.kstatemachine.transition

interface ConsoleCallback {
    fun userInput(value: String)

}

sealed class States : DefaultState() {
    object InputAccountNumber : States()
    object InputAccountPin : States() , FinalState
}
object SwitchEvent : Event
object StateMachineDefinition{
    fun createStateMachine() = runBlocking {
        // Create state machine and configure its states in a setup block
        val machine = createStateMachine(this) {
            addInitialState(States.InputAccountNumber) {
                // Add state listeners
                onEntry { println("Enter account number") }
                onExit { println("Exit account number") }

                // Setup transition
                transition<SwitchEvent> {
                    targetState = States.InputAccountPin
                    // Add transition listener
                    onTriggered { println("Transition triggered") }
                }
            }

            addFinalState(States.InputAccountPin)

            onFinished { println("Finished") }
        }

        return@runBlocking machine
    }
}

class ConsoleThreads(val callback: ConsoleCallback) {
    private var isAlive = true
    private lateinit var thread: Thread
    private var stateMachine = StateMachineDefinition.createStateMachine()
    fun run() {
        thread = thread(isDaemon = true) {
            while (isAlive) {
                val input = readLine()
                if (input !== null && !input.isNullOrBlank()) {
                    callback.userInput(input)
                    runBlocking {
                        stateMachine.processEvent(SwitchEvent)
                    }

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