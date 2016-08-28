package de.igormukhin.getthenut.solve;

import de.igormukhin.getthenut.Game;
import de.igormukhin.getthenut.Pos;
import de.igormukhin.getthenut.textmap.TextBoard;
import de.igormukhin.getthenut.textmap.TextGameFormatter;
import org.junit.Test;

import static de.igormukhin.getthenut.Direction.EAST;
import static de.igormukhin.getthenut.Direction.NORTH;
import static de.igormukhin.getthenut.Direction.WEST;
import static org.assertj.core.api.Assertions.assertThat;

public class SolveHelperTest {

    @Test
    public void rebaseWorks() {
        // given
        String map = String.format(
                "#####%n" +
                "#  n#%n" +
                "### #%n" +
                "# s #%n" +
                "#####");
        Game initialGame = TextBoard.parse(map);

        Game slowSolution = initialGame.roll(Pos.of(3, 2), WEST);
        slowSolution = slowSolution.roll(Pos.of(3, 1), EAST);
        slowSolution = slowSolution.roll(Pos.of(3, 3), NORTH);

        Game fastToPoint = initialGame.roll(Pos.of(3, 2), EAST);

        // when
        Game betterSolution = SolveHelper.rebase(slowSolution, fastToPoint);

        // then
        assertThat(betterSolution.rolls()).isEqualTo(2);
        assertThat(TextGameFormatter.of(betterSolution).format()).isEqualTo(String.format(
                ">> Rolls: 0%n" +
                "#####%n" +
                "#  n#%n" +
                "### #%n" +
                "# s #%n" +
                "#####%n" +
                "%n" +
                ">> Rolls: 1%n" +
                "#####%n" +
                "#  n#%n" +
                "### #%n" +
                "#  s#%n" +
                "#####%n" +
                "%n" +
                ">> Rolls: 2\tWON%n" +
                "#####%n" +
                "#   #%n" +
                "###s#%n" +
                "#   #%n" +
                "#####%n" +
                "%n"));
    }

}