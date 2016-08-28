package de.igormukhin.getthenut;

import de.igormukhin.getthenut.textmap.TextBoardFormatter;

import static java.util.Objects.requireNonNull;

public class Board {

    private final Land land;
    private final ActorSet actorSet;

    public Board(Land land, ActorSet actorSet) {
        this.land = requireNonNull(land);
        this.actorSet = requireNonNull(actorSet);
    }

    public Land land() {
        return land;
    }

    public ActorSet actorSet() {
        return actorSet;
    }

    @Override
    public String toString() {
        return TextBoardFormatter.of(this).format();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        if (!land.equals(board.land)) return false;
        return actorSet.equals(board.actorSet);

    }

    @Override
    public int hashCode() {
        int result = land.hashCode();
        result = 31 * result + actorSet.hashCode();
        return result;
    }
}
