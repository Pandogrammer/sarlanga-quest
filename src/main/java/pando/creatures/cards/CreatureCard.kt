package pando.creatures.cards

import pando.creatures.CreatureCode
import pando.creatures.CreatureCode.*
import pando.creatures.CreatureStats
import pando.creatures.races.*

sealed class CreatureCard(val name: String,
                          val stats: CreatureStats,
                          val skillDescription: String,
                          val actions: List<CreatureAction>) {
    abstract val code: CreatureCode
}

data class EyeCard(override val code: CreatureCode = EYE) : CreatureCard(
        "Eye", EyeStats(),
        "Cada vez que el ojo mata a una criatura, su ataque aumenta en 1",
        listOf(NormalAttack()))

data class SkeletonCard(override val code: CreatureCode = SKELETON) : CreatureCard(
        "Skeleton", SkeletonStats(),
        "Cuando muere, el esqueleto vuelve a la vida luego de 7 turnos",
        listOf(NormalAttack()))

data class FrogCard(override val code: CreatureCode = FROG) : CreatureCard(
        "Frog", FrogStats(),
        "caca",
        listOf(NormalAttack()))

data class FluffyCard(override val code: CreatureCode = FLUFFY) : CreatureCard(
        "Fluffy", FluffyStats(),
        "caca",
        listOf(SlowAttack()))

data class GolemCard(override val code: CreatureCode = GOLEM) : CreatureCard(
        "Golem", GolemStats(),
        "caca",
        listOf(SlowAttack()))

