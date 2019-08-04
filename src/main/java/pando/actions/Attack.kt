package pando.actions

import pando.creatures.Creature
import pando.creatures.races.Frog
import pando.creatures.races.Golem
import pando.creatures.Token

class Attack(fatigue: Int = 1) : Action(fatigue) {
    override val melee: Boolean = true

    override fun execute(creature: Creature, target: Creature, critical: Boolean) {
        var damage = (if(critical) creature.attack() * 2 else creature.attack()) - target.stats.defense + calculateModifiers(creature, target)

        if(damage < 1) damage = 1

        target.damageCounters += damage

        if (target.health() < 0)
            target.damageCounters = target.stats.health
    }

    //lo voy a tener que sacar en algun momento, ya que no lo voy a poder comunicar al exterior
    //deberia caerle reactivamente
    private fun calculateModifiers(creature: Creature, target: Creature): Int {
        var damageModifier = 0
        if(creature is Frog)
            target.getTokens(Token.FROG)?.let {
                damageModifier += it
                target.removeTokens(Token.FROG, it)
            }

        if(target is Golem)
            target.getTokens(Token.GOLEM)?.let {
                damageModifier -= it
                target.removeTokens(Token.GOLEM, it)
            }

        return damageModifier
    }
}