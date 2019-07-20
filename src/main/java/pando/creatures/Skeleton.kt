package pando.creatures

class Skeleton(team: Int = 0) : Creature(initialHealth = 6, initialAttack = 1, speed = 1, team = team, dexterity = 3){

    override fun toString(): String {
        return "SKELETON"
    }
}