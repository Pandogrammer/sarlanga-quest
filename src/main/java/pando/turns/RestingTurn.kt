package pando.turns

import io.reactivex.subjects.PublishSubject
import pando.creatures.Creature

class RestingTurn {

    val executed = PublishSubject.create<Unit>()

    fun execute(creatures: List<Creature>) {
        creatures.forEach { restIfAlive(it) }
        executed.onNext(Unit)
    }

    private fun restIfAlive(creature: Creature) {
        if (creature.health() > 0) {
            creature.fatigue -= creature.speed
            if (creature.fatigue < 0) creature.fatigue = 0
        }
    }

}