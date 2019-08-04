package pando.test

import pando.creatures.Creature
import pando.creatures.CreatureBehaviour
import pando.creatures.CreatureStats
import pando.creatures.Position

class TestCreature(stats: CreatureStats = TestCreatureStats(),
                   behaviour: CreatureBehaviour = TestCreatureBehaviour(),
                   position: Position = Position(1, 1),
                   team: Int = 0) : Creature(stats, behaviour, position, team) {

}

class TestCreatureBehaviour : CreatureBehaviour