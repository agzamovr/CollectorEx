package org.agzamovr.collectors;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;
import static org.agzamovr.collectors.SummingBigDecimalCollector.SUMMING_BIG_DECIMAL_COLLECTOR;
import static org.assertj.core.api.Assertions.assertThat;

public class SummingBigDecimalCollectorTest {
    @Test
    public void testSummingEmptyList() {
        List<BigDecimal> list = emptyList();

        List<BigDecimal> result = SUMMING_BIG_DECIMAL_COLLECTOR.summingBigDecimal(i -> i, list);

        assertThat(result.isEmpty());
    }

    @Test
    public void testSummingBigDecimal() {
        List<BigDecimal> list = asList(BigDecimal.ONE, new BigDecimal("2"), new BigDecimal("3"));

        List<BigDecimal> result = SUMMING_BIG_DECIMAL_COLLECTOR.summingBigDecimal(d -> d, list);

        assertThat(result).containsExactly(BigDecimal.ONE, new BigDecimal("3"), new BigDecimal("6"));
    }

    @Test
    public void testSummingBigDecimalWithSetCollector() {
        List<BigDecimal> list = asList(BigDecimal.ONE, new BigDecimal("2"), new BigDecimal("3"));

        Set<BigDecimal> result = SUMMING_BIG_DECIMAL_COLLECTOR.summingBigDecimal(i -> i, list, toSet());

        assertThat(result).containsAll(asList(BigDecimal.ONE, new BigDecimal("3"), new BigDecimal("6")));
    }
}