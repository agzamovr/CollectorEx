package org.agzamovr.collectors;


import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.Comparator.nullsLast;
import static java.util.stream.Collectors.toList;

class RankingCollector {
    static final RankingCollector RANKING_COLLECTOR = new RankingCollector();

    private void validateInput(Comparator<?> comparator,
                               Comparator<?> rankOrder,
                               Collector<?, ?, ?> downstream) {
        Objects.requireNonNull(comparator, "Comparator cannot be null");
        Objects.requireNonNull(rankOrder, "Rank comparator cannot be null");
        Objects.requireNonNull(downstream, "Downstream collector cannot be null");
    }

    <T, A, R> SortedMap<Integer, R> rankFinisher(List<T> list,
                                                 Comparator<T> comparator,
                                                 Comparator<Integer> rankOrder,
                                                 boolean denseRank,
                                                 Collector<? super T, A, R> downstream) {
        validateInput(comparator, rankOrder, downstream);
        list.sort(comparator);
        SortedMap<Integer, R> map = new TreeMap<>(rankOrder);
        Integer rank = 0;
        T prev = null;
        Supplier<A> downstreamSupplier = downstream.supplier();
        Function<A, R> downstreamFinisher = downstream.finisher();
        BiConsumer<A, ? super T> downstreamAccumulator = downstream.accumulator();
        A container = null;
        for (int i = 0; i < list.size(); i++) {
            T current = list.get(i);
            boolean startNextRank = prev == null || comparator.compare(current, prev) != 0;
            if (startNextRank) {
                if (container != null)
                    map.put(rank, downstreamFinisher.apply(container));
                rank = denseRank ? rank + 1 : i + 1;
                container = downstreamSupplier.get();
            }
            downstreamAccumulator.accept(container, current);
            prev = current;
        }
        if (container != null)
            map.put(rank, downstreamFinisher.apply(container));
        return map;
    }

    <T extends Comparable<? super T>>
    Collector<T, ?, SortedMap<Integer, List<T>>> denseRank() {
        return denseRank(nullsLast(Comparator.<T>naturalOrder()));
    }

    <T>
    Collector<T, ?, SortedMap<Integer, List<T>>> denseRank(Comparator<T> comparator) {
        return denseRank(comparator, Integer::compareTo);
    }

    <T extends Comparable<? super T>, R>
    Collector<T, ?, SortedMap<Integer, R>> denseRank(Collector<? super T, ?, R> downstream) {
        return rank(nullsLast(Comparator.<T>naturalOrder()), Integer::compareTo, true, downstream);
    }

    <T extends Comparable<? super T>>
    Collector<T, ?, SortedMap<Integer, List<T>>> rank() {
        return rank(nullsLast(Comparator.<T>naturalOrder()));
    }

    <T>
    Collector<T, ?, SortedMap<Integer, List<T>>> rank(Comparator<T> comparator) {
        return rank(comparator, Integer::compareTo);
    }

    <T extends Comparable<? super T>, R>
    Collector<T, ?, SortedMap<Integer, R>> rank(Collector<? super T, ?, R> downstream) {
        return rank(nullsLast(Comparator.<T>naturalOrder()), Integer::compareTo, false, downstream);
    }

    <T>
    Collector<T, ?, SortedMap<Integer, List<T>>> denseRank(Comparator<T> comparator,
                                                           Comparator<Integer> rankOrder) {
        return rank(comparator, rankOrder, true, toList());
    }

    <T>
    Collector<T, ?, SortedMap<Integer, List<T>>> rank(Comparator<T> comparator,
                                                      Comparator<Integer> rankOrder) {
        return rank(comparator, rankOrder, false, toList());
    }

    <T, R>
    Collector<T, ?, SortedMap<Integer, R>> rank(Comparator<T> comparator,
                                                Comparator<Integer> rankOrder,
                                                boolean denseRank,
                                                Collector<? super T, ?, R> downstream) {
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> rankFinisher(list, comparator, rankOrder, denseRank, downstream));
    }
}