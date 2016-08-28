package de.igormukhin.getthenut.solve;

import de.igormukhin.getthenut.Game;

public interface SolverProgress {

    void onBetterPathFound(Game betterPath);

}
