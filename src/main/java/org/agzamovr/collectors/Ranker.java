package org.agzamovr.collectors;


import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toList;

public class Ranker<T> {
    private final Comparator<Integer> rankOrder;
    private final Comparator<T> comparator;
    private final boolean denseRank;

    public Ranker(Comparator<T> comparator, Comparator<Integer> rankOrder, boolean denseRank) {
        Objects.requireNonNull(rankOrder, "Rank comparator cannot be null");
        Objects.requireNonNull(comparator, "Comparator cannot be null");
        this.comparator = comparator;
        this.rankOrder = rankOrder;
        this.denseRank = denseRank;
    }

    public Ranker(Comparator<T> comparator, Comparator<Integer> rankOrder) {
        this(comparator, rankOrder, false);
    }

    public Ranker(Comparator<T> comparator) {
        this(comparator, Integer::compareTo, false);
    }

    public SortedMap<Integer, List<T>> rank(List<T> list) {
        return rank(list, toList());
    }

    public <A, R> SortedMap<Integer, R> rank(List<T> list,
                                             Collector<? super T, A, R> downstream) {
        Objects.requireNonNull(downstream, "Downstream collector cannot be null");
        list.sort(comparator);
        SortedMap<Integer, R> map = new TreeMap<>(rankOrder);
        Integer rank = 0;
        T prev = null;
        Supplier<A> downstreamSupplier = downstream.supplier();
        Function<A, R> finisher = downstream.finisher();
        BiConsumer<A, ? super T> downstreamAccumulator = downstream.accumulator();
        A container = null;
        for (int i = 0; i < list.size(); i++) {
            T current = list.get(i);
            boolean startNextRank = prev == null || comparator.compare(current, prev) != 0;
            if (startNextRank) {
                if (container != null)
                    map.put(rank, finisher.apply(container));
                rank = denseRank ? rank + 1 : i + 1;
                container = downstreamSupplier.get();
                downstreamAccumulator.accept(container, current);
            } else {
                downstreamAccumulator.accept(container, current);
            }
            prev = current;
        }
        if (container != null)
            map.put(rank, finisher.apply(container));
        return map;
    }
}