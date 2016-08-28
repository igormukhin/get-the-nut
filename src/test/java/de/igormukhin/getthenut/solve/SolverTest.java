package de.igormukhin.getthenut.solve;

import de.igormukhin.getthenut.Game;
import de.igormukhin.getthenut.Level;
import de.igormukhin.getthenut.textmap.TextBoard;
import de.igormukhin.getthenut.textmap.TextGameFormatter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static java.util.concurrent.TimeUnit.HOURS;
import static org.assertj.core.api.Assertions.assertThat;

public class SolverTest {

    @Rule
    public Timeout globalTimeout = new Timeout(1, HOURS);

    @Test
    public void findOneRollSolution() {
        // given
        String map = String.format(
                "#####%n" +
                "#s n#%n" +
                "#####");
        Solver solver = Solver.of(TextBoard.parse(map));

        // when
        Game solution = solver.solve();

        // then
        assertThat(solution.rolls()).isEqualTo(1);
        assertThat(TextBoard.format(solution)).isEqualTo(String.format(
                "#####%n" +
                "# s #%n" +
                "#####"));
    }

    @Test
    public void findTwoRollsSolution() {
        // given
        String map = String.format(
                "#####%n" +
                "#s###%n" +
                "#   #%n" +
                "#  n#%n" +
                "#####");
        Solver solver = Solver.of(TextBoard.parse(map));

        // when
        Game solution = solver.solve();

        // then
        assertThat(solution.rolls()).isEqualTo(2);
        assertThat(TextBoard.format(solution)).isEqualTo(String.format(
                "#####%n" +
                "# ###%n" +
                "#   #%n" +
                "# s #%n" +
                "#####"));
    }

    @Test
    public void solvesLevelSwamp1in5rolls() {
        // given
        Solver solver = Solver.of(Level.of(3, 1).start());

        // when
        Game solution = solver.solve();

        // then
        System.out.println(TextGameFormatter.of(solution).format());
        assertThat(solution.rolls()).isEqualTo(5);
    }

    @Test
    public void solvesLevelSwamp3in9rolls() {
        // given
        Solver solver = Solver.of(Level.of(3, 3).start());

        // when
        Game solution = solver.solve();

        // then
        System.out.println(TextGameFormatter.of(solution).format());
        assertThat(solution.rolls()).isEqualTo(9);
    }

    @Test
    public void solvesLevelSwamp3in36in34rolls() {
        // given
        Solver solver = Solver.of(Level.of(3, 36).start());

        // when
        Game solution = solver.solve(new SolverProgressPrinter());

        // then
        System.out.println(TextGameFormatter.of(solution).format());
        assertThat(solution.rolls()).isEqualTo(34);
    }

    @Test
    public void solvesLevelForrest15in10rolls() {
        // given
        Solver solver = Solver.of(Level.of(1, 15).start());

        // when
        Game solution = solver.solve();

        // then
        System.out.println(TextGameFormatter.of(solution).format());
        assertThat(solution.rolls()).isEqualTo(10);
    }

}