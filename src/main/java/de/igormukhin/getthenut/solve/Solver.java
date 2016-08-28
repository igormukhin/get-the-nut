package de.igormukhin.getthenut.solve;

import com.google.common.base.Preconditions;
import de.igormukhin.getthenut.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    public Game doSolve(SolverProgress solverProgress) throws NoSolutionException {
        Searcher searcher = new Searcher(solverProgress);
        searcher.traverse();

        return searcher.getSolution().orElseThrow(NoSolutionException::new);
    }

    public Game solve() throws NoSolutionException {
        return doSolve(null);
    }

    public Game solve(SolverProgress solverProgress) throws NoSolutionException {
        return doSolve(requireNonNull(solverProgress));
    }

    private class Searcher {

        private final SolverProgress solverProgress;
        private final Map<ActorSet, Game> paths = new HashMap<>();
        private Game solution;

        public Searcher(SolverProgress solverProgress) {
            this.solverProgress = solverProgress;
        }

        void traverse() {
            traverseFrom(initialGame);
        }

        public Optional<Game> getSolution() {
            return Optional.ofNullable(solution);
        }

        private void traverseFrom(Game game) {
            for (Actor actor : game.actorSet()) {
                for (Direction direction : Direction.values()) {
                    Game nextGame;
                    try {
                        nextGame = game.roll(actor.pos(), direction);
                    } catch (IllegalMoveException e) {
                        continue;
                    }

                    if (nextGame.ended()) {
                        if (nextGame.won()) {
                            onWin(nextGame);
                        }
                        continue;
                    }

                    boolean shouldTraverseFrom = beforeTraverseFrom(nextGame);
                    if (shouldTraverseFrom) {
                        traverseFrom(nextGame);
                    }
                }
            }
        }

        /**
         * This method violates command–query separation principle.
         * But it is private.
         */
        private boolean beforeTraverseFrom(Game game) {
            Game visitedState = paths.get(game.actorSet());
            if (visitedState == null || game.rolls() < visitedState.rolls()) {
                if (solverProgress != null) {
                    solverProgress.onBetterPathFound(game);
                }

                paths.put(game.actorSet(), game);
                return true;
            }

            return false;
        }

        private void onWin(Game newSolution) {
            if (solution == null || newSolution.rolls() < solution.rolls()) {
                solution = newSolution;
            }
        }
    }
}
