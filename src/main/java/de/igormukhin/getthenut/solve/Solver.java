package de.igormukhin.getthenut.solve;

import com.google.common.base.Preconditions;
import de.igormukhin.getthenut.*;
import de.igormukhin.getthenut.IllegalMoveException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public class Solver {

    private final Game initialGame;

    private Solver(Game initialGame) {
        this.initialGame = requireNonNull(initialGame);
        Preconditions.checkArgument(!initialGame.ended(), "Nothing to solve: game already over");
    }

    public static Solver of(Game initialGame) {
        return new Solver(initialGame);
    }

    public Game solve() throws NoSolutionException {
        return new TreeWalker().invoke();
    }

    private class TreeWalker {

        private final Map<ActorSet, Game> shortestPaths = new HashMap<>();
        private final Set<ActorSet> noSolutionBoards = new HashSet<>();
        private Game bestGame;

        public Game invoke() {

            solveFrom(initialGame, (solution) -> {
                if (bestGame == null || bestGame.rolls() > solution.rolls()) {
                    bestGame = solution;
                }
            }, (g) -> {}, (g) -> {});

            if (bestGame == null) {
                throw new NoSolutionException();
            }

            // DEBUG
            for (Game roll : bestGame.rollsAsGameList()) {
                if (noSolutionBoards.contains(roll.actorsSet())) {
                    System.out.println("noSolutionBoards contains\n" + roll);
                }
            }

            return bestGame;
        }

        private void solveFrom(Game game, Consumer<Game> whenWon, Consumer<Game> cutOff, Consumer<Game> onUniqueBetterGameFound) {
            for (Actor actor : game.actorsSet()) {
                for (Direction direction : Direction.values()) {
                    Game nextGame;
                    try {
                        nextGame = game.roll(actor.pos(), direction);
                    } catch (IllegalMoveException e) {
                        continue;
                    }

                    if (nextGame.ended()) {
                        if (nextGame.won()) {
                            whenWon.accept(nextGame);
                        }
                        continue;
                    }

                    if (isThereBetterOrSamePath(nextGame)) {
                        cutOff.accept(nextGame);
                        continue;
                    } else {
                        shortestPaths.put(nextGame.actorsSet(), nextGame);
                    }

                    if (noSolutionBoards.contains(nextGame.actorsSet())) {
                        continue;
                    }

                    onUniqueBetterGameFound.accept(nextGame);

                    AtomicBoolean hasAnyNewRolls = new AtomicBoolean();
                    AtomicBoolean hasAnySolutions = new AtomicBoolean();
                    AtomicBoolean hasCutOff = new AtomicBoolean();

                    solveFrom(nextGame, (g) -> {
                        hasAnySolutions.set(true);
                        whenWon.accept(g);
                    }, (g) -> {
                        hasCutOff.set(true);
                        cutOff.accept(g);
                    }, (g) -> {
                        hasAnyNewRolls.set(true);
                        onUniqueBetterGameFound.accept(g);
                    });

                    if (!hasAnySolutions.get() && hasAnyNewRolls.get() && !hasCutOff.get()) {
                        noSolutionBoards.add(nextGame.actorsSet());
                    }
                }
            }
        }

        private boolean isThereBetterOrSamePath(Game game) {
            return shortestPaths.containsKey(game.actorsSet())
                    && shortestPaths.get(game.actorsSet()).rolls() <= game.rolls();
        }
    }
}
