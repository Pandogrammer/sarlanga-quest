package pando.creatures

import io.reactivex.Observable
import pando.domain.Damage

class Frog(damages: Observable<Damage>) : Creature(
        initialHealth = 5,
        dexterity = 4,
        initialAttack = 3,
        defense = 0,
        speed = 2
) {
    val skill = damages.filter{ it.target == this && it.action.melee }.subscribe{
        it.actor.addTokens(Token.FROG, 1)
    }
}