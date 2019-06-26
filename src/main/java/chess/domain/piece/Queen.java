package chess.domain.piece;

import chess.domain.Direction;
import chess.domain.MoveRule;
import chess.domain.Position;
import chess.domain.Team;

import java.util.Optional;

public class Queen extends Piece {
    private static final String NAME = "q";
    private static final double SCORE = 9;

    public Queen(Team team) {
        super(team);
    }

    @Override
    protected MoveRule setMoveRule() {
        return this::queen;
    }

    private boolean queen(Position source, Position target, Optional<Team> optionalTargetPieceTeam) {
        Direction direction = source.direction(target);
        validDirection(Direction.ALL_DIRECTION, direction);
        validSameTeamCatch(optionalTargetPieceTeam);
        return true;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public double getScore() {
        return SCORE;
    }
}