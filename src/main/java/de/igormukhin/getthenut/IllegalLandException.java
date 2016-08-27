package de.igormukhin.getthenut;

import de.igormukhin.getthenut.Direction;
import de.igormukhin.getthenut.Pos;

/**
 * Thrown when the move is illegal.
 */
public class IllegalLandException extends RuntimeException {

    public IllegalLandException(Pos actorPos, String message) {
        super(String.format("ActorType at %s: %s", actorPos, message));
    }

    public IllegalLandException(Pos actorPos, Direction direction, String message) {
        super(String.format("ActorType at %s moving to %s: %s", actorPos, direction, message));
    }

}
