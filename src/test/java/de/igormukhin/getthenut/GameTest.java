package de.igormukhin.getthenut;

import de.igormukhin.getthenut.textmap.TextBoard;
import de.igormukhin.getthenut.textmap.TextBoardParser;
import org.junit.Test;

import static de.igormukhin.getthenut.Direction.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionThrownBy;

public class GameTest {

    @Test
    public void rollOneCell() {
        // given
        String map = String.format(
                "####%n" +
                "# n#%n" +
                "#  #%n" +
                "#s #%n" +
                "####");
        Game game = TextBoard.parse(map);

        // when
        game = game.roll(Pos.of(3, 1), EAST);

        // then
        assertThat(game.rolls()).isEqualTo(1);
        assertThat(TextBoard.format(game)).isEqualTo(String.format(
                "####%n" +
                "# n#%n" +
                "#  #%n" +
                "# s#%n" +
                "####"));
    }

    @Test
    public void rollTwoCells() {
        // given
        String map = String.format(
                "#####%n" +
                "# n##%n" +
                "#   #%n" +
                "#s  #%n" +
                "#####");
        Game game = TextBoard.parse(map);

        // when
        game = game.roll(Pos.of(3, 1), EAST);

        // then
        assertThat(game.rolls()).isEqualTo(1);
        assertThat(TextBoard.format(game)).isEqualTo(String.format(
                "#####%n" +
                "# n##%n" +
                "#   #%n" +
                "#  s#%n" +
                "#####"));
    }

    @Test
    public void rollFiveTimes() {
        // given
        String map = String.format(
                "######%n" +
                "#  ###%n" +
                "#   n#%n" +
                "#  ###%n" +
                "#s  ##%n" +
                "######");
        Game game = TextBoard.parse(map);

        // when
        game = game.roll(Pos.of(4, 1), EAST);
        game = game.roll(Pos.of(4, 3), WEST);
        game = game.roll(Pos.of(4, 1), NORTH);
        game = game.roll(Pos.of(1, 1), EAST);
        game = game.roll(Pos.of(1, 2), SOUTH);

        // then
        assertThat(game.rolls()).isEqualTo(5);
        assertThat(TextBoard.format(game)).isEqualTo(String.format(
                "######%n" +
                "#  ###%n" +
                "#   n#%n" +
                "#  ###%n" +
                "# s ##%n" +
                "######"));
    }

    @Test
    public void boarRolls_eatsSquirrel_lose() {
        // given
        String map = String.format(
                "#####%n" +
                "#b n#%n" +
                "#   #%n" +
                "#  s#%n" +
                "#####");
        Game game = TextBoard.parse(map);

        // when
        game = game.roll(Pos.of(1, 1), SOUTH);
        game = game.roll(Pos.of(3, 1), EAST);

        // then
        assertThat(TextBoard.format(game)).isEqualTo(String.format(
                "#####%n" +
                "#  n#%n" +
                "#   #%n" +
                "# b #%n" +
                "#####"));
        assertThat(game.ended()).isTrue();
        assertThat(game.won()).isFalse();
    }

    @Test
    public void boarRolls_eatsMouse() {
        // given
        String map = String.format(
                "#####%n" +
                "#b n#%n" +
                "# m #%n" +
                "#  s#%n" +
                "#####");
        Game game = TextBoard.parse(map);

        // when
        game = game.roll(Pos.of(1, 1), SOUTH);

        // then
        assertThat(TextBoard.format(game)).isEqualTo(String.format(
                "#####%n" +
                "#  n#%n" +
                "#b  #%n" +
                "#  s#%n" +
                "#####"));
        assertThat(game.ended()).isFalse();
    }

