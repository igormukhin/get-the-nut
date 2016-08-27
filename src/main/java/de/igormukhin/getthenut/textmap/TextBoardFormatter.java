package de.igormukhin.getthenut.textmap;

import de.igormukhin.getthenut.ActorSet;
import de.igormukhin.getthenut.Board;
import de.igormukhin.getthenut.Land;
import de.igormukhin.getthenut.Pos;

import static java.util.Objects.requireNonNull;

public class TextBoardFormatter {

    private final Board board;

    private TextBoardFormatter(Board board) {
        this.board = requireNonNull(board);
    }

    public static TextBoardFormatter of(Board board) {
        return new TextBoardFormatter(board);
    }

    public String format() {
        Land land = board.land();
        ActorSet actorSet = board.actorSet();
        String lineSeparator = System.lineSeparator();
        int expectedMapSize = land.height() * (land.width() + lineSeparator.length());
        StringBuffer sb = new StringBuffer(expectedMapSize);

        for (int row = 0; row < land.height(); row++) {
            if (row > 0) {
                sb.append(lineSeparator);
            }

            for (int col = 0; col < land.width(); col++) {
                Pos pos = Pos.of(row, col);

                char spot = TextBoard.formatSpotType(land.spotTypeAt(pos));

                sb.append(actorSet.actorTypeAt(pos)
                        .map(TextBoard::formatActorType)
                        .orElse(spot));

            }
        }

        return sb.toString();
    }

}
