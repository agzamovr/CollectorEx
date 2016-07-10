package org.agzamovr.collectors;

import org.junit.Test;

import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;
import static org.agzamovr.collectors.SummingLongCollector.SUMMING_LONG_COLLECTOR;
import static org.assertj.core.api.Assertions.assertThat;

public class SummingLongCollectorTest {
    @Test
    public void testSummingEmptyList() {
        List<Long> list = emptyList();

        List<Long> result = SUMMING_LONG_COLLECTOR.summingLong(i -> i, list);

        assertThat(result.isEmpty());
    }

    @Test
    public void testSummingLong() {
        List<Long> list = asList(1L, 2L, 3L);

        List<Long> result = SUMMING_LONG_COLLECTOR.summingLong(i -> i, list);

        assertThat(result).containsExactly(1L, 3L, 6L);
    }

    @Test
    public void testSummingLongWithSetCollector() {
        List<Long> list = asList(1L, 2L, 3L);

        Set<Long> result = SUMMING_LONG_COLLECTOR.summingLong(i -> i, list, toSet());

        assertThat(result).containsAll(asList(1L, 3L, 6L));
    }
}