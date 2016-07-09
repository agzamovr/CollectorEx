package org.agzamovr.collectors;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.Map.Entry;

import static java.lang.System.currentTimeMillis;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;
import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;

public class RankerTest {
    private Entry<Integer, List<Integer>> entry1 = new SimpleEntry<>(1, singletonList(1));
    private Entry<Integer, List<Integer>> entry2 = new SimpleEntry<>(2, singletonList(2));
    private Entry<Integer, List<Integer>> entry3 = new SimpleEntry<>(3, singletonList(3));
    private Entry<Integer, List<Integer>> entry4 = new SimpleEntry<>(4, singletonList(4));

    @Test
    public void testRankEmptyList() {
        Ranker<Integer> ranker = new Ranker<>(Integer::compare);
        List<Integer> list = Collections.emptyList();

        SortedMap<Integer, List<Integer>> result = ranker.rank(list);

        assertThat(result.isEmpty());
    }

    @Test
    public void testRankSimpleSortedIntegerList() {
        Ranker<Integer> ranker = new Ranker<>(Integer::compare);
        List<Integer> list = asList(1, 2, 3, 4);

        SortedMap<Integer, List<Integer>> rankedMap = ranker.rank(list);

        assertThat(rankedMap).containsExactly(entry1, entry2, entry3, entry4);
    }

    @Test
    public void testRankSimpleReverseSortedIntegerList() {
        Ranker<Integer> ranker = new Ranker<>(Integer::compare);
        List<Integer> list = asList(4, 3, 2, 1);

        SortedMap<Integer, List<Integer>> rankedMap = ranker.rank(list);

        assertThat(rankedMap).containsExactly(entry1, entry2, entry3, entry4);
    }

    @Test
    public void testRankWithDuplicates() {
        Ranker<Integer> ranker = new Ranker<>(Integer::compare);
        List<Integer> list = asList(1, 2, 3, 4, 4, 3, 2, 1);

        SortedMap<Integer, List<Integer>> rankedMap = ranker.rank(list);

        Entry<Integer, List<Integer>> entry1 = new SimpleEntry<>(1, asList(1, 1));
        Entry<Integer, List<Integer>> entry2 = new SimpleEntry<>(3, asList(2, 2));
        Entry<Integer, List<Integer>> entry3 = new SimpleEntry<>(5, asList(3, 3));
        Entry<Integer, List<Integer>> entry4 = new SimpleEntry<>(7, asList(4, 4));
        assertThat(rankedMap).containsExactly(entry1, entry2, entry3, entry4);
    }

    @Test
    public void testDenseRankWithDuplicates() {
        Ranker<Integer> ranker = new Ranker<>(Integer::compare, Integer::compare, true);
        List<Integer> list = asList(1, 2, 3, 4, 4, 3, 2, 1);

        SortedMap<Integer, List<Integer>> rankedMap = ranker.rank(list);

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
        Ranker<Integer> ranker = new Ranker<>(intComparator, intComparator.reversed());

        SortedMap<Integer, List<Integer>> rankedMap = ranker.rank(list);

        assertThat(rankedMap).containsExactly(entry4, entry3, entry2, entry1);
    }

    @Test
    public void testRankWithCustomComparator() {
        Ranker<Integer> ranker = new Ranker<>(this::evenOddComparator);
        List<Integer> list = asList(1, 2, 3, 4);

        SortedMap<Integer, List<Integer>> rankedMap = ranker.rank(list);

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
        Ranker<Integer> ranker = new Ranker<>(Integer::compare);
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 4, 3, 2, 1);

        SortedMap<Integer, Set<Integer>> rankedMap = ranker.rank(list, toSet());

        Entry<Integer, Set<Integer>> entry1 = new SimpleEntry<>(1, singleton(1));
        Entry<Integer, Set<Integer>> entry2 = new SimpleEntry<>(3, singleton(2));
        Entry<Integer, Set<Integer>> entry3 = new SimpleEntry<>(5, singleton(3));
        Entry<Integer, Set<Integer>> entry4 = new SimpleEntry<>(7, singleton(4));
        assertThat(rankedMap).containsExactly(entry1, entry2, entry3, entry4);
    }

    @Test
    public void testRankWithMapperCollector() {
        Ranker<Integer> ranker = new Ranker<>(Integer::compare);
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 4, 3, 2, 1);

        SortedMap<Integer, Set<Integer>> rankedMap = ranker.rank(list, mapping(i -> i * i, toSet()));

        Entry<Integer, Set<Integer>> entry1 = new SimpleEntry<>(1, singleton(1));
        Entry<Integer, Set<Integer>> entry2 = new SimpleEntry<>(3, singleton(4));
        Entry<Integer, Set<Integer>> entry3 = new SimpleEntry<>(5, singleton(9));
        Entry<Integer, Set<Integer>> entry4 = new SimpleEntry<>(7, singleton(16));
        assertThat(rankedMap).containsExactly(entry1, entry2, entry3, entry4);
    }

    @Test
    public void testComplexComparator() {
        Comparator<Bid> bidComparator = Comparator
                .comparing(Bid::getPrice, naturalOrder())
                .thenComparing(Bid::getShippingDate, nullsLast(naturalOrder()))
                .thenComparing(Bid::getExperience, nullsLast(Comparator.<Integer>naturalOrder().reversed()))
                .thenComparing(Bid::getSentDate);
        Ranker<Bid> ranker = new Ranker<>(bidComparator);

        List<Bid> bids = getBids();

        SortedMap<Integer, List<Bid>> rankedMap = ranker.rank(bids);

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

    List<Bid> getBids() {
        Date past = new Date(currentTimeMillis() - 1000);
        Date present = new Date();
        Date future = new Date(currentTimeMillis() + 1000);
        Bid bid1 = new Bid(1, BigDecimal.ONE, future, null, present);
        Bid bid2 = new Bid(2, new BigDecimal("2"), present, null, present);
        Bid bid3 = new Bid(3, new BigDecimal("2"), future, null, present);
        Bid bid4 = new Bid(4, new BigDecimal("3"), future, 2, present);
        Bid bid5 = new Bid(5, new BigDecimal("3"), future, 1, present);
        Bid bid6 = new Bid(6, new BigDecimal("4"), future, null, past);
        Bid bid7 = new Bid(7, new BigDecimal("4"), future, null, present);
        return asList(bid1, bid2, bid3, bid4, bid5, bid6, bid7);
    }

}