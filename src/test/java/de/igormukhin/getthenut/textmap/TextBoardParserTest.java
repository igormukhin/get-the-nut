package de.igormukhin.getthenut.textmap;

import de.igormukhin.getthenut.*;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TextBoardParserTest {

    @Test
    public void loads() {
        // given
        String map = String.format(
                "####%n" +
                "# n#%n" +
                "#s #%n" +
                "####");

        // when
        Board board = TextBoardParser.of(map).parse();

        // then
        assertThat(board.land().width()).isEqualTo(4);
        assertThat(board.land().height()).isEqualTo(4);
        assertThat(board.land().spotTypeAt(Pos.of(3, 2))).isEqualTo(SpotType.FORREST);
        assertThat(board.land().spotTypeAt(Pos.of(2, 2))).isEqualTo(SpotType.ROAD);
        assertThat(board.actorSet().actorsCount()).isEqualTo(2);
        assertThat(board.actorSet().actorTypeAt(Pos.of(1, 2))).contains(ActorType.NUT);
        assertThat(board.actorSet().actorTypeAt(Pos.of(2, 1))).contains(ActorType.SQUIRREL);
    }

}