package de.igormukhin.getthenut.support;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ImmutableSetTest {

    @Test
    public void immutableSetsAreEqualNoOrder() {
        // where
        ImmutableSet<Integer> set1 = ImmutableSet.of(1, 2);
        ImmutableSet<Integer> set2 = ImmutableSet.of(2, 1);

        // check
        assertThat(set1.equals(set2)).isTrue();
    }

}
