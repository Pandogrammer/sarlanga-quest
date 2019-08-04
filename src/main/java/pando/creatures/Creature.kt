package pando.creatures

open class Creature(val stats: CreatureStats,
                    val behaviour: CreatureBehaviour,
                    val position: Position,
                    val team: Int) {

    val tokens = HashMap<Token, Int>()

    var fatigue: Int = initialFatigue
    var attack: Int = initialAttack
    var damage: Int = 0

    val health = { initialHealth - damage }

    fun addTokens(token: Token, quantity: Int) {
        if(!tokens.containsKey(token))
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

