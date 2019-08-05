package pando.creatures

data class Position(val line: Int, val column: Int) : Comparable<Position> {

    override fun compareTo(other: Position)= when {
        column < other.column -> -1
        column > other.column -> 1
        line < other.line -> -1
        line > other.line -> 1
        else -> 0
    }
}