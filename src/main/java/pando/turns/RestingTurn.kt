package pando.turns

import io.reactivex.subjects.PublishSubject
import pando.creatures.Creature
import pando.domain.Rest

class RestingTurn {

    val executed = PublishSubject.create<Rest>()

    fun execute(creatures: List<Creature>) {
        creatures.forEach { restIfAlive(it) }
        executed.onNext(Rest())
    }

    private fun restIfAlive(creature: Creature) {
        if (creature.health() > 0) {
            creature.fatigue -= creature.stats.speed
            if (creature.fatigue < 0) creature.fatigue = 0
        }
    }

}