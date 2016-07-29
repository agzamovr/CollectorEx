package org.agzamovr.collectors;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

public class DistinctCollectorTest {
    private List<Bid> bidList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        Bid bid1 = new Bid(1, 1);
        Bid bid2 = new Bid(1, 2);
        bidList.add(bid1);
        bidList.add(bid2);
    }

    @Test
    public void testDistinctWithEmptyList() {
        List<Integer> list = emptyList();

        List<Integer> result = list.stream().collect(CollectorEx.distinct());

        assertThat(result).isEmpty();
    }

    @Test
    public void testDistinctWithIntegerList() {
        List<Integer> list = asList(1, 2, 2, 1, -1, null);

        List<Integer> result = list.stream().collect(CollectorEx.distinct());

        assertThat(result).containsExactly(-1, 1, 2, null);
    }

    @Test
    public void testDistinctWithCustomMapper() {
        List<Integer> list = asList(1, -1, 2, -2, 3);

        List<Integer> result = list.stream().collect(CollectorEx.distinct(i -> i * i));

        assertThat(result).containsExactly(1, 4, 9);
    }

    @Test
    public void testDistinctWithCustomComparator() {
        List<Integer> result = bidList.stream().collect(CollectorEx.distinct(Bid::getNum));

        assertThat(result).containsExactly(1);
    }

    @Test
    public void testDistinctWithCustomCollector() {
        Set<Integer> result = bidList.stream().collect(CollectorEx.distinct(Bid::getNum, toSet()));

        assertThat(result).containsExactly(1);
    }

    @Test
    public void testDistinctWithCustomComparatorAndDownstreamCollector() {
        List<Integer> list = asList(1, -1, 2, -2, 3);
        Comparator<Integer> absComparator = (x, y) -> Integer.compare(Math.abs(x), Math.abs(y));

        Set<Integer> result = list.stream().collect(CollectorEx.distinct(absComparator, toSet()));

        System.out.println(result);
    }
}