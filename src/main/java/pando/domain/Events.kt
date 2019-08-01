package pando.domain

import pando.actions.Action
import pando.creatures.Creature
import pando.creatures.Frog

class Death(val creature: Creature)
class Kill(val killer: Creature)
class Damage(val actor: Creature, val action: Action, val target: Frog)
class Rest()