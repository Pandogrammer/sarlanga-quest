package pando.creatures

import pando.creatures.CreatureCode.*
import pando.creatures.cards.*

enum class CreatureCode {
    EYE, SKELETON;
}

class CreatureCodeMapper {

    fun toCard(creatureCode: CreatureCode): CreatureCard {
        return when(creatureCode){
            EYE -> EyeCard()
            SKELETON -> SkeletonCard()
        }
    }
}