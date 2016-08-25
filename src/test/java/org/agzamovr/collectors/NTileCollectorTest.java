package org.agzamovr.collectors;

import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

public class NTileCollectorTest {

    @Test
    public void testNTileWithEmptyList() {
        List<Integer> list = emptyList();

        List<List<Integer>> result = list.stream().collect(CollectorEx.ntile(1));

        assertThat(result).isEmpty();
    }

    @Test
    public void testNTileWithListSizeSmallerThanTilesCount() {
        List<Integer> list = singletonList(1);

        List<List<Integer>> result = list.stream().collect(CollectorEx.ntile(2));

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).contains(1);
    }

    @Test
    public void testNTileWithListSizeGreaterThanTilesCount() {
        List<Integer> list = asList(null, 1, 1, 2, 3, null);

        List<List<Integer>> result = list.stream().collect(CollectorEx.ntile(2));

        assertThat(result).hasSize(2);
        assertThat(result.get(0)).contains(1, 1, 2);
        assertThat(result.get(1)).contains(3, null, null);
    }

    @Test
    public void testNTileWithCustomComparator() {
        List<Integer> list = asList(null, 1, 1, 2, 3, null);
        Comparator<Integer> integerComparator = Comparator.nullsLast(Integer::compareTo);

        List<List<Integer>> result = list.stream().collect(CollectorEx.ntile(2, integerComparator.reversed()));

        assertThat(result.get(0)).contains(null, null, 3);
        assertThat(result.get(1)).contains(2, 1, 1);
    }

    @Test
    public void testNTileWithSetCollector() {
        List<Integer> list = asList(null, 1, 1, 2, 3, null);

        List<Set<Integer>> result = list.stream().collect(CollectorEx.ntile(2, toSet()));

        assertThat(result.get(0)).contains(1, 2);
        assertThat(result.get(1)).contains(3, null);
    }

    @Test
    public void testNTileWithCustomComparatorAndSetCollector() {
        List<Integer> list = asList(null, 1, 1, 2, 3, null);
        Comparator<Integer> integerComparator = Comparator.nullsLast(Integer::compareTo);

        List<Set<Integer>> result = list.stream().collect(CollectorEx.ntile(2, integerComparator.reversed(), toSet()));

        assertThat(result.get(0)).contains(3, null);
        assertThat(result.get(1)).contains(1, 2);
    }
}