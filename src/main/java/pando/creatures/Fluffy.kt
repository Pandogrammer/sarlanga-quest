package pando.creatures

import io.reactivex.Observable
import pando.domain.Kill

class Fluffy(kills: Observable<Kill>) : Creature(initialHealth = 6) {
    var killCounter = 0

    init{
        kills.filter { it.killer == this }.subscribe {
            killCounter++
            damage -= killCounter

            if(damage < 0 ) damage = 0
        }
    }
}