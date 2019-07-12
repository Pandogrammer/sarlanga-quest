package actions

import creatures.Creature

class Attack(fatigue: Int = 1) : Action(fatigue) {

    override fun execute(attacker: Creature, objective: Creature, critical: Boolean) {
        val damage = if(critical) attacker.attack * 2 else attacker.attack

        objective.health = objective.health - (damage - objective.defense)

        if (objective.health < 0)
            objective.health = 0
    }
}