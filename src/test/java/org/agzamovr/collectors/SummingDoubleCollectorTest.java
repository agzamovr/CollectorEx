package org.agzamovr.collectors;

import org.junit.Test;

import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;
import static org.agzamovr.collectors.SummingDoubleCollector.SUMMING_DOUBLE_COLLECTOR;
import static org.assertj.core.api.Assertions.assertThat;

public class SummingDoubleCollectorTest {
    @Test
    public void testSummingEmptyList() {
        List<Double> list = emptyList();

        List<Double> result = SUMMING_DOUBLE_COLLECTOR.summingDouble(i -> i, list);

        assertThat(result.isEmpty());
    }

    @Test
    public void testSummingDouble() {
        List<Double> list = asList(1.0, 2.1, 3.1);

        List<Double> result = SUMMING_DOUBLE_COLLECTOR.summingDouble(d -> d, list);

        assertThat(result).containsExactly(1.0, 3.1, 6.2);
    }

    @Test
    public void testSummingDoubleWithSetCollector() {
        List<Double> list = asList(1.0, 2.1, 3.1);

        Set<Double> result = SUMMING_DOUBLE_COLLECTOR.summingDouble(i -> i, list, toSet());

        assertThat(result).containsAll(asList(1.0, 3.1, 6.2));
    }
}