import java.util.*

// Class for storing state information
class State {
    var x: Int
    var y: Int

    constructor(x: Int, y: Int) {
        this.x = x
        this.y = y
    }
}

// Class for storing action information
class Action {
    var x: Int
    var y: Int

    constructor(x: Int, y: Int) {
        this.x = x
        this.y = y
    }
}

// Q-Learning algorithm implementation in Kotlin
class QLearning {
    var qTable = mutableMapOf<State, MutableMap<Action, Double>>()
    var actions = arrayOf(Action(-1, 0), Action(1, 0), Action(0, -1), Action(0, 1))
    var epsilon = 0.3
    var alpha = 0.1
    var gamma = 0.9

    // Initialize Q-Values for all states and actions
    fun initQValues(states: List<State>) {
        for (state in states) {
            qTable[state] = mutableMapOf()
            for (action in actions) {
                qTable[state]!![action] = 0.0
            }
        }
    }

    // Select action using epsilon-greedy strategy
    fun selectAction(state: State): Action {
        if (Math.random() < epsilon) {
            // Random action
            return actions[(Math.random() * actions.size).toInt()]
        } else {
            // Greedy action
            var maxValue = Double.NEGATIVE_INFINITY
            var maxAction = actions[0]
            for (action in actions) {
                if (qTable[state]!![action]!! > maxValue) {
                    maxValue = qTable[state]!![action]!!
                    maxAction = action
                }
            }
            return maxAction
        }
    }

    // Update Q-Value for given state and action
    fun updateQValue(state: State, action: Action, reward: Double, nextState: State) {
        var maxValue = Double.NEGATIVE_INFINITY
        for (nextAction in actions) {
            maxValue = Math.max(maxValue, qTable[nextState]!![nextAction]!!)
        }
        qTable[state]!![action] = qTable[state]!![action]!! + alpha * (reward + gamma * maxValue - qTable[state]!![action]!!)
    }
}

fun main(args: Array<String>) {
    var qLearning = QLearning()
    var states = listOf(State(0, 0), State(0, 1), State(1, 0), State(1, 1))
    qLearning.initQValues(states)

    // Training loop
    for (i in 1..1000) {
        var currentState = states[(Math.random() * states.size).toInt()]
        var episode = mutableListOf<Pair<State, Action>>()

        // Generate episode
        while (currentState != states[0]) {
            var action = qLearning.selectAction(currentState)
            episode.add(Pair(currentState
