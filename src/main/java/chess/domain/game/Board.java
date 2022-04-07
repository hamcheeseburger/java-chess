package chess.domain.game;

import chess.dao.Member;
import chess.domain.pieces.Color;

import java.util.ArrayList;
import java.util.List;

public final class Board {

    private final int id;
    private final String roomTitle;
    private final Color turn;
    private final List<Member> members;

    public Board(int id, String roomTitle, Color turn, List<Member> members) {
        this.id = id;
        this.roomTitle = roomTitle;
        this.turn = turn;
        this.members = members;
    }

    public Board(int roomId, String roomTitle, Color turn) {
        this(roomId, roomTitle, turn, new ArrayList<>());
    }

    public Board(String roomTitle, Color turn, List<Member> members) {
        this(0, roomTitle, turn, members);
    }

    public Board(String roomTitle) {
        this(0, roomTitle, Color.WHITE);
    }

    public int getId() {
        return id;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public Color getTurn() {
        return turn;
    }

    public List<Member> getMembers() {
        return members;
    }
}
