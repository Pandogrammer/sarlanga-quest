package pando.domain

import io.reactivex.Observable
import pando.actions.Action
import pando.creatures.SpawnedCreature


interface Events {
    val actions: Observable<ActionExecution>
    val rest: Observable<Rest>
    val damageEvent: Observable<DamageEvent>
    val kills: Observable<Kill>
    val deaths: Observable<Death>
}

class ActionExecution(val actor: SpawnedCreature, val action: Action, val target: SpawnedCreature, val roll: Int)
class Death(val spawnedCreature: SpawnedCreature)
class Kill(val killer: SpawnedCreature)
class DamageEvent(val actor: SpawnedCreature, val action: Action, val target: SpawnedCreature)
class Rest