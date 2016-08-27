package de.igormukhin.getthenut;

import static java.util.Objects.requireNonNull;

public class Actor {

    private final ActorType actorType;
    private final Pos pos;

    private Actor(ActorType actorType, Pos pos) {
        this.actorType = requireNonNull(actorType);
        this.pos = requireNonNull(pos);
    }

    public static Actor of(ActorType actorType, Pos pos) {
        return new Actor(actorType, pos);
    }

    public ActorType type() {
        return actorType;
    }

    public Pos pos() {
        return pos;
    }

    public Actor moveTo(Pos pos) {
        return new Actor(actorType, pos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Actor actor = (Actor) o;

        if (actorType != actor.actorType) return false;
        return pos.equals(actor.pos);

    }

    @Override
    public int hashCode() {
        int result = actorType.hashCode();
        result = 31 * result + pos.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "actorType=" + actorType +
                ", pos=" + pos +
                '}';
    }
}
