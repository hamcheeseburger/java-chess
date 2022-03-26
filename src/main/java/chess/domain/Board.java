package chess.domain;

import chess.domain.position.Column;
import chess.domain.position.Direction;
import chess.domain.position.Position;
import chess.domain.position.Row;

import java.util.*;

public final class Board {

    private static final double PAWN_PENALTY_SCORE = 0.5;

    private final Map<Position, Piece> pieces;

    public Board(final Initializer initiator) {
        pieces = initiator.initialize();
    }

    public Optional<Piece> piece(final Position position) {
        if (pieces.containsKey(position)) {
            return Optional.of(pieces.get(position));
        }
        return Optional.empty();
    }

    public boolean move(final Position sourcePosition, final Position targetPosition) {
        final Piece piece = findPiece(sourcePosition);
        validateTargetNotSameColor(targetPosition, piece);

        return movePiece(sourcePosition, targetPosition, piece);
    }

    private Piece findPiece(final Position sourcePosition) {
        final Optional<Piece> wrappedPiece = piece(sourcePosition);
        if (wrappedPiece.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 말이 존재하지 않습니다.");
        }
        return wrappedPiece.get();
    }

    private void validateTargetNotSameColor(final Position targetPosition, final Piece piece) {
        if (pieces.containsKey(targetPosition) && piece.isSameColorPiece(findPiece(targetPosition))) {
            throw new IllegalArgumentException("[ERROR] 목적지에 같은 색의 기물이 있으면 움직일 수 없다.");
        }
    }

    private boolean movePiece(final Position sourcePosition, final Position targetPosition, final Piece piece) {
        final boolean movable = piece.isMovable(sourcePosition, targetPosition);
        if (movable) {
            checkPawnMovement(sourcePosition, targetPosition, piece);
            validatePathEmpty(sourcePosition, targetPosition);
            pieces.remove(sourcePosition);
            pieces.put(targetPosition, piece);
        }
        return movable;
    }

    private void validatePathEmpty(final Position source, final Position target) {
        final Direction direction = Direction.calculate(source, target);
        if (!direction.isIgnore()) {
            List<Position> positions = source.calculatePath(target, direction);
            validatePiecesNotExistOnPath(positions);
        }
    }

    private void validatePiecesNotExistOnPath(final List<Position> positions) {
        for (Position position : positions) {
            validatePieceNotExist(position);
        }
    }

    private void validatePieceNotExist(final Position position) {
        if (pieces.containsKey(position)) {
            throw new IllegalArgumentException("[ERROR] 이동경로에 다른 기물이 있으면 움직일 수 없다.");
        }
    }

    private void checkPawnMovement(final Position sourcePosition, final Position targetPosition, final Piece piece) {
        if (piece.isPawn() && Direction.calculate(sourcePosition, targetPosition) == Direction.DIAGONAL) {
            checkPawnTargetExist(targetPosition);
        }
        if (piece.isPawn() && Direction.calculate(sourcePosition, targetPosition) == Direction.VERTICAL) {
            checkPawnTargetNotExist(targetPosition);
        }
    }

    private void checkPawnTargetExist(final Position targetPosition) {
        if (!pieces.containsKey(targetPosition)) {
            throw new IllegalArgumentException("[ERROR] 폰은 상대기물이 목적지에 존재해야 대각선으로 움직일 수 있다.");
        }
    }

    private void checkPawnTargetNotExist(final Position targetPosition) {
        if (pieces.containsKey(targetPosition)) {
            throw new IllegalArgumentException("[ERROR] 폰은 직진할 때 다른 기물이 존재하는 목적지에 이동할 수 없다.");
        }
    }

    public boolean isEnd() {
        return pieces.values()
                .stream()
                .filter(Piece::isKing)
                .count() != 2;
    }

    public double calculateScore(final Color color) {
        double score = pieces.values()
                .stream()
                .filter(piece -> piece.isSameColor(color))
                .mapToDouble(Piece::score)
                .sum();
        return score - countPawnsOnSameColumns(color) * PAWN_PENALTY_SCORE;
    }

    private int countPawnsOnSameColumns(final Color color) {
        return Arrays.stream(Column.values())
                .mapToInt(column -> countPawnsOnSameColumn(column, color))
                .filter(count -> count > 1)
                .sum();
    }

    private int countPawnsOnSameColumn(final Column column, final Color color) {
        return (int) Arrays.stream(Row.values())
                .map(row -> piece(Position.valueOf(column, row)))
                .filter(piece -> piece.isPresent() && piece.get().isPawn() && piece.get().isSameColor(color))
                .count();
    }

    public Map<Result, Color> whoIsWin() {
        Map<Result, Color> gameResult = new HashMap<>();
        if (calculateScore(Color.WHITE) > calculateScore(Color.BLACK)) {
            gameResult.put(Result.WIN, Color.WHITE);
        }
        if (calculateScore(Color.WHITE) < calculateScore(Color.BLACK)) {
            gameResult.put(Result.WIN, Color.WHITE);
        }
        return gameResult;
    }
}
