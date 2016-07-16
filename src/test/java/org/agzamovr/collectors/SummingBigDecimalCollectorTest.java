package org.agzamovr.collectors;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

public class SummingBigDecimalCollectorTest {
    @Test
    public void testSummingEmptyList() {
        List<BigDecimal> list = emptyList();

        List<BigDecimal> result = list.stream().collect(CollectorEx.summingBigDecimal(bd -> bd));

        assertThat(result.isEmpty());
    }

    @Test
    public void testSummingBigDecimal() {
        List<BigDecimal> list = asList(BigDecimal.ONE, new BigDecimal("2"), new BigDecimal("3"));

        List<BigDecimal> result = list.stream().collect(CollectorEx.summingBigDecimal(bd -> bd));

        assertThat(result).containsExactly(BigDecimal.ONE, new BigDecimal("3"), new BigDecimal("6"));
    }

    @Test
    public void testSummingBigDecimalWithSetCollector() {
        List<BigDecimal> list = asList(BigDecimal.ONE, new BigDecimal("2"), new BigDecimal("3"));

        Set<BigDecimal> result = list.stream().collect(CollectorEx.summingBigDecimal(bd -> bd, toSet()));

        assertThat(result).containsAll(asList(BigDecimal.ONE, new BigDecimal("3"), new BigDecimal("6")));
    }
}