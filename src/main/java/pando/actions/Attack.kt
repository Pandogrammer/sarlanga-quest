package pando.actions

import pando.creatures.Creature

class Attack(fatigue: Int = 1) : Action(fatigue) {

    override fun execute(attacker: Creature, objective: Creature, critical: Boolean) {
        val damage = if(critical) attacker.attack * 2 else attacker.attack

        objective.damage += (damage - objective.defense)

        if (objective.health() < 0)
            objective.damage = objective.initialHealth
    }
}