package org.agzamovr.collectors;

import org.junit.Test;

import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;
import static org.agzamovr.collectors.SummingIntCollector.SUMMING_INT_COLLECTOR;
import static org.assertj.core.api.Assertions.assertThat;

public class SummingIntCollectorTest {

    @Test
    public void testSummingEmptyList() {
        List<Integer> list = emptyList();

        List<Integer> result = list.stream().collect(SUMMING_INT_COLLECTOR.summingInt(i -> i));

        assertThat(result.isEmpty());
    }

    @Test
    public void testSummingInt() {
        List<Integer> list = asList(1, 2, 3);

        List<Integer> result = list.stream().collect(SUMMING_INT_COLLECTOR.summingInt(i -> i));

        assertThat(result).containsExactly(1, 3, 6);
    }

    @Test
    public void testSummingIntWithSetCollector() {
        List<Integer> list = asList(1, 2, 3);

        Set<Integer> result = list.stream().collect(SUMMING_INT_COLLECTOR.summingInt(i -> i, toSet()));

        assertThat(result).containsExactly(1, 3, 6);
    }
}