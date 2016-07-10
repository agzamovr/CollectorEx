package org.agzamovr.collectors;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

public class SumTest {

    @Test
    public void testSummingEmptyList() {
        Sum sum = new Sum();
        List<Integer> list = emptyList();

        List<Integer> result = sum.summingInt(i -> i, list);

        assertThat(result.isEmpty());
    }

    @Test
    public void testSummingInt() {
        Sum sum = new Sum();
        List<Integer> list = asList(1, 2, 3);

        List<Integer> result = sum.summingInt(i -> i, list);

        assertThat(result).containsExactly(1, 3, 6);
    }

    @Test
    public void testSummingIntWithSetCollector() {
        Sum sum = new Sum();
        List<Integer> list = asList(1, 2, 3);

        Set<Integer> result = sum.summingInt(i -> i, list, toSet());

        assertThat(result).containsExactly(1, 3, 6);
    }

    @Test
    public void testSummingLong() {
        Sum sum = new Sum();
        List<Long> list = asList(1L, 2L, 3L);

        List<Long> result = sum.summingLong(i -> i, list);

        assertThat(result).containsExactly(1L, 3L, 6L);
    }

    @Test
    public void testSummingLongWithSetCollector() {
        Sum sum = new Sum();
        List<Long> list = asList(1L, 2L, 3L);

        Set<Long> result = sum.summingLong(i -> i, list, toSet());

        assertThat(result).containsAll(asList(1L, 3L, 6L));
    }

    @Test
    public void testSummingDouble() {
        Sum sum = new Sum();
        List<Double> list = asList(1.0, 2.1, 3.1);

        List<Double> result = sum.summingDouble(d -> d, list);

        assertThat(result).containsExactly(1.0, 3.1, 6.2);
    }

    @Test
    public void testSummingDoubleWithSetCollector() {
        Sum sum = new Sum();
        List<Double> list = asList(1.0, 2.1, 3.1);

        Set<Double> result = sum.summingDouble(i -> i, list, toSet());

        assertThat(result).containsAll(asList(1.0, 3.1, 6.2));
    }

    @Test
    public void testSummingBigDecimal() {
        Sum sum = new Sum();
        List<BigDecimal> list = asList(BigDecimal.ONE, new BigDecimal("2"), new BigDecimal("3"));

        List<BigDecimal> result = sum.summingBigDecimal(d -> d, list);

        assertThat(result).containsExactly(BigDecimal.ONE, new BigDecimal("3"), new BigDecimal("6"));
    }

    @Test
    public void testSummingBigDecimalWithSetCollector() {
        Sum sum = new Sum();
        List<BigDecimal> list = asList(BigDecimal.ONE, new BigDecimal("2"), new BigDecimal("3"));

        Set<BigDecimal> result = sum.summingBigDecimal(i -> i, list, toSet());

        assertThat(result).containsAll(asList(BigDecimal.ONE, new BigDecimal("3"), new BigDecimal("6")));
    }
}