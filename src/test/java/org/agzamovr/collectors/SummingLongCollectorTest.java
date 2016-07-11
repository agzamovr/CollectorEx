package org.agzamovr.collectors;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.agzamovr.collectors.SummingLongCollector.SUMMING_LONG_COLLECTOR;
import static org.assertj.core.api.Assertions.assertThat;

public class SummingLongCollectorTest {
    @Test
    public void testSummingEmptyList() {
        List<Long> list = emptyList();

        List<Long> result = list.stream().collect(SUMMING_LONG_COLLECTOR.summingLong(l -> l));

        assertThat(result.isEmpty());
    }

    @Test
    public void testSummingLong() {
        List<Long> list = asList(1L, 2L, 3L);

        List<Long> result = list.stream().collect(SUMMING_LONG_COLLECTOR.summingLong(l -> l));

        assertThat(result).containsExactly(1L, 3L, 6L);
    }

    @Test
    public void testSummingLongWithSetCollector() {
        List<Long> list = asList(1L, 2L, 3L);

        List<Long> result = list.stream().collect(SUMMING_LONG_COLLECTOR.summingLong(l -> l));

        assertThat(result).containsAll(asList(1L, 3L, 6L));
    }
}