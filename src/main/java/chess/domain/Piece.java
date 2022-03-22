package chess.domain;

import chess.domain.pieces.Type;

public final class Piece {
    private final Color color;
    private final Type type;

    public Piece(Color color, Type type) {
        this.color = color;
        this.type = type;
    }
}
