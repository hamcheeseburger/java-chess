package chess.domain.pieces;

import chess.domain.position.Position;

public final class Rook implements Type {

    @Override
    public boolean isMovable(final Position source, final Position target) {
        return source.isStraight(target);
    }

    @Override
    public boolean isPawn() {
        return false;
    }

    @Override
    public boolean isKing() {
        return false;
    }

    @Override
    public double score() {
        return 5;
    }

    @Override
    public String symbol() {
        return "R";
    }
}
