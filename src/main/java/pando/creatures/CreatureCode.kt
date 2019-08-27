package pando.creatures

import pando.creatures.CreatureCode.*
import pando.creatures.cards.*

enum class CreatureCode {
    EYE, SKELETON, FROG, FLUFFY, GOLEM;
}

class CreatureCodeMapper {

    fun toCard(creatureCode: CreatureCode): CreatureCard {
        return when(creatureCode){
            EYE -> EyeCard()
            SKELETON -> SkeletonCard()
            FLUFFY -> FluffyCard()
            GOLEM -> GolemCard()
            FROG -> FrogCard()
        }
    }
}