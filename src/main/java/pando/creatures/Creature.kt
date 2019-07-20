package pando.creatures

open class Creature(val initialHealth: Int = 1,
                    val initialFatigue: Int = 0,
                    val initialAttack: Int = 0,
                    val defense: Int = 0,
                    val speed: Int = 0,
                    val position: Position = Position(0, 0),
                    val team: Int = 0,
                    val dexterity: Int = 0) {

    var fatigue: Int = initialFatigue
    var health: Int = initialHealth
    var attack: Int = initialAttack

}

enum class CreatureCode {
    EYE, SKELETON
}