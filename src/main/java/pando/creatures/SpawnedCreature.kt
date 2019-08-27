package pando.creatures

import pando.creatures.cards.CreatureCard

class SpawnedCreature(val id: Int,
                      val position: Position,
                      val team: Int,
                      val stats: CreatureStats,
                      val behaviour: CreatureBehaviour?,
                      val card: CreatureCard?/*caca*/) {

    override fun toString(): String {
        return "[T$team]${javaClass.simpleName}[${position.column}-${position.line}]"
    }

    var fatigue: Int = 0
    var damageCounters: Int = 0
    var attackBonus: Int = 0

    val health = { stats.health - damageCounters }
    val attack = { stats.attack + attackBonus }

    val tokens = HashMap<Token, Int>()

    fun addTokens(token: Token, quantity: Int) {
        if (!tokens.containsKey(token))
            tokens[token] = 0

        tokens[token]?.let { tokens[token] = it.plus(quantity) }
    }

    fun getTokens(token: Token): Int? {
        return tokens[token]
    }

    fun removeTokens(token: Token, quantity: Int) {
        tokens[token]?.let {
            tokens[token] = it.minus(quantity)
            if (it < 0) {
                tokens.remove(token)
            }
        }
    }

}

