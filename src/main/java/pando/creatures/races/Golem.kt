package pando.creatures.races

import pando.creatures.*
import pando.creatures.cards.GolemCard
import pando.domain.Events


class GolemStats : CreatureStats(8, 2, 1, 1, 4, 1)

class GolemBehaviour: CreatureBehaviour {
    private val tokenType = Token.GOLEM

    private fun addShield(spawnedCreature: SpawnedCreature) = spawnedCreature.addTokens(tokenType, 1)

    override fun attachTo(spawnedCreature: SpawnedCreature, events: Events) {
        val shieldRegeneration = events.rest.subscribe{ if(spawnedCreature.fatigue == 0) addShield(spawnedCreature) }

        //polemico, porque en realidad es mas una regen que un bloqueo de daño,
        //hay un frame donde el golem puede estar muerto
        val shieldBlock = events.damageEvent.filter{ it.target == spawnedCreature }.subscribe{
            spawnedCreature.getTokens(tokenType)?.let {
                spawnedCreature.damageCounters -= it
                spawnedCreature.removeTokens(tokenType, it)
            }
        }

        addShield(spawnedCreature)
    }

}

