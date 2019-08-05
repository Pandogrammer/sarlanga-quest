package pando.domain

import pando.creatures.Position
import pando.creatures.cards.CreatureCard

class Team(val essence: Int = 0) {
    val creatures = HashMap<Position, CreatureCard>()
    var usedEssence = 0

    fun addCreature(position: Position, creatureCard: CreatureCard) {
        if (positionIsEmpty(position) && positionIsValid(position) && essenceIsNotExceeded(creatureCard.stats.essence)) {
            creatures[position] = creatureCard
            usedEssence += creatureCard.stats.essence
        }
    }

    fun getCreature(position: Position): CreatureCard? {
        return creatures[position]
    }

    fun removeCreature(position: Position) {
        creatures.remove(position)
    }

    fun positionIsEmpty(position: Position) = creatures[position] == null

    fun positionIsValid(position: Position): Boolean = position.column in 1..2 && position.line in 1..2

    fun essenceIsNotExceeded(creatureEssence: Int): Boolean = usedEssence + creatureEssence <= essence

}