package org.agzamovr.collectors;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ModeCollectorTest {

    @Test
    public void testModeIntegerList() {
        List<Integer> list = Arrays.asList(1, 1, 2, 2, 3, 4, null, null);

        Set<Integer> result = list.stream().collect(CollectorEx.mode());

        assertThat(result).contains(1, 2, null);
    }

    @Test
    public void testModeWithCustomMapper() {
        List<Integer> list = Arrays.asList(1, -1, 2, -2, 3, 4);

        Set<Integer> result = list.stream().collect(CollectorEx.mode(Math::abs));

        assertThat(result).contains(1, 2);
    }

    @Test
    public void testModeWithCustomMapperAndDownstreamCollector() {
        List<Integer> list = Arrays.asList(1, -1, 2, -2, 3, 4);

        List<Integer> result = list.stream().collect(CollectorEx.mode(Math::abs,
                Collectors.toList()));

        assertThat(result).contains(1, 2);
    }
}