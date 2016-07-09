package org.agzamovr.collectors;

import org.junit.Test;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.Map.Entry;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CollectorExTest {

    @Test
    public void testRankCollectorEmptyList() {
        List<Integer> list = Collections.emptyList();

        SortedMap<Integer, List<Integer>> result = list.stream().collect(CollectorEx.rank());

        assertThat(result.isEmpty());
    }

    @Test
    public void testRankCollector() {
        List<Integer> list = asList(1, 2, 3, 4, null);

        SortedMap<Integer, List<Integer>> rankedMap = list.stream().collect(CollectorEx.rank());

        Entry<Integer, List<Integer>> entry1 = new SimpleEntry<>(1, singletonList(1));
        Entry<Integer, List<Integer>> entry2 = new SimpleEntry<>(2, singletonList(2));
        Entry<Integer, List<Integer>> entry3 = new SimpleEntry<>(3, singletonList(3));
        Entry<Integer, List<Integer>> entry4 = new SimpleEntry<>(4, singletonList(4));
        Entry<Integer, List<Integer>> entry5 = new SimpleEntry<>(5, singletonList(null));

        assertThat(rankedMap).containsExactly(entry1, entry2, entry3, entry4, entry5);
    }

    @Test
    public void testDenseRankCollector() {
        List<Integer> list = asList(1, 2, 3, 4, null, 4, 3, 2, 1);

        SortedMap<Integer, List<Integer>> denseRankedMap = list.stream().collect(CollectorEx.denseRank());

        Entry<Integer, List<Integer>> entry1 = new SimpleEntry<>(1, asList(1, 1));
        Entry<Integer, List<Integer>> entry2 = new SimpleEntry<>(2, asList(2, 2));
        Entry<Integer, List<Integer>> entry3 = new SimpleEntry<>(3, asList(3, 3));
        Entry<Integer, List<Integer>> entry4 = new SimpleEntry<>(4, asList(4, 4));
        Entry<Integer, List<Integer>> entry5 = new SimpleEntry<>(5, singletonList(null));
        assertThat(denseRankedMap).containsExactly(entry1, entry2, entry3, entry4, entry5);
    }

    @Test
    public void testRankCollectorNullsFirst() {
        List<Integer> list = asList(1, 2, 3, 4, null);

        SortedMap<Integer, List<Integer>> rankedMap = list.stream().collect(CollectorEx.rankNullsFirst());

        Entry<Integer, List<Integer>> entry1 = new SimpleEntry<>(1, singletonList(null));
        Entry<Integer, List<Integer>> entry2 = new SimpleEntry<>(2, singletonList(1));
        Entry<Integer, List<Integer>> entry3 = new SimpleEntry<>(3, singletonList(2));
        Entry<Integer, List<Integer>> entry4 = new SimpleEntry<>(4, singletonList(3));
        Entry<Integer, List<Integer>> entry5 = new SimpleEntry<>(5, singletonList(4));

        assertThat(rankedMap).containsExactly(entry1, entry2, entry3, entry4, entry5);
    }

    @Test
    public void testDenseRankCollectorNullsFirst() {
        List<Integer> list = asList(1, 2, 3, null, 3, 2, 1);

        SortedMap<Integer, List<Integer>> rankedMap = list.stream().collect(CollectorEx.denseRankNullsFirst());

        Entry<Integer, List<Integer>> entry1 = new SimpleEntry<>(1, singletonList(null));
        Entry<Integer, List<Integer>> entry2 = new SimpleEntry<>(2, asList(1, 1));
        Entry<Integer, List<Integer>> entry3 = new SimpleEntry<>(3, asList(2, 2));
        Entry<Integer, List<Integer>> entry4 = new SimpleEntry<>(4, asList(3, 3));

        assertThat(rankedMap).containsExactly(entry1, entry2, entry3, entry4);
    }

    @Test
    public void testRankReversedCollector() {
        List<Integer> list = asList(1, 2, 3, 4, null);

        SortedMap<Integer, List<Integer>> rankedMap = list.stream().collect(CollectorEx.rankReversed());

        Entry<Integer, List<Integer>> entry1 = new SimpleEntry<>(1, singletonList(4));
        Entry<Integer, List<Integer>> entry2 = new SimpleEntry<>(2, singletonList(3));
        Entry<Integer, List<Integer>> entry3 = new SimpleEntry<>(3, singletonList(2));
        Entry<Integer, List<Integer>> entry4 = new SimpleEntry<>(4, singletonList(1));
        Entry<Integer, List<Integer>> entry5 = new SimpleEntry<>(5, singletonList(null));

        assertThat(rankedMap).containsExactly(entry1, entry2, entry3, entry4, entry5);
    }

    @Test
    public void testRankReversedNullsFirstCollector() {
        List<Integer> list = asList(1, 2, 3, 4, null);

        SortedMap<Integer, List<Integer>> rankedMap = list.stream().collect(CollectorEx.rankReversedNullsFirst());

        Entry<Integer, List<Integer>> entry1 = new SimpleEntry<>(1, singletonList(null));
        Entry<Integer, List<Integer>> entry2 = new SimpleEntry<>(2, singletonList(4));
        Entry<Integer, List<Integer>> entry3 = new SimpleEntry<>(3, singletonList(3));
        Entry<Integer, List<Integer>> entry4 = new SimpleEntry<>(4, singletonList(2));
        Entry<Integer, List<Integer>> entry5 = new SimpleEntry<>(5, singletonList(1));

        assertThat(rankedMap).containsExactly(entry1, entry2, entry3, entry4, entry5);
    }

    @Test
    public void testDenseRankReversedCollector() {
        List<Integer> list = asList(1, 2, 3, 4, null, 4, 3, 2, 1);

        SortedMap<Integer, List<Integer>> denseRankedMap = list.stream().collect(CollectorEx.denseRankReversed());

        Entry<Integer, List<Integer>> entry1 = new SimpleEntry<>(1, asList(4, 4));
        Entry<Integer, List<Integer>> entry2 = new SimpleEntry<>(2, asList(3, 3));
        Entry<Integer, List<Integer>> entry3 = new SimpleEntry<>(3, asList(2, 2));
        Entry<Integer, List<Integer>> entry4 = new SimpleEntry<>(4, asList(1, 1));
        Entry<Integer, List<Integer>> entry5 = new SimpleEntry<>(5, singletonList(null));
        assertThat(denseRankedMap).containsExactly(entry1, entry2, entry3, entry4, entry5);
    }

    @Test
    public void testDenseRankReversedNullsFirstCollector() {
        List<Integer> list = asList(1, 2, 3, 4, null, 4, 3, 2, 1);

        SortedMap<Integer, List<Integer>> denseRankedMap = list.stream().collect(CollectorEx.denseRankReversedNullsFirst());

        Entry<Integer, List<Integer>> entry1 = new SimpleEntry<>(1, singletonList(null));
        Entry<Integer, List<Integer>> entry2 = new SimpleEntry<>(2, asList(4, 4));
        Entry<Integer, List<Integer>> entry3 = new SimpleEntry<>(3, asList(3, 3));
        Entry<Integer, List<Integer>> entry4 = new SimpleEntry<>(4, asList(2, 2));
        Entry<Integer, List<Integer>> entry5 = new SimpleEntry<>(5, asList(1, 1));
        assertThat(denseRankedMap).containsExactly(entry1, entry2, entry3, entry4, entry5);
    }

    @Test
    public void testRankCollectorWithCustomComparator() {
        List<Integer> list = asList(1, 2, 3, 4);

        SortedMap<Integer, List<Integer>> denseRankedMap = list.stream().collect(CollectorEx.rank(Integer::compare));

        Entry<Integer, List<Integer>> entry1 = new SimpleEntry<>(1, singletonList(1));
        Entry<Integer, List<Integer>> entry2 = new SimpleEntry<>(2, singletonList(2));
        Entry<Integer, List<Integer>> entry3 = new SimpleEntry<>(3, singletonList(3));
        Entry<Integer, List<Integer>> entry4 = new SimpleEntry<>(4, singletonList(4));
        assertThat(denseRankedMap).containsExactly(entry1, entry2, entry3, entry4);
    }

    @Test
    public void testRankCollectorWithCustomComparatorAndRankOrder() {
        List<Integer> list = asList(1, 2, 3, 4);
        Comparator<Integer> comparator = Integer::compare;
        SortedMap<Integer, List<Integer>> denseRankedMap = list.stream().collect(CollectorEx.rank(comparator, comparator.reversed()));

        Entry<Integer, List<Integer>> entry1 = new SimpleEntry<>(1, singletonList(1));
        Entry<Integer, List<Integer>> entry2 = new SimpleEntry<>(2, singletonList(2));
        Entry<Integer, List<Integer>> entry3 = new SimpleEntry<>(3, singletonList(3));
        Entry<Integer, List<Integer>> entry4 = new SimpleEntry<>(4, singletonList(4));
        assertThat(denseRankedMap).containsExactly(entry4, entry3, entry2, entry1);
    }

    @Test
    public void testRankCollectorWithSetDownstreamCollector() {
        List<Integer> list = asList(1, 2, 3, 4, 4, 3, 2, 1);
        SortedMap<Integer, Set<Integer>> denseRankedMap = list.stream().collect(CollectorEx.rank(toSet()));

        Entry<Integer, Set<Integer>> entry1 = new SimpleEntry<>(1, singleton(1));
        Entry<Integer, Set<Integer>> entry2 = new SimpleEntry<>(3, singleton(2));
        Entry<Integer, Set<Integer>> entry3 = new SimpleEntry<>(5, singleton(3));
        Entry<Integer, Set<Integer>> entry4 = new SimpleEntry<>(7, singleton(4));
        assertThat(denseRankedMap).containsExactly(entry1, entry2, entry3, entry4);
    }

    @Test
    public void testRankCollectorWithMapperDownstreamCollector() {
        List<Integer> list = asList(1, 2, 3, 4, 4, 3, 2, 1);
        SortedMap<Integer, Set<Integer>> denseRankedMap = list.stream().collect(CollectorEx.rank(mapping(i -> i * i, toSet())));

        Entry<Integer, Set<Integer>> entry1 = new SimpleEntry<>(1, singleton(1));
        Entry<Integer, Set<Integer>> entry2 = new SimpleEntry<>(3, singleton(4));
        Entry<Integer, Set<Integer>> entry3 = new SimpleEntry<>(5, singleton(9));
        Entry<Integer, Set<Integer>> entry4 = new SimpleEntry<>(7, singleton(16));
        assertThat(denseRankedMap).containsExactly(entry1, entry2, entry3, entry4);
    }

    @Test
    public void testGroupingByCollectorWithRankCollector() {
        List<Integer> list = asList(1, 2, 3, 4);
        Map<Integer, SortedMap<Integer, List<Integer>>> denseRankedMap = list.stream()
                .collect(groupingBy(i -> i % 2, CollectorEx.rank()));
        SortedMap<Integer, List<Integer>> odds = new TreeMap<>();
        odds.put(1, singletonList(1));
        odds.put(2, singletonList(3));
        SortedMap<Integer, List<Integer>> evens = new TreeMap<>();
        evens.put(1, singletonList(2));
        evens.put(2, singletonList(4));
        Entry<Integer, SortedMap<Integer, List<Integer>>> entry1 = new SimpleEntry<>(0, evens);
        Entry<Integer, SortedMap<Integer, List<Integer>>> entry2 = new SimpleEntry<>(1, odds);
        assertThat(denseRankedMap).containsExactly(entry1, entry2);
    }
}