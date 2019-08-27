package pando.creatures.races

import pando.creatures.*
import pando.creatures.cards.FrogCard
import pando.domain.Events

class FrogStats : CreatureStats(5, 3, 0, 2, 4, 1)

class FrogBehaviour : CreatureBehaviour {
    private val tokenType = Token.FROG

    override fun attachTo(spawnedCreature: SpawnedCreature, events: Events) {

        val reaction = events.damageEvent.filter{ it.target == spawnedCreature && it.action.melee }.subscribe{
            it.actor.addTokens(tokenType, 1)
        }

        val extraDamage = events.damageEvent.filter{ it.actor == spawnedCreature }.subscribe {
            val tokens = it.target.getTokens(tokenType)
            tokens?.let { tokens ->
                it.target.damageCounters += tokens
                it.target.removeTokens(tokenType, tokens)
            }
        }
    }
}