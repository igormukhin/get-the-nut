package de.igormukhin.getthenut;

import de.igormukhin.getthenut.support.OptionalHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static de.igormukhin.getthenut.ActorType.NUT;
import static de.igormukhin.getthenut.ActorType.SQUIRREL;
import static de.igormukhin.getthenut.SpotType.SWAMP;

/**
 * Immutable representation of the state of the game.
 */
public class Game {

    private final Game parent;
    private final Land land;
    private final ActorSet actorSet;

    private final int rolls;
    private final boolean ended;
    private final boolean won;

    private Game(Board board) {
        this.parent = null;
        this.land = board.land();
        this.actorSet = board.actorSet();
        this.rolls = 0;
        this.ended = false;
        this.won = false;
    }

    private Game(Game parent, ActorSet actorSet, boolean ended, boolean won) {
        this.parent = parent;
        this.land = parent.land;
        this.actorSet = actorSet;
        this.rolls = parent.rolls() + 1;
        this.ended = ended;
        this.won = won;
    }

    public static Game start(Board board) {
        return new Game(board);
    }

    public Game roll(Pos actorPos, Direction direction) throws IllegalMoveException {
        return new Roller(actorPos, direction).roll();
    }

    public Game parent() {
        return parent;
    }

    public ActorSet actorsSet() {
        return actorSet;
    }

    public Board board() {
        return new Board(land, actorSet);
    }

    public int rolls() {
        return rolls;
    }

    public boolean ended() {
        return ended;
    }

    public boolean won() {
        return won;
    }

    public List<Game> rollsAsGameList() {
        Game currentGame = this;
        LinkedList<Game> games = new LinkedList<>();
        while (currentGame != null) {
            games.addFirst(currentGame);
            currentGame = currentGame.parent();
        }

        return Collections.unmodifiableList(games);
    }

    @Override
    public String toString() {
        return "Game{" +
                ", board=" + new Board(land, actorSet) +
                ", rolls=" + rolls +
                ", ended=" + ended +
                ", won=" + won +
                '}';
    }

    private class Roller {
        private final Pos initialActorPos;
        private final ActorType actorType;
        private final Direction direction;

        private Pos currentActorPos;
        private ActorSet currentActorSet;
        private boolean rolling = true;
        private boolean ended = false;
        private boolean won = false;

        Roller(Pos initialActorPos, Direction direction) {
            if (Game.this.ended) {
                throw new IllegalStateException("Game is over!");
            }

            this.initialActorPos = initialActorPos;
            this.actorType = actorSet.actorTypeAt(initialActorPos)
                    .orElseThrow(() -> new IllegalMoveException(initialActorPos, "No body here"));
            this.direction = direction;

            this.currentActorPos = initialActorPos;
            this.currentActorSet = actorSet;
        }

        Game roll() {
            moveAllSpots();
            return new Game(Game.this, currentActorSet, ended, won);
        }

        private void moveAllSpots() {
            while (rolling) {
                moveOneSpot();
            }
        }

        private void moveOneSpot() {
            doMovePhase();

            if (rolling) {
                doEatingPhase();
            }

            checkGameOver();
        }

        private void doMovePhase() {
            boolean isFirstStep = currentActorPos.equals(initialActorPos);

            if (!isActorMoveable(currentActorPos)) {
                if (isFirstStep) {
                    throw new IllegalMoveException(currentActorPos, "Actor can't move");
                } else {
                    finishMove();
                    return;
                }
            }

            Pos nextPos = direction.moveFrom(currentActorPos);
            if (!land.inBounds(nextPos)) {
                throw new IllegalLandException(currentActorPos, direction, "Would go out of bounds");
            }

            if (!isVacant(nextPos)) {
                if (isFirstStep) {
                    throw new IllegalMoveException(currentActorPos, direction, "The spot to take is not vacant");
                } else {
                    finishMove();
                    return;
                }
            }

            currentActorSet = currentActorSet.moveActor(currentActorPos, direction);
            currentActorPos = nextPos;
        }

        private void doEatingPhase() {
            getEaten();
            eatOthers();
        }

        private void finishMove() {
            rolling = false;
        }

        private void checkGameOver() {
            if (!currentActorSet.containsAny(SQUIRREL)) {
                finishGame(false);

            } else if (!currentActorSet.containsAny(NUT)) {
                finishGame(actorType == SQUIRREL);

            } else if (actorType == SQUIRREL && land.spotTypeAt(currentActorPos) == SWAMP) {
                // if squirrel stuck in swap, lose
                finishGame(false);
            }
        }

        private void finishGame(boolean won) {
            finishMove();
            ended = true;
            this.won = won;
        }

        private void getEaten() {
            boolean meEaten = Arrays.stream(Direction.values())
                    .map(d -> d.moveFrom(currentActorPos))
                    .filter(p -> land.spotTypeAt(p) != SWAMP) // can't eat while in swamp
                    .map(p -> currentActorSet.actorTypeAt(p))
                    .flatMap(OptionalHelper::toStream)
                    .anyMatch(o -> o.canEat(actorType));

            if (meEaten) {
                currentActorSet = currentActorSet.clearPos(currentActorPos);
                finishMove();
            }
        }

        private void eatOthers() {
            if (!currentActorSet.actorTypeAt(currentActorPos).isPresent()) {
                // if me already eaten, exit
                return;
            }

            if (land.spotTypeAt(currentActorPos) == SWAMP) {
                // can't eat other, while in swamp
                return;
            }

            Arrays.stream(Direction.values())
                    .map(d -> d.moveFrom(currentActorPos))
                    .map(p -> currentActorSet.actorAt(p))
                    .flatMap(OptionalHelper::toStream)
                    .filter(a -> actorType.canEat(a.type()))
                    .forEach(a -> {
                        currentActorSet = currentActorSet.clearPos(a.pos());
                        finishMove();
                    });
        }

        private boolean isActorMoveable(Pos actorPos) {
            if (!actorType.isMoveable()) {
                return false;
            }

            if (land.spotTypeAt(actorPos) == SWAMP
                    && actorType.isStucksInSwamp()) {
                return false;
            }

            return true;
        }

        private boolean isVacant(Pos pos) {
            if (!land.spotTypeAt(pos).canActorBeOn()) {
                return false;
            }

            if (currentActorSet.actorTypeAt(pos).isPresent()) {
                return false;
            }

            return true;
        }
    }
}
