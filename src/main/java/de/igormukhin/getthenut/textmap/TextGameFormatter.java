package de.igormukhin.getthenut.textmap;

import de.igormukhin.getthenut.Game;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class TextGameFormatter {

    private Game game;

    private TextGameFormatter(Game game) {
        this.game = game;
    }

    public static TextGameFormatter of(Game game) {
        return new TextGameFormatter(game);
    }


    public String format() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        format(game.rollsAsGameList(), pw);

        pw.flush();
        return sw.toString();
    }

    private void format(List<Game> games, PrintWriter pw) {
        games.forEach(game -> format(game, pw));
    }

    private void format(Game game, PrintWriter pw) {
        pw.printf(">> Rolls: %d", game.rolls());
        if (game.ended()) {
            pw.printf("\t%s", game.won() ? "WON" : "LOST");
        }
        pw.println();

        pw.println(TextBoard.format(game));
        pw.println();
    }

}