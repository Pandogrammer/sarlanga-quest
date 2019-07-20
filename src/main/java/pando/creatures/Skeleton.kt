package pando.creatures

import io.reactivex.Observable
import pando.domain.Death

class Skeleton(team: Int = 0, restingTurns: Observable<Unit>, deaths: Observable<Death>) : Creature(initialHealth = 6, initialAttack = 1, speed = 1, team = team, dexterity = 3){


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

