package domain

interface Matchs {
    fun find(matchId: Int): Match?
    fun add(match: Match): Int
}