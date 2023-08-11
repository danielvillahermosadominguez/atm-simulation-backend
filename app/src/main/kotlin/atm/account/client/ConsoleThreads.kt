package atm.account.client

import kotlin.concurrent.thread

interface ConsoleCallback {
    fun userInput(value: String)

}
data class Event(val data: String)


class StateMachine(var state: ConsoleState? = ConsoleState.InputAccountNumber) {
    fun nextState(event: Event) {
        state = state?.nextState(event)
    }
}

class ConsoleThreads(val callback: ConsoleCallback) {
    private var isAlive = true
    private lateinit var thread: Thread

    private var stateMachine = StateMachine()

    fun run() {
        thread = thread(isDaemon = true) {
            stateMachine.state?.currentAction()
            while (isAlive) {
                val input = readLine()
                if (input !== null && !input.isNullOrBlank()) {
                    callback.userInput(input)
                    stateMachine.nextState(Event(input))
                    stateMachine.state?.currentAction()
                }
            }
        }
    }

    fun readLine(): String? {
        return readlnOrNull()
    }

    fun stop() {
        isAlive = false
        thread.join()
    }
}