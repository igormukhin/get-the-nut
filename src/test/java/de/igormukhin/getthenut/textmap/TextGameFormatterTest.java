package de.igormukhin.getthenut.textmap;

import de.igormukhin.getthenut.Direction;
import de.igormukhin.getthenut.Game;
import de.igormukhin.getthenut.Pos;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TextGameFormatterTest {

    @Test
    public void formatNoRolls() {
        // given
        String map = String.format(
                "#####%n" +
                "#s###%n" +
                "#   #%n" +
                "#  n#%n" +
                "#####");
        Game game = TextBoard.parse(map);

        // when
        String display = TextGameFormatter.of(game).format();

        // then
        assertThat(display).isEqualTo(String.format(
                ">> Rolls: 0%n" +
                "#####%n" +
                "#s###%n" +
                "#   #%n" +
                "#  n#%n" +
                "#####%n" +
                "%n"));

    }

    @Test
    public void formatTwoRollsWin() {
        // given
        String map = String.format(
                "#####%n" +
                "#s###%n" +
                "#   #%n" +
                "#  n#%n" +
                "#####");
        Game game = TextBoard.parse(map);
        game = game.roll(Pos.of(1, 1), Direction.SOUTH);
        game = game.roll(Pos.of(3, 1), Direction.EAST);

        // when
        String display = TextGameFormatter.of(game).format();

        // then
        assertThat(display).isEqualTo(String.format(
                ">> Rolls: 0%n" +
                "#####%n" +
                "#s###%n" +
                "#   #%n" +
                "#  n#%n" +
                "#####%n" +
                "%n" +
                ">> Rolls: 1%n" +
                "#####%n" +
                "# ###%n" +
                "#   #%n" +
                "#s n#%n" +
                "#####%n" +
                "%n" +
                ">> Rolls: 2\tWON%n" +
                "#####%n" +
                "# ###%n" +
                "#   #%n" +
                "# s #%n" +
                "#####%n" +
                "%n"
        ));

    }

    @Test
    public void formatOneRollLost() {
        // given
        String map = String.format(
                "#####%n" +
                "#s###%n" +
                "#   #%n" +
                "#m n#%n" +
                "#####");
        Game game = TextBoard.parse(map);
        game = game.roll(Pos.of(3, 1), Direction.EAST);

        // when
        String display = TextGameFormatter.of(game).format();

        // then
        assertThat(display).isEqualTo(String.format(
                ">> Rolls: 0%n" +
                "#####%n" +
                "#s###%n" +
                "#   #%n" +
                "#m n#%n" +
                "#####%n" +
                "%n" +
                ">> Rolls: 1\tLOST%n" +
                "#####%n" +
                "#s###%n" +
                "#   #%n" +
                "# m #%n" +
                "#####%n" +
                "%n"
        ));

    }
}