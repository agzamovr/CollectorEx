package org.agzamovr.collectors;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collector;

import static java.util.Comparator.nullsLast;
import static java.util.stream.Collectors.toList;

class DistinctCollector {
    static final DistinctCollector DISTINCT_COLLECTOR = new DistinctCollector();

    <T, A, D, R>
    R distinctFinisher(Collection<T> collection,
                       Comparator<? super D> comparator,
                       Function<? super T, D> mapper,
                       Collector<? super D, A, R> downstream) {
        Map<Integer, List<D>> rankedMap = collection.stream()
                .map(mapper)
                .collect(CollectorEx.rank(comparator));
        Function<A, R> downstreamFinisher = downstream.finisher();
        BiConsumer<A, ? super D> downstreamAccumulator = downstream.accumulator();
        A container = downstream.supplier().get();
        rankedMap.forEach((rank, items) -> downstreamAccumulator.accept(container, items.get(0)));
        return downstreamFinisher.apply(container);
    }

    <T extends Comparable<? super T>>
    Collector<T, ?, List<T>> distinct() {
        return distinct(nullsLast(Comparator.<T>naturalOrder()), Function.identity(), toList());
    }

    <T>
    Collector<T, ?, List<T>> distinct(Comparator<? super T> comparator) {
        return distinct(comparator, Function.identity(), toList());
    }

    <T extends Comparable<? super T>, R>
    Collector<T, ?, R> distinct(Collector<? super T, ?, R> downstream) {
        return distinct(nullsLast(Comparator.<T>naturalOrder()), Function.identity(), downstream);
    }

    <T, D extends Comparable<? super D>>
    Collector<T, ?, List<D>> distinct(Function<? super T, D> mapper) {
        return distinct(nullsLast(Comparator.naturalOrder()), mapper, toList());
    }

    <T, R>
    Collector<T, ?, R> distinct(Comparator<? super T> comparator, Collector<? super T, ?, R> downstream) {
        return distinct(comparator, Function.identity(), downstream);
    }

    <T, D>
    Collector<T, ?, List<D>> distinct(Comparator<? super D> comparator, Function<? super T, D> mapper) {
        return distinct(comparator, mapper, toList());
    }

    <T, D extends Comparable<? super D>, R>
    Collector<T, ?, R> distinct(Function<? super T, D> mapper, Collector<? super D, ?, R> downstream) {
        return distinct(nullsLast(Comparator.naturalOrder()), mapper, downstream);
    }

    <T, D, R>
    Collector<T, List<T>, R> distinct(Comparator<? super D> comparator,
                                      Function<? super T, D> mapper,
                                      Collector<? super D, ?, R> downstream) {
        return Collector.of(ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> distinctFinisher(list, comparator, mapper, downstream));
    }
}