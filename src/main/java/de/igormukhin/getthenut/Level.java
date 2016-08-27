package de.igormukhin.getthenut;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import de.igormukhin.getthenut.textmap.TextBoardParser;

import java.io.IOException;
import java.net.URL;

import static com.google.common.base.Throwables.propagate;

public class Level {

    private final int location;
    private final int levelNum;
    private final Board board;

    private Level(int location, int levelNum) {
        this.location = location;
        this.levelNum = levelNum;

        String map = loadMap();
        this.board = TextBoardParser.of(map).parse();
    }

    private String loadMap() {
        URL url = Resources.getResource(getResourceName());
        try {
            return Resources.toString(url, Charsets.UTF_8);
        } catch (IOException e) {
            throw propagate(e);
        }
    }

    private String getResourceName() {
        return String.format("levels/level-%d-%d.txt", location, levelNum);
    }

    public static Level of(int location, int num) {
        return new Level(location, num);
    }

    public Game start() {
        return Game.start(board);
    }
}
