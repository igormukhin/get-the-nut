package de.igormukhin.getthenut;

public enum SpotType {
    ROAD(true),
    FORREST(false),
    SWAMP(true);

    private final boolean canActorBeOn;

    SpotType(boolean canActorBeOn) {
        this.canActorBeOn = canActorBeOn;
    }

    public boolean canActorBeOn() {
        return canActorBeOn;
    }
}
