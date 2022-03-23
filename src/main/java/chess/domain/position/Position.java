package chess.domain.position;

import java.util.Objects;

public final class Position {

    private final Column column;
    private final Row row;

    private Position(Column column, Row row) {
        this.column = column;
        this.row = row;
    }

    public static Position of(final String value) {
        final var column = Column.of(value.substring(0, 1));
        final var row = Row.of(Integer.parseInt(value.substring(1)));

        return new Position(column, row);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return column == position.column && row == position.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, row);
    }

    public boolean isStraight(Position target) {
        return isVertical(target) || isHorizontal(target);
    }

    private boolean isVertical(Position target) {
        return column.isSame(target.column);
    }

    private boolean isHorizontal(Position target) {
        return row.isSame(target.row);
    }

    public boolean isDiagonal(Position target) {
        int columnGap = column.gap(target.column);
        int rowGap = row.gap(target.row);
        return columnGap == rowGap;
    }
}
