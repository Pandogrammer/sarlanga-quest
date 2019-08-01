package pando.creatures

import io.reactivex.Observable
import pando.domain.Death
import pando.domain.Rest

class Skeleton(team: Int = 0, restingTurns: Observable<Rest>, deaths: Observable<Death>) : Creature(
        initialHealth = 6,
        dexterity = 4,
        initialAttack = 2,
        defense = 0,
        speed = 1
) {


    val skill = deaths.filter { it.creature == this }.subscribe{
                fatigue = 7
                restingTurns.takeUntil{ fatigue == 0 }.subscribe {
                    fatigue -= 1
                    if (fatigue == 0){
                        damage = 0
                    }
                }
            }



    override fun toString(): String {
        return "SKELETON"
    }
}

