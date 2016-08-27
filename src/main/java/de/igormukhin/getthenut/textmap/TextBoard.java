package de.igormukhin.getthenut.textmap;

import com.google.common.collect.ImmutableBiMap;
import de.igormukhin.getthenut.ActorType;
import de.igormukhin.getthenut.Game;
import de.igormukhin.getthenut.SpotType;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class TextBoard {

    private static final ImmutableBiMap<Character, SpotType> char2spotType;
    private static final ImmutableBiMap<Character, ActorType> char2actorType;
    static {
        char2spotType = ImmutableBiMap.<Character, SpotType>builder()
                .put(' ', SpotType.ROAD)
                .put('#', SpotType.FORREST)
                .put('x', SpotType.SWAMP)
                .build();

        char2actorType = ImmutableBiMap.<Character, ActorType>builder()
                .put('n', ActorType.NUT)
                .put('s', ActorType.SQUIRREL)
                .put('m', ActorType.MOUSE)
                .put('b', ActorType.BOAR)
                .build();
    }

    public static Optional<SpotType> resolveSpotType(char ch) {
        return Optional.ofNullable(char2spotType.get(ch));
    }

    public static Optional<ActorType> resolveActorType(char ch) {
        return Optional.ofNullable(char2actorType.get(ch));
    }

    public static char formatSpotType(SpotType spotType) {
        return requireNonNull(char2spotType.inverse().get(spotType));
    }

    public static char formatActorType(ActorType actorType) {
        return requireNonNull(char2actorType.inverse().get(actorType));
    }

    public static Game parse(String map) {
        return Game.start(TextBoardParser.of(map).parse());
    }

    public static String format(Game game) {
        return TextBoardFormatter.of(game.board()).format();
    }
}
