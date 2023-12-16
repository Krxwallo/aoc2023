enum class Direction(val translation: (Pos) -> Pos) {
    UP({ (x, y) -> Pos(x, y - 1) }),
    DOWN({ (x, y) -> Pos(x, y + 1) }),
    LEFT({ (x, y) -> Pos(x - 1, y) }),
    RIGHT({ (x, y) -> Pos(x + 1, y) }),
}