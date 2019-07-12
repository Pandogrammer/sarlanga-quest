package actions

import kotlin.random.Random

open class ActionDie {
    open fun roll() : Int {
        return Random.nextInt()
    }
}