package de.igormukhin.getthenut.solve;

import com.google.common.base.Preconditions;
import de.igormukhin.getthenut.*;

import java.util.Comparator;
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

    public Game solve() throws NoSolutionException {
        Searcher searcher = new Searcher();
        searcher.traverse();

        return searcher.findShortestSolution().orElseThrow(NoSolutionException::new);
    }

    private class Path {

        private final Game game;

        // this solution always includes the `game` as a step
        private Game solution;

        public Path(Game game) {
            this(game, null);
        }

        public Path(Game game, Game solution) {
            this.game = game;
            this.solution = solution;
        }

        public void updateSolution(Game newSolution) {
            if (solution == null || newSolution.rolls() < solution.rolls()) {
                solution = newSolution;
            }
        }
    }

    private class Searcher {

        private final Map<ActorSet, Path> paths = new HashMap<>();

        void traverse() {
            beforeTraverseFrom(initialGame);
            traverseFrom(initialGame);
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

                    if (beforeTraverseFrom(nextGame)) {
                        traverseFrom(nextGame);
                    }
                }
            }
        }

        /**
         * TODO: this method violates command–query separation principle
         */
        private boolean beforeTraverseFrom(Game game) {
            if (!paths.containsKey(game.actorSet())) {
                // we are at a new game state
                paths.put(game.actorSet(), new Path(game));
                return true;
            }

            Path foundPath = paths.get(game.actorSet());
            if (game.rolls() < foundPath.game.rolls()) {
                Path newPath = new Path(game);
                paths.put(game.actorSet(), newPath);

                if (foundPath.solution != null) {
                    Game improvedSolution = SolveHelper.rebase(foundPath.solution, game);
                    onImprovedWin(improvedSolution);
                }

                // we already visited this game state but this path is faster
                // we borrowed the part of the solution (see rebase)
                // so no need to traverse the same sub-tree again
                return false;
            }

            // we are at known position, but we got too slow here
            return false;
        }

        private void onImprovedWin(Game improvedSolution) {
            for (Game roll : improvedSolution.parent().rollsAsGameList()) {
                Path path = paths.get(roll.actorSet());
                if (roll.rolls() < path.game.rolls()) {
                    path = new Path(roll, path.solution);
                    paths.put(path.game.actorSet(), path);
                }

                path.updateSolution(improvedSolution);
            }
        }

        private void onWin(Game solution) {
            for (Game roll : solution.parent().rollsAsGameList()) {
                paths.get(roll.actorSet()).updateSolution(solution);
            }
        }

        Optional<Game> findShortestSolution() {
            return paths.values().stream()
                    .map(path -> path.solution)
                    .filter(solution -> solution != null)
                    .min(Comparator.comparing(Game::rolls));
        }
    }
}
