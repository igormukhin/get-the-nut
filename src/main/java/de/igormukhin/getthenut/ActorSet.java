package de.igormukhin.getthenut;

import com.google.common.collect.ImmutableSet;

import java.util.Iterator;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static de.igormukhin.getthenut.support.ImmutableSetCollector.toImmutableSet;
import static java.util.Objects.requireNonNull;

public class ActorSet implements Iterable<Actor> {

    private final ImmutableSet<Actor> actors;

    public ActorSet(ImmutableSet<Actor> actors) {
        this.actors = requireNonNull(actors);
    }

    public Optional<Actor> actorAt(Pos pos) {
        return actors.stream()
                .filter(a -> a.pos().equals(pos))
                .findFirst();
    }

    public Optional<ActorType> actorTypeAt(Pos pos) {
        return actorAt(pos)
                .map(Actor::type);
    }

    public int actorsCount() {
        return actors.size();
    }

    public boolean containsNone(ActorType actorType) {
        checkNotNull(actorType);

        return !actors.stream()
                .anyMatch(a -> a.type() == actorType);
    }

    public ActorSet moveActor(Pos actorPos, Direction direction) {
        return new ActorSet(actors.stream()
                .map(a -> {
                    if (a.pos().equals(actorPos)) {
                        return a.moveTo(direction.moveFrom(a.pos()));
                    } else {
                        return a;
                    }
                }).collect(toImmutableSet()));
    }

    public ActorSet clearPos(Pos pos) {
        return new ActorSet(actors.stream()
                .filter(a -> !a.pos().equals(pos))
                .collect(toImmutableSet()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActorSet actorSet = (ActorSet) o;

        return actors.equals(actorSet.actors);

    }

    @Override
    public int hashCode() {
        return actors.hashCode();
    }

    @Override
    public Iterator<Actor> iterator() {
        return actors.iterator();
    }

    @Override
    public String toString() {
        return "ActorSet{" +
                "actors=" + actors +
                '}';
    }
}
