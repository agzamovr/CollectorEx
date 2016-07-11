package org.agzamovr.collectors;

import org.junit.Test;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.Map.Entry;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;
import static java.util.stream.Collectors.*;
import static org.agzamovr.collectors.RankingCollector.RANKING_COLLECTOR;
import static org.assertj.core.api.Assertions.assertThat;

public class RankingCollectorTest {
    private Entry<Integer, List<Integer>> entry1 = new SimpleEntry<>(1, singletonList(1));
    private Entry<Integer, List<Integer>> entry2 = new SimpleEntry<>(2, singletonList(2));
    private Entry<Integer, List<Integer>> entry3 = new SimpleEntry<>(3, singletonList(3));
    private Entry<Integer, List<Integer>> entry4 = new SimpleEntry<>(4, singletonList(4));

    @Test
    public void testRankWithEmptyList() {
        List<Integer> list = Collections.emptyList();

        SortedMap<Integer, List<Integer>> result = list.stream().collect
                (RANKING_COLLECTOR.rank());

        assertThat(result.isEmpty());
    }

    @Test
    public void testRankWithSortedIntegerList() {
        List<Integer> list = asList(1, 2, 3, 4);

        SortedMap<Integer, List<Integer>> rankedMap = list.stream().collect
                (RANKING_COLLECTOR.rank());

        assertThat(rankedMap).containsExactly(entry1, entry2, entry3, entry4);
    }

    @Test
    public void testRankWithReverseSortedIntegerList() {
        List<Integer> list = asList(4, 3, 2, 1);

        SortedMap<Integer, List<Integer>> rankedMap = list.stream().collect
                (RANKING_COLLECTOR.rank());

        assertThat(rankedMap).containsExactly(entry1, entry2, entry3, entry4);
    }

    @Test
    public void testRankWithDuplicates() {
        List<Integer> list = asList(1, 2, 3, 4, 4, 3, 2, 1);

        SortedMap<Integer, List<Integer>> rankedMap = list.stream().collect
                (RANKING_COLLECTOR.rank());

        Entry<Integer, List<Integer>> entry1 = new SimpleEntry<>(1, asList(1, 1));
        Entry<Integer, List<Integer>> entry2 = new SimpleEntry<>(3, asList(2, 2));
        Entry<Integer, List<Integer>> entry3 = new SimpleEntry<>(5, asList(3, 3));
        Entry<Integer, List<Integer>> entry4 = new SimpleEntry<>(7, asList(4, 4));
        assertThat(rankedMap).containsExactly(entry1, entry2, entry3, entry4);
    }

    @Test
    public void testDenseRankWithDuplicates() {
        List<Integer> list = asList(1, 2, 3, 4, 4, 3, 2, 1);

        SortedMap<Integer, List<Integer>> rankedMap = list.stream().collect
                (RANKING_COLLECTOR.denseRank());

        Entry<Integer, List<Integer>> entry1 = new SimpleEntry<>(1, asList(1, 1));
        Entry<Integer, List<Integer>> entry2 = new SimpleEntry<>(2, asList(2, 2));
        Entry<Integer, List<Integer>> entry3 = new SimpleEntry<>(3, asList(3, 3));
        Entry<Integer, List<Integer>> entry4 = new SimpleEntry<>(4, asList(4, 4));
        assertThat(rankedMap).containsExactly(entry1, entry2, entry3, entry4);
    }

    @Test
    public void testRankOrder() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        Comparator<Integer> intComparator = Integer::compare;

        SortedMap<Integer, List<Integer>> rankedMap = list.stream().collect
                (RANKING_COLLECTOR.rank(intComparator, intComparator.reversed()));

