package pando.creatures

import io.reactivex.Observable
import pando.domain.Damage
import pando.domain.Death
import pando.domain.Kill
import pando.domain.Rest
import pando.turns.ActionExecution

interface Events {
    val actions: Observable<ActionExecution>
    val rest: Observable<Rest>
    val damage: Observable<Damage>
    val kills: Observable<Kill>
    val deaths: Observable<Death>
}