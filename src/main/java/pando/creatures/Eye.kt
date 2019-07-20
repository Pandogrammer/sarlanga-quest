package pando.creatures

import pando.domain.Death
import io.reactivex.Observable

class Eye(team: Int = 0, deaths: Observable<Death>) : Creature(initialHealth = 4, initialAttack = 2, speed = 2, team = team, dexterity = 3) {

    init {
        deaths
            .filter{ it.killer == this }
            .subscribe{
                attack++
            }
    }

    override fun toString(): String {
        return "EYE"
    }
}