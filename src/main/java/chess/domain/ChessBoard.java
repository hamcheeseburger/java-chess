package chess.domain;

import chess.domain.dto.PieceDto;
import chess.domain.dto.PositionDto;
import chess.domain.piece.*;
import chess.domain.piece.info.*;

import java.util.*;
import java.util.stream.Collectors;

public class ChessBoard {
    private static final String OBSTACLE_ERROR = "[ERROR] 기물을 뛰어 넘어 이동할 수 없습니다.";
    private static final String SAME_COLOR_ERROR = "[ERROR] taget에 같은 편 말이 있습니다.";
    private static final String NOT_MOVE_ERROR = "[ERROR] 올바른 이동이 아닙니다.";
    private static final String EMPTY_ERROR = "[ERROR] 체스말이 없습니다.";
    private Map<Position, Piece> chessBoard;

    public ChessBoard(Map<Position, Piece> chessBoard) {
        this.chessBoard = chessBoard;
    }

    public static ChessBoard generate() {
        Map<Position, Piece> chessBoard = new HashMap<>();
        chessBoard.putAll(King.generate());
        chessBoard.putAll(Queen.generate());
        chessBoard.putAll(Knight.generate());
        chessBoard.putAll(Rook.generate());
        chessBoard.putAll(Bishop.generate());
        chessBoard.putAll(Pawn.generate());
        return new ChessBoard(chessBoard);
    }

    public Map<Position, Piece> getChessBoard() {
        return Collections.unmodifiableMap(chessBoard);
    }

    public Piece findByPosition(Position position) {
        return chessBoard.getOrDefault(position, Empty.EMPTY);
    }

    public boolean isAliveAllKings() {
        return (int) chessBoard.values().stream()
                .filter(piece -> piece.isKing())
                .count() == 2;
    }

    public double sumScoreByColor(Color color) {
        Map<Character, Long> pawns = collectPawnCountsByRow(color);
        int count = (int) pawns.keySet().stream()
                .filter(character -> pawns.get(character) >= 2)
                .mapToLong(character -> pawns.get(character))
                .sum();
        return chessBoard.values().stream()
                .filter(piece -> piece.isSameColor(color))
                .mapToDouble(piece -> piece.getScore().getValue())
                .sum() - (count * 0.5);
    }

    private Map<Character, Long> collectPawnCountsByRow(Color color) {
        return chessBoard.entrySet().stream()
                .filter(entry -> entry.getValue().isPawn())
                .filter(entry -> entry.getValue().isSameColor(color))
                .collect(Collectors.groupingBy(entry -> entry.getKey().getX(), Collectors.counting()));
    }

    public void movePiece(Position source, Position target) {
        Piece sourcePiece = findByPosition(source);
        validateEmpty(sourcePiece);
        validateMovable(sourcePiece, source, target);
        validateSameColor(source, target);
        chessBoard.remove(source);
        chessBoard.put(target, sourcePiece);
    }

    private void validateEmpty(Piece sourcePiece) {
        if (sourcePiece.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_ERROR);
        }
    }

    private void validateMovable(Piece sourcePiece, Position source, Position target) {
        if (!sourcePiece.canMove(source, target)) {
            throw new IllegalArgumentException(NOT_MOVE_ERROR);
        }
        validatePieceRoute(sourcePiece, source, target);
    }

    private void validatePieceRoute(Piece sourcePiece, Position source, Position target) {
        if (sourcePiece.isKnight()) {
            return;
        }
        if (!sourcePiece.isKnight() && !sourcePiece.isPawn()) {
            validateCrossOrDiagonalRoute(source, target);
            return;
        }
        Pawn pawn = (Pawn) sourcePiece;
        if (pawn.isAttackAble(source, target)) {
            validateEmpty(findByPosition(target));
        }
        if (!pawn.isAttackAble(source, target)) {
            validateCrossOrDiagonalRoute(source, target);
        }
    }

    private void validateCrossOrDiagonalRoute(Position source, Position target) {
        if (source.isCross(target)) {
            Direction cross = Cross.findCrossByTwoPosition(source, target);
            hasPieceInPath(source, target, cross);
        }
        if (source.isDiagonal(target)) {
            Direction diagonal = Diagonal.findDiagonalByTwoPosition(source, target);
            hasPieceInPath(source, target, diagonal);
        }
    }

    public void validateSameColor(Position source, Position target) {
        if (findByPosition(source).isSameColor(findByPosition(target))) {
            throw new IllegalArgumentException(SAME_COLOR_ERROR);
        }
    }

    public void hasPieceInPath(Position source, Position target, Direction direction) {
        int sourceX = source.getX();
        int sourceY = source.getY();
        int count = checkDistance(source, target);
        for (int i = 1; i < count; i++) {
            validatePieceInPath(sourceX, sourceY, i, direction);
        }
    }

    private int checkDistance(Position source, Position target) {
        int count = Math.max(Math.abs(source.subtractX(target)), Math.abs(source.subtractY(target)));
        if (findByPosition(source).isPawn()) {
            count++;
        }
        return count;
    }

    private void validatePieceInPath(int sourceX, int sourceY, int count, Direction direction) {
        if (hasSamePositionPiece(
                Position.of((char) (sourceX + (direction.getChangeValues()[0] * count)), (char) (sourceY + (direction.getChangeValues()[1] * count))))) {
            throw new IllegalArgumentException(OBSTACLE_ERROR);
        }
    }

    public boolean hasSamePositionPiece(Position target) {
        return chessBoard.keySet().stream()
                .anyMatch(position -> position.equals(target));
    }

    public List<Position> routes(Piece sourcePiece, Position source) {
        List<Position> routes = new ArrayList<>();
        for (Position target : Position.POSITIONS) {
            try {
                validateMovable(sourcePiece, source, target);
                validateSameColor(source, target);
                routes.add(target);
            } catch (Exception e) {
                continue;
            }
        }
        return routes;
    }

    public void sync(Map<PositionDto, PieceDto> chessBoard) {
        this.chessBoard.clear();
        for (PositionDto positionDto : chessBoard.keySet()) {
            Position position = Position.of(positionDto.getPosition().charAt(0), positionDto.getPosition().charAt(1));
            String pieceName = chessBoard.get(positionDto).getName();
            String pieceColor = chessBoard.get(positionDto).getColor();
            Piece piece = piece(pieceName, pieceColor);
            this.chessBoard.put(position, piece);
        }
    }

    private Piece piece(String pieceName, String pieceColor) {
        if (pieceName.equalsIgnoreCase("K")) {
            return new King(pieceName, Color.from(pieceColor));
        }
        if (pieceName.equalsIgnoreCase("Q")) {
            return new Queen(pieceName, Color.from(pieceColor));
        }
        if (pieceName.equalsIgnoreCase("N")) {
            return new Knight(pieceName, Color.from(pieceColor));
        }
        if (pieceName.equalsIgnoreCase("B")) {
            return new Bishop(pieceName, Color.from(pieceColor));
        }
        if (pieceName.equalsIgnoreCase("R")) {
            return new Rook(pieceName, Color.from(pieceColor));
        }
        return new Pawn(pieceName, Color.from(pieceColor));
    }
}