    @Test
    public void roll_eatTheNut_win() {
        // given
        String map = String.format(
                "####%n" +
                "# n#%n" +
                "#s #%n" +
                "####");
        Game game = TextBoard.parse(map);

        // when
        game = game.roll(Pos.of(2, 1), NORTH);

        // then
        assertThat(game.rolls()).isEqualTo(1);
        assertThat(game.actorsSet().actorTypeAt(Pos.of(1, 1))).contains(ActorType.SQUIRREL);
        assertThat(game.actorsSet().actorTypeAt(Pos.of(1, 2))).isEmpty();
        assertThat(game.ended()).isTrue();
        assertThat(game.won()).isTrue();
    }

    @Test
    public void roll_gotEeaten_lose() {
        // given
        String map = String.format(
                "#####%n" +
                "#b n#%n" +
                "#   #%n" +
                "#  s#%n" +
                "#####");
        Game game = TextBoard.parse(map);

        // when
        game = game.roll(Pos.of(3, 3), WEST);
        game = game.roll(Pos.of(3, 1), NORTH);

        // then
        assertThat(TextBoard.format(game)).isEqualTo(String.format(
                "#####%n" +
                "#b n#%n" +
                "#   #%n" +
                "#   #%n" +
                "#####"));
        assertThat(game.ended()).isTrue();
        assertThat(game.won()).isFalse();
    }

    @Test
    public void roll_stuckInSwamp_lose() {
        // given
        String map = String.format(
                "#####%n" +
                "#b n#%n" +
                "#   #%n" +
                "#x s#%n" +
                "#####");
        Game game = TextBoard.parse(map);

        // when
        Game game2 = game.roll(Pos.of(3, 3), WEST);

        // then
        assertThatExceptionThrownBy(() -> game2.roll(Pos.of(3, 1), NORTH))
                .isInstanceOf(IllegalStateException.class);

        assertThat(TextBoard.format(game2)).isEqualTo(String.format(
                "#####%n" +
                "#b n#%n" +
                "#   #%n" +
                "#s  #%n" +
                "#####"));
        assertThat(game2.ended()).isTrue();
        assertThat(game2.won()).isFalse();
    }

    @Test
    public void mouseRolls_stuckInSwamp_canBeEaten() {
        // given
        String map = String.format(
                "#####%n" +
                "#b n#%n" +
                "#  s#%n" +
                "#x m#%n" +
                "#####");
        Game game = TextBoard.parse(map);

        // when
        game = game.roll(Pos.of(3, 3), WEST);
        game = game.roll(Pos.of(1, 1), SOUTH);

        // then
        assertThat(TextBoard.format(game)).isEqualTo(String.format(
                "#####%n" +
                "#  n#%n" +
                "#b s#%n" +
                "#x  #%n" +
                "#####"));
        assertThat(game.ended()).isFalse();
    }

    @Test
    public void mouseRolls_stuckInSwamp_cantMove() {
        // given
        String map = String.format(
                "#####%n" +
                "#b n#%n" +
                "#  s#%n" +
                "#x m#%n" +
                "#####");
        Game game = TextBoard.parse(map);

        // when
        Game game2 = game.roll(Pos.of(3, 3), WEST);

        // then
        assertThatExceptionThrownBy(() -> game2.roll(Pos.of(3, 1), EAST))
                .isInstanceOf(IllegalMoveException.class);
        assertThat(game2.ended()).isFalse();
    }

    @Test
    public void boarRolls_stuckInSwamp_cantEat() {
        // given
        String map = String.format(
                "#####%n" +
                "#b n#%n" +
                "#  s#%n" +
                "#xm #%n" +
                "#####");
        Board board = TextBoardParser.of(map).parse();
        Game game = Game.start(board);

        // when
        game = game.roll(Pos.of(1, 1), SOUTH);
        game = game.roll(Pos.of(2, 3), WEST);

        // then
        assertThat(TextBoard.format(game)).isEqualTo(String.format(
                "#####%n" +
                "#  n#%n" +
                "#s  #%n" +
                "#bm #%n" +
                "#####"));
        assertThat(game.ended()).isFalse();
    }

}