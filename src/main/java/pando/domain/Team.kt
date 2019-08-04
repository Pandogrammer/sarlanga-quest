package pando.domain

import pando.creatures.Creature
import pando.creatures.Position

class Team(val essence: Int = 0) {
    val creatures = HashMap<Position, Creature>()
    var usedEssence = 0

    fun addCreature(creature: Creature, position: Position) {
        if (positionIsEmpty(position) && positionIsValid(position) && essenceIsNotExceeded(creature.essence)) {
            creatures[position] = creature
            usedEssence += creature.essence
        }
    }

    fun getCreature(position: Position): Creature? {
        return creatures[position]
    }

    fun removeCreature(position: Position) {
        creatures.remove(position)
    }

    private fun positionIsEmpty(position: Position) = creatures[position] == null

    private fun positionIsValid(position: Position): Boolean = position.column in 1..2 && position.line in 1..2

    private fun essenceIsNotExceeded(creatureEssence: Int): Boolean = usedEssence + creatureEssence <= essence

}