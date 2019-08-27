package pando.actions

import pando.creatures.SpawnedCreature

class Attack(fatigue: Int = 1) : Action(fatigue) {
    override val melee: Boolean = true

    //esto deberia transformarse en Damage, y que la accion invoque el efecto
    override fun execute(spawnedCreature: SpawnedCreature, target: SpawnedCreature, critical: Boolean) {
        var damage = (if(critical) spawnedCreature.attack() * 2 else spawnedCreature.attack()) - target.stats.defense

        if(damage < 1) damage = 1

        target.damageCounters += damage

        if (target.health() < 0)
            target.damageCounters = target.stats.health
    }

}