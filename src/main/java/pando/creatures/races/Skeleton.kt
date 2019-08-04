package pando.creatures.races

import io.reactivex.Observable
import pando.creatures.*
import pando.domain.Death
import pando.domain.Rest

class Skeleton(events: Events, position: Position, team: Int) : Creature(SkeletonStats(), position, team) {
    override val behaviour = SkeletonBehaviour(this, events)
}

class SkeletonStats : CreatureStats(6, 2, 0, 1, 4, 1)

class SkeletonBehaviour(override val creature: Creature, override val events: Events) : CreatureBehaviour {
    init {
        events.deaths.filter { it.creature == creature }.subscribe{
            creature.fatigue = 7
            events.rest.takeUntil{ creature.fatigue == 0 }.subscribe {
                creature.fatigue -= 1
                if (creature.fatigue == 0){
                    creature.damageCounters = 0
                }
            }
        }
    }
}
