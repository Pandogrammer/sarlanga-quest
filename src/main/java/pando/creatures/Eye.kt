package pando.creatures

import pando.domain.Kill
import io.reactivex.Observable

class Eye(team: Int = 0, deaths: Observable<Kill>) : Creature(
        initialHealth = 4,
        dexterity = 3,
        initialAttack = 1,
        defense = 0,
        speed = 3
) {

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