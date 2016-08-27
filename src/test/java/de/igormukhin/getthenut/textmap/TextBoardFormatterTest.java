package de.igormukhin.getthenut.textmap;

import com.google.common.collect.ImmutableSet;
import de.igormukhin.getthenut.*;
import org.junit.Test;

import static de.igormukhin.getthenut.ActorType.NUT;
import static de.igormukhin.getthenut.ActorType.SQUIRREL;
import static de.igormukhin.getthenut.SpotType.FORREST;
import static de.igormukhin.getthenut.SpotType.ROAD;
import static org.assertj.core.api.Assertions.assertThat;

public class TextBoardFormatterTest {

    @Test
    public void board1() {
        // given
        SpotType[][] spots = {
            { FORREST, FORREST, FORREST, FORREST },
            { FORREST, ROAD, ROAD, FORREST },
            { FORREST, ROAD, ROAD, FORREST },
            { FORREST, FORREST, FORREST, FORREST },
        };
        ImmutableSet<Actor> actors = ImmutableSet.of(
                Actor.of(NUT, Pos.of(1, 2)),
                Actor.of(SQUIRREL, Pos.of(2, 1))
        );
        Board board = new Board(new Land(spots), new ActorSet(actors));

        // when
        String map = TextBoardFormatter.of(board).format();

        // then
        assertThat(map).isEqualTo(String.format(
                "####%n" +
                "# n#%n" +
                "#s #%n" +
                "####"));
    }

}