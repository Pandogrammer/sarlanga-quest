package pando.turns

import io.reactivex.subjects.PublishSubject
import pando.creatures.SpawnedCreature
import pando.domain.Rest

class RestingTurn {

    val executed = PublishSubject.create<Rest>()

    fun execute(spawnedCreatures: List<SpawnedCreature>) {
        spawnedCreatures.forEach { restIfAlive(it) }
        executed.onNext(Rest())
    }

    private fun restIfAlive(spawnedCreature: SpawnedCreature) {
        if (spawnedCreature.health() > 0) {
            spawnedCreature.fatigue -= spawnedCreature.stats.speed
            if (spawnedCreature.fatigue < 0) spawnedCreature.fatigue = 0
        }
    }

}