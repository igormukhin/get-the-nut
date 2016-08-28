package de.igormukhin.getthenut;

/**
 * Thrown when the move is illegal.
 */
public class IllegalLandException extends RuntimeException {

    public IllegalLandException(Pos actorPos, Direction direction, String message) {
        super(String.format("ActorType at %s moving to %s: %s", actorPos, direction, message));
    }

}
