package pando.creatures.cards

import pando.actions.Action
import pando.actions.Attack
import pando.creatures.CreatureCode
import pando.creatures.CreatureStats
import pando.creatures.races.EyeStats
import pando.creatures.races.SkeletonStats

sealed class CreatureCard(val name: String,
                          val stats: CreatureStats,
                          val skillDescription: String,
                          val actions: List<Action>){
    abstract val creatureCode: CreatureCode
}

data class EyeCard(override val creatureCode: CreatureCode = CreatureCode.EYE) : CreatureCard(
        "Eye", EyeStats(),
        "Cada vez que el ojo mata a una criatura, su ataque aumenta en 1",
        listOf(Attack()))

data class SkeletonCard(override val creatureCode: CreatureCode = CreatureCode.SKELETON): CreatureCard(
        "Skeleton", SkeletonStats(),
        "Cuando muere, el esqueleto vuelve a la vida luego de 7 turnos",
        listOf(Attack()))