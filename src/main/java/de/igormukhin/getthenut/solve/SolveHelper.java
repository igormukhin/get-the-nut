package de.igormukhin.getthenut.solve;

import de.igormukhin.getthenut.Game;

import java.util.List;

import static com.google.common.base.Preconditions.*;

public class SolveHelper {

    /**
     * It's like Git Rebase - rebases the solution on another head
     */
    public static Game rebase(Game solution, Game head) {
        checkNotNull(solution);
        checkNotNull(head);
        checkArgument(allStatesUnique(solution));
        checkArgument(allStatesUnique(head));

        Game newSolution = head;
        boolean rebasing = false;

        for (Game game : solution.rollsAsGameList()) {
            if (rebasing) {
                newSolution = game.withParent(newSolution);

            } else if (game.actorSet().equals(head.actorSet())) {
                rebasing = true;

            }
        }

        checkState(allStatesUnique(newSolution));
        return newSolution;
    }

    private static boolean allStatesUnique(Game game) {
        List<Game> states = game.rollsAsGameList();
        return states.size() == states.stream().map(Game::actorSet).distinct().count();
    }

}
