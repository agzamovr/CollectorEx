package org.agzamovr.collectors;

import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class DistinctCollectorTest {
    @Test
    public void testDistinctWithEmptyList() {
        List<Integer> list = emptyList();

        Collection<Integer> result = list.stream().collect(CollectorEx.distinct(Function.identity()));

        assertThat(result).isEmpty();
    }

    @Test
    public void testDistinctWithIntegerList() {
        List<Integer> list = asList(1, 2, 2, 1, -1, null);

        Collection<Integer> result = list.stream().collect(CollectorEx.distinct(Function.identity()));

        assertThat(result).contains(1, 2, -1, null);
    }

    @Test
    public void testDistinctWithCustomDownstreamCollector() {
        List<Integer> list = asList(1, 2, 2, 1, -1, null);

        List<Integer> result = list.stream().collect(CollectorEx.distinct(Function.identity(),
                Collectors.toList()));

        assertThat(result).contains(1, 2, -1, null);
    }
}