package pando.test

import pando.creatures.*

class CreatureBuilder{
    private var health: Int = 1
    private var attack: Int = 0
    private var defense: Int = 0
    private var speed: Int = 0
    private var dexterity: Int = 0
    private var essence: Int = 0

    private var id: Int = 0
    private var team: Int = 0
    private var position: Position = Position(1, 1)

    fun health(health: Int) : CreatureBuilder {
        this.health = health
        return this
    }

    fun attack(attack: Int) : CreatureBuilder{
        this.attack = attack
        return this
    }

    fun speed(speed: Int) : CreatureBuilder{
        this.speed = speed
        return this
    }

    fun defense(defense: Int) : CreatureBuilder{
        this.defense = defense
        return this
    }

    fun dexterity(dexterity: Int) : CreatureBuilder{
        this.dexterity = dexterity
        return this
    }

    fun essence(essence: Int) : CreatureBuilder{
        this.essence = essence
        return this
    }

    fun team(team: Int) : CreatureBuilder{
        this.team = team
        return this
    }

    fun position(position: Position) : CreatureBuilder {
        this.position = position
        return this
    }

    fun build() = SpawnedCreature(
            id,
            position,
            team,
            CreatureStats(health, attack, defense, speed, dexterity, essence),
            null, null
    )

}