        assertThat(rankedMap).containsExactly(entry4, entry3, entry2, entry1);
    }

    @Test
    public void testRankWithCustomComparator() {
        List<Integer> list = asList(1, 2, 3, 4);

        SortedMap<Integer, List<Integer>> rankedMap = list.stream().collect
                (RANKING_COLLECTOR.rank(this::evenOddComparator));

        Entry<Integer, List<Integer>> entry1 = new SimpleEntry<>(1, asList(1, 3));
        Entry<Integer, List<Integer>> entry2 = new SimpleEntry<>(3, asList(2, 4));
        assertThat(rankedMap).containsExactly(entry1, entry2);
    }

    int evenOddComparator(int x, int y) {
        boolean xeven = x % 2 == 0, yeven = y % 2 == 0;
        if ((xeven && yeven) || (!xeven && !yeven))
            return 0;
        return xeven ? 1 : -1;
    }

    @Test
    public void testRankWithSetCollector() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 4, 3, 2, 1);

        SortedMap<Integer, Set<Integer>> rankedMap = list.stream().collect
                (RANKING_COLLECTOR.rank(toSet()));

        Entry<Integer, Set<Integer>> entry1 = new SimpleEntry<>(1, singleton(1));
        Entry<Integer, Set<Integer>> entry2 = new SimpleEntry<>(3, singleton(2));
        Entry<Integer, Set<Integer>> entry3 = new SimpleEntry<>(5, singleton(3));
        Entry<Integer, Set<Integer>> entry4 = new SimpleEntry<>(7, singleton(4));
        assertThat(rankedMap).containsExactly(entry1, entry2, entry3, entry4);
    }

    @Test
    public void testRankWithMapperCollector() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 4, 3, 2, 1);

        SortedMap<Integer, Set<Integer>> rankedMap = list.stream().collect
                (RANKING_COLLECTOR.rank(mapping(i -> i * i, toSet())));

        Entry<Integer, Set<Integer>> entry1 = new SimpleEntry<>(1, singleton(1));
        Entry<Integer, Set<Integer>> entry2 = new SimpleEntry<>(3, singleton(4));
        Entry<Integer, Set<Integer>> entry3 = new SimpleEntry<>(5, singleton(9));
        Entry<Integer, Set<Integer>> entry4 = new SimpleEntry<>(7, singleton(16));
        assertThat(rankedMap).containsExactly(entry1, entry2, entry3, entry4);
    }

    @Test
    public void testGroupingByCollectorWithRankCollector() {
        List<Integer> list = asList(1, 2, 3, 4);
        Map<Integer, SortedMap<Integer, List<Integer>>> rankedMap = list.stream()
                .collect(groupingBy(i -> i % 2, RANKING_COLLECTOR.rank()));
        SortedMap<Integer, List<Integer>> odds = new TreeMap<>();
        odds.put(1, singletonList(1));
        odds.put(2, singletonList(3));
        SortedMap<Integer, List<Integer>> evens = new TreeMap<>();
        evens.put(1, singletonList(2));
        evens.put(2, singletonList(4));
        Entry<Integer, SortedMap<Integer, List<Integer>>> entry1 = new SimpleEntry<>(0, evens);
        Entry<Integer, SortedMap<Integer, List<Integer>>> entry2 = new SimpleEntry<>(1, odds);
        assertThat(rankedMap).containsExactly(entry1, entry2);
    }

    @Test
    public void testComplexComparator() {
        Comparator<Bid> bidComparator = Comparator
                .comparing(Bid::getPrice, naturalOrder())
                .thenComparing(Bid::getShippingDate, nullsLast(naturalOrder()))
                .thenComparing(Bid::getExperience, nullsLast(Comparator.<Integer>naturalOrder().reversed()))
                .thenComparing(Bid::getSentDate);

        List<Bid> bids = Bid.getBids();

        SortedMap<Integer, List<Bid>> rankedMap = bids.stream()
                .collect(RANKING_COLLECTOR.rank(bidComparator));

        assertThat(rankedMap.size()).isEqualTo(bids.size());
        List<Bid> actualBid = rankedMap.get(1);
        //first bid
        assertThat(actualBid.size()).isEqualTo(1);
        assertThat(actualBid.get(0).num).isEqualTo(1);
        //second bid
        actualBid = rankedMap.get(2);
        assertThat(actualBid.size()).isEqualTo(1);
        assertThat(actualBid.get(0).num).isEqualTo(2);
        //third bid
        actualBid = rankedMap.get(3);
        assertThat(actualBid.size()).isEqualTo(1);
        assertThat(actualBid.get(0).num).isEqualTo(3);
        //4th bid
        actualBid = rankedMap.get(4);
        assertThat(actualBid.size()).isEqualTo(1);
        assertThat(actualBid.get(0).num).isEqualTo(4);
        //5th bid
        actualBid = rankedMap.get(5);
        assertThat(actualBid.size()).isEqualTo(1);
        assertThat(actualBid.get(0).num).isEqualTo(5);
        //6th bid
        actualBid = rankedMap.get(6);
        assertThat(actualBid.size()).isEqualTo(1);
        assertThat(actualBid.get(0).num).isEqualTo(6);
        //7th bid
        actualBid = rankedMap.get(7);
        assertThat(actualBid.size()).isEqualTo(1);
        assertThat(actualBid.get(0).num).isEqualTo(7);
    }
}