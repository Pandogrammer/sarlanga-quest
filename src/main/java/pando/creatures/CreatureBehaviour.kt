package pando.creatures

import pando.domain.Events

interface CreatureBehaviour {
    val creature: Creature
    val events: Events
}
