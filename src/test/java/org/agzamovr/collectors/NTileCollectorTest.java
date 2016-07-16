package org.agzamovr.collectors;

import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

        Map<Integer, List<Integer>> result = list.stream().collect(CollectorEx.ntile(1));

        assertThat(result).isEmpty();
    }

    @Test
    public void testNTileWithListSizeSmallerThanTilesCount() {
        List<Integer> list = singletonList(1);

        Map<Integer, List<Integer>> result = list.stream().collect(CollectorEx.ntile(2));

        assertThat(result).containsEntry(1, singletonList(1));
    }

    @Test
    public void testNTileWithListSizeGreaterThanTilesCount() {
        List<Integer> list = asList(null, 1, 1, 2, 3, null);

        Map<Integer, List<Integer>> result = list.stream().collect(CollectorEx.ntile(2));

        assertThat(result).containsEntry(1, asList(1, 1, 2));
        assertThat(result).containsEntry(2, asList(3, null, null));
    }

    @Test
    public void testNTileWithCustomComparator() {
        List<Integer> list = asList(null, 1, 1, 2, 3, null);
        Comparator<Integer> integerComparator = Comparator.nullsLast(Integer::compareTo);

        Map<Integer, List<Integer>> result = list.stream().collect(CollectorEx.ntile(2, integerComparator.reversed()));

        assertThat(result).containsEntry(1, asList(null, null, 3));
        assertThat(result).containsEntry(2, asList(2, 1, 1));
    }

    @Test
    public void testNTileWithSetCollector() {
        List<Integer> list = asList(null, 1, 1, 2, 3, null);

        Map<Integer, Set<Integer>> result = list.stream().collect(CollectorEx.ntile(2, toSet()));

        assertThat(result.get(1)).contains(1, 2);
        assertThat(result.get(2)).contains(3, null);
    }

    @Test
    public void testNTileWithCustomComparatorAndSetCollector() {
        List<Integer> list = asList(null, 1, 1, 2, 3, null);
        Comparator<Integer> integerComparator = Comparator.nullsLast(Integer::compareTo);

        Map<Integer, Set<Integer>> result = list.stream().collect(CollectorEx.ntile(2, integerComparator.reversed(), toSet()));

        assertThat(result.get(1)).contains(3, null);
        assertThat(result.get(2)).contains(1, 2);
    }
}