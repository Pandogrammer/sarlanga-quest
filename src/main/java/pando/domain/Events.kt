package pando.domain

import pando.actions.Action
import pando.creatures.Creature

class Death(val creature: Creature)
class Kill(val killer: Creature)
class Damage(val actor: Creature, val action: Action, val target: Creature)
class Rest