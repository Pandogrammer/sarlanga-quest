package pando.creatures.cards

import pando.creatures.CreatureCardAction
import pando.creatures.CreatureCardAction.*
import pando.creatures.CreatureCode
import pando.creatures.CreatureStats
import pando.creatures.races.*

sealed class CreatureCard(val name: String,
                          val stats: CreatureStats,
                          val skillDescription: String,
                          val actions: List<CreatureCardAction>){
    abstract val creatureCode: CreatureCode
}

data class EyeCard(override val creatureCode: CreatureCode = CreatureCode.EYE) : CreatureCard(
        "Eye", EyeStats(),
        "Cada vez que el ojo mata a una criatura, su ataque aumenta en 1",
        listOf(NORMAL_ATTACK))

data class SkeletonCard(override val creatureCode: CreatureCode = CreatureCode.SKELETON): CreatureCard(
        "Skeleton", SkeletonStats(),
        "Cuando muere, el esqueleto vuelve a la vida luego de 7 turnos",
        listOf(NORMAL_ATTACK))

data class FrogCard(override val creatureCode: CreatureCode = CreatureCode.FROG): CreatureCard(
        "Frog", FrogStats(),
        "caca",
        listOf(NORMAL_ATTACK))

data class FluffyCard(override val creatureCode: CreatureCode = CreatureCode.FLUFFY): CreatureCard(
        "Fluffy", FluffyStats(),
        "caca",
        listOf(NORMAL_ATTACK))

data class GolemCard(override val creatureCode: CreatureCode = CreatureCode.GOLEM): CreatureCard(
        "Golem", GolemStats(),
        "caca",
        listOf(NORMAL_ATTACK))

