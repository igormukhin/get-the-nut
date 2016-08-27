package de.igormukhin.getthenut.textmap;

import com.google.common.collect.ImmutableSet;
import de.igormukhin.getthenut.*;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.*;

import static com.google.common.base.Throwables.propagate;
import static java.util.Objects.requireNonNull;

public class TextBoardParser {

    private String map;

    private TextBoardParser(String map) {
        this.map = requireNonNull(map);
    }

    public static TextBoardParser of(String map) {
        return new TextBoardParser(map);
    }

    public Board parse() {
        List<String> lines = toLines(map);
        int height = lines.size();
        int width = lines.get(0).length();
        SpotType[][] spots = new SpotType[height][width];
        Set<Actor> actors = new HashSet<>();

        int row = 0;
        for (String line : lines) {
            for (int col = 0; col < width; col++) {
                char ch = line.charAt(col);

                Optional<SpotType> spotTypeOpt = TextBoard.resolveSpotType(ch);
                SpotType spotType = spotTypeOpt.orElse(SpotType.ROAD);
                spots[row][col] = spotType;

                Optional<ActorType> actor = TextBoard.resolveActorType(ch);
                if (actor.isPresent()) {
                    actors.add(Actor.of(actor.get(), Pos.of(row, col)));
                }

                if (!spotTypeOpt.isPresent() && !actor.isPresent()) {
                    throw new IllegalArgumentException(String.format("Unknown map character %c", ch));
                }
            }
            row++;
        }

        Land land = new Land(spots);
        ActorSet actorSet = new ActorSet(ImmutableSet.copyOf(actors));
        return new Board(land, actorSet);
    }

    private static List<String> toLines(String text) {
        LineNumberReader reader  = new LineNumberReader(new StringReader(text));
        ArrayList<String> lines = new ArrayList<>();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw propagate(e);
        }
        return lines;
    }


}
