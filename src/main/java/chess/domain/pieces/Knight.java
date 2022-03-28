package chess.domain.pieces;

import chess.domain.position.Position;

public final class Knight implements Type {

    private static final int FIRST_MOVEMENT_LIMIT = 2;
    private static final int SECOND_MOVEMENT_LIMIT = 1;

    @Override
    public boolean isMovable(final Position source, final Position target) {
        final int columnGap = source.columnGap(target);
        final int rowGap = source.rowGap(target);
        return moveVertical(columnGap, rowGap) || moveHorizontal(columnGap, rowGap);
    }

    private boolean moveHorizontal(final int columnGap, final int rowGap) {
        return rowGap == FIRST_MOVEMENT_LIMIT && columnGap == SECOND_MOVEMENT_LIMIT;
    }

    private boolean moveVertical(final int columnGap, final int rowGap) {
        return columnGap == FIRST_MOVEMENT_LIMIT && rowGap == SECOND_MOVEMENT_LIMIT;
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
        return 2.5;
    }

    @Override
    public String symbol() {
        return "N";
    }
}