package pando.creatures

open class Creature(val initialHealth: Int = 1,
                    val initialFatigue: Int = 0,
                    val initialAttack: Int = 0,
                    val defense: Int = 0,
                    val speed: Int = 0,
                    val position: Position = Position(0, 0),
                    val team: Int = 0,
                    val dexterity: Int = 0,
                    val essence: Int = 0) {

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

