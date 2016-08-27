package de.igormukhin.getthenut;

import de.igormukhin.getthenut.textmap.TextBoard;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LevelTest {

    @Test
    public void loadsExitingLevel() {
        // when
        Level level = Level.of(3, 36);
        Game game = level.start();

        // then
        assertThat(TextBoard.format(game)).isEqualTo(String.format(
                "############%n" +
                "##n m#b  ###%n" +
                "#### #  ####%n" +
                "###     b###%n" +
                "#### #   ###%n" +
                "###     ####%n" +
                "####x#  s###%n" +
                "############"));
    }

}