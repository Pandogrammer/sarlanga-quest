package pando.test

import pando.creatures.CreatureStats

class TestCreatureStats(health: Int = 1,
                        attack: Int = 0,
                        defense: Int = 0,
                        speed: Int = 0,
                        dexterity: Int = 0) :
        CreatureStats(health, attack, defense, speed, dexterity)