package pando.creatures.races

import pando.creatures.*
import pando.domain.Events


class Golem(events: Events, position: Position, team: Int) : Creature(GolemStats(), position, team) {
    override val behaviour = GolemBehaviour(this, events)
}

class GolemStats : CreatureStats(8, 2, 1, 1, 4, 1)

class GolemBehaviour(override val creature: Creature, override val events: Events) : CreatureBehaviour {
    private val tokenType = Token.GOLEM

    init {
        val shieldRegeneration = events.rest.subscribe{ if(creature.fatigue == 0) addShield() }

        //polemico, porque en realidad es mas una regen que un bloqueo de daño,
        //hay un frame donde el golem puede estar muerto
        val shieldBlock = events.damageEvent.filter{ it.target == creature }.subscribe{
            creature.getTokens(tokenType)?.let {
                creature.damageCounters -= it
                creature.removeTokens(tokenType, it)
            }
        }

        addShield()
    }

    private fun addShield() = creature.addTokens(tokenType, 1)
}

