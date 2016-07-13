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

    <T, A, R>
    SortedMap<Integer, R> rankFinisher(List<T> list,
                                       Comparator<? super T> comparator,
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

    <T, R>
    Map<R, Integer> mapObjToRankFinisher(List<T> list,
                                         Function<? super T, R> mapper,
                                         Comparator<? super T> comparator,
                                         boolean denseRank) {
        SortedMap<Integer, List<T>> rankedMap = rankFinisher(list, comparator, Integer::compareTo, denseRank, toList());
        Map<R, Integer> mapObjToRank = new HashMap<>();
        rankedMap.forEach((rank, items) -> items.forEach((item) -> mapObjToRank.put(mapper.apply(item), rank)));
        return mapObjToRank;
    }

    <T extends Comparable<? super T>>
    Collector<T, ?, SortedMap<Integer, List<T>>> denseRank() {
        return denseRank(nullsLast(Comparator.<T>naturalOrder()));
    }

    <T>
    Collector<T, ?, SortedMap<Integer, List<T>>> denseRank(Comparator<? super T> comparator) {
        return denseRank(comparator, Integer::compareTo);
    }

    <T extends Comparable<? super T>, R>
    Collector<T, ?, SortedMap<Integer, R>> denseRank(Collector<? super T, ?, R> downstream) {
        return rank(nullsLast(Comparator.<T>naturalOrder()), Integer::compareTo, true, downstream);
    }

    <T>
    Collector<T, ?, SortedMap<Integer, List<T>>> denseRank(Comparator<? super T> comparator,
                                                           Comparator<Integer> rankOrder) {
        return rank(comparator, rankOrder, true, toList());
    }

    <T extends Comparable<? super T>>
    Collector<T, ?, SortedMap<Integer, List<T>>> rank() {
        return rank(nullsLast(Comparator.<T>naturalOrder()));
    }

    <T>
    Collector<T, ?, SortedMap<Integer, List<T>>> rank(Comparator<? super T> comparator) {
        return rank(comparator, Integer::compareTo);
    }

    <T extends Comparable<? super T>, R>
    Collector<T, ?, SortedMap<Integer, R>> rank(Collector<? super T, ?, R> downstream) {
        return rank(nullsLast(Comparator.<T>naturalOrder()), Integer::compareTo, false, downstream);
    }

    <T>
    Collector<T, ?, SortedMap<Integer, List<T>>> rank(Comparator<? super T> comparator,
                                                      Comparator<Integer> rankOrder) {
        return rank(comparator, rankOrder, false, toList());
    }

    <T, R>
    Collector<T, ?, SortedMap<Integer, R>> rank(Comparator<? super T> comparator,
                                                Comparator<Integer> rankOrder,
                                                boolean denseRank,
                                                Collector<? super T, ?, R> downstream) {
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> rankFinisher(list, comparator, rankOrder, denseRank, downstream));
    }

    <T extends Comparable<? super T>>
    Collector<T, ?, Map<T, Integer>> mapObjToDenseRank() {
        return mapObjToRank(nullsLast(Comparator.<T>naturalOrder()), true);
    }

    <T>
    Collector<T, ?, Map<T, Integer>> mapObjToDenseRank(Comparator<? super T> comparator) {
        return mapObjToRank(comparator, true);
    }

    <T extends Comparable<? super T>, R>
    Collector<T, ?, Map<R, Integer>> mapObjToDenseRank(Function<? super T, R> mapper) {
        return mapObjToRank(mapper, nullsLast(Comparator.<T>naturalOrder()), true);
    }

    <T extends Comparable<? super T>>
    Collector<T, ?, Map<T, Integer>> mapObjToRank() {
        return mapObjToRank(nullsLast(Comparator.<T>naturalOrder()), false);
    }

    <T>
    Collector<T, ?, Map<T, Integer>> mapObjToRank(Comparator<? super T> comparator) {
        return mapObjToRank(comparator, false);
    }

    <T extends Comparable<? super T>, R>
    Collector<T, ?, Map<R, Integer>> mapObjToRank(Function<? super T, R> mapper) {
        return mapObjToRank(mapper, nullsLast(Comparator.<T>naturalOrder()), false);
    }

    <T>
    Collector<T, ?, Map<T, Integer>> mapObjToRank(Comparator<? super T> comparator,
                                                  boolean denseRank) {
        return mapObjToRank(Function.identity(), comparator, denseRank);
    }

    <T, R>
    Collector<T, ?, Map<R, Integer>> mapObjToRank(Function<? super T, R> mapper,
                                                  Comparator<? super T> comparator,
                                                  boolean denseRank) {
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> mapObjToRankFinisher(list, mapper, comparator, denseRank));
    }
}