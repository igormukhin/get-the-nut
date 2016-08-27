package de.igormukhin.getthenut;

import java.util.EnumSet;

public enum ActorType {
    NUT (false, false),
    SQUIRREL (true, true),
    MOUSE(true, true),
    BOAR (true, true);

    static {
        NUT.canEatActorTypes = EnumSet.noneOf(ActorType.class);
        SQUIRREL.canEatActorTypes = EnumSet.of(NUT);
        MOUSE.canEatActorTypes = EnumSet.of(NUT);
        BOAR.canEatActorTypes = EnumSet.of(NUT, SQUIRREL, MOUSE);
    }

    private boolean moveable;
    private boolean stucksInSwamp;
    private EnumSet canEatActorTypes;

    ActorType(boolean moveable, boolean stucksInSwamp) {
        this.moveable = moveable;
        this.stucksInSwamp = stucksInSwamp;
    }

    public boolean isMoveable() {
        return moveable;
    }

    public boolean isStucksInSwamp() {
        return stucksInSwamp;
    }

    public boolean canEat(ActorType other) {
        return canEatActorTypes.contains(other);
    }
}
