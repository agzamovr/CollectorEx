package org.agzamovr.collectors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;
import static java.util.stream.Collectors.toList;

class SummingLongCollector {
    static final SummingLongCollector SUMMING_LONG_COLLECTOR = new SummingLongCollector();

    <T extends Comparable<? super T>>
    List<Long> summingLong(ToLongFunction<? super T> mapper,
                           List<T> list) {
        return summingLong(nullsLast(naturalOrder()), mapper, list);
    }

    <T extends Comparable<? super T>, R>
    R summingLong(ToLongFunction<? super T> mapper,
                  List<T> list,
                  Collector<Long, ?, R> downstream) {
        return summingLong(nullsLast(naturalOrder()), mapper, list, downstream);
    }

    <T> List<Long> summingLong(Comparator<T> comparator,
                                      ToLongFunction<? super T> mapper,
                                      List<T> list) {
        return summingLong(comparator, mapper, list, toList());
    }

    <T, A, R> R summingLong(Comparator<T> comparator,
                                   ToLongFunction<? super T> mapper,
                                   List<T> list,
                                   Collector<Long, A, R> downstream) {
        Objects.requireNonNull(comparator, "Comparator cannot be null");
        list.sort(comparator);
        long sum = 0;
        Supplier<A> downstreamSupplier = downstream.supplier();
        Function<A, R> finisher = downstream.finisher();
        BiConsumer<A, Long> downstreamAccumulator = downstream.accumulator();
        A container = downstreamSupplier.get();
        for (T item : list) {
            sum = sum + mapper.applyAsLong(item);
            downstreamAccumulator.accept(container, sum);
        }
        return finisher.apply(container);
    }

    <T extends Comparable<? super T>>
    Collector<T, ?, List<Long>> summingLong(ToLongFunction<? super T> mapper) {
        SummingLongCollector sum = new SummingLongCollector();
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> sum.summingLong(mapper, list));
    }

    <T extends Comparable<? super T>, R>
    Collector<T, ?, R> summingLong(ToLongFunction<? super T> mapper,
                                   Collector<Long, ?, R> downstream) {
        SummingLongCollector sum = new SummingLongCollector();
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> sum.summingLong(mapper, list, downstream));
    }

    <T>
    Collector<T, ?, List<Long>> summingLong(Comparator<T> comparator,
                                            ToLongFunction<? super T> mapper) {
        SummingLongCollector sum = new SummingLongCollector();
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> sum.summingLong(comparator, mapper, list));
    }

    <T, R>
    Collector<T, ?, R> summingLong(Comparator<T> comparator,
                                   ToLongFunction<? super T> mapper,
                                   Collector<Long, ?, R> downstream) {
        SummingLongCollector sum = new SummingLongCollector();
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> sum.summingLong(comparator, mapper, list, downstream));
    }
}