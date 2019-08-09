package pando.actions

import pando.creatures.Creature
import pando.creatures.races.Frog
import pando.creatures.races.Golem
import pando.creatures.Token
import pando.creatures.races.FrogBehaviour
import pando.creatures.races.GolemBehaviour

class Attack(fatigue: Int = 1) : Action(fatigue) {
    override val melee: Boolean = true

    //esto deberia transformarse en Damage, y que la accion invoque el efecto
    override fun execute(creature: Creature, target: Creature, critical: Boolean) {
        var damage = (if(critical) creature.attack() * 2 else creature.attack()) - target.stats.defense

        if(damage < 1) damage = 1

        target.damageCounters += damage

        if (target.health() < 0)
            target.damageCounters = target.stats.health
    }

}