package pando.domain

import io.reactivex.Observable
import pando.actions.Action
import pando.creatures.Creature


interface Events {
    val actions: Observable<ActionExecution>
    val rest: Observable<Rest>
    val damage: Observable<Damage>
    val kills: Observable<Kill>
    val deaths: Observable<Death>
}

class ActionExecution(val creature: Creature, val action: Action, val target: Creature, val roll: Int)
class Death(val creature: Creature)
class Kill(val killer: Creature)
class Damage(val actor: Creature, val action: Action, val target: Creature)
class Rest