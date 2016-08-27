package de.igormukhin.getthenut.support;

import java.util.Optional;
import java.util.stream.Stream;

public class OptionalHelper {

    public static <T> Stream<T> toStream(Optional<T> optional) {
        return optional.isPresent() ? Stream.of(optional.get()) : Stream.empty();
    }

}
