package pando.creatures

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import pando.domain.Rest

class Golem(restingTurns: Observable<Rest>) : Creature(
        initialHealth = 8,
        dexterity = 4,
        initialAttack = 2,
        defense = 1,
        speed = 1
) {
    val skill = restingTurns.subscribe{ if(fatigue == 0) addShield() }

    init{
        addShield()
    }

    private fun addShield() = this.addTokens(Token.GOLEM, 1)
}