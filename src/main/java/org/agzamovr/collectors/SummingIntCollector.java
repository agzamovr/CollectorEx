package org.agzamovr.collectors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;
import static java.util.stream.Collectors.toList;

class SummingIntCollector {
    static final SummingIntCollector SUMMING_INT_COLLECTOR = new SummingIntCollector();

    <T extends Comparable<? super T>>
    List<Integer> summingInt(ToIntFunction<? super T> mapper,
                             List<T> list) {
        return summingInt(nullsLast(naturalOrder()), mapper, list);
    }

    <T extends Comparable<? super T>, R>
    R summingInt(ToIntFunction<? super T> mapper,
                 List<T> list,
                 Collector<Integer, ?, R> downstream) {
        return summingInt(nullsLast(naturalOrder()), mapper, list, downstream);
    }

    <T> List<Integer> summingInt(Comparator<T> comparator,
                                 ToIntFunction<? super T> mapper,
                                 List<T> list) {
        return summingInt(comparator, mapper, list, toList());
    }

    <T, A, R> R summingInt(Comparator<T> comparator,
                           ToIntFunction<? super T> mapper,
                           List<T> list,
                           Collector<Integer, A, R> downstream) {
        Objects.requireNonNull(comparator, "Comparator cannot be null");
        list.sort(comparator);
        int sum = 0;
        Supplier<A> downstreamSupplier = downstream.supplier();
        Function<A, R> finisher = downstream.finisher();
        BiConsumer<A, Integer> downstreamAccumulator = downstream.accumulator();
        A container = downstreamSupplier.get();
        for (T item : list) {
            sum = sum + mapper.applyAsInt(item);
            downstreamAccumulator.accept(container, sum);
        }
        return finisher.apply(container);
    }

    <T extends Comparable<? super T>>
    Collector<T, ?, List<Integer>> summingInt(ToIntFunction<? super T> mapper) {
        SummingIntCollector sum = new SummingIntCollector();
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> sum.summingInt(mapper, list));
    }

    <T extends Comparable<? super T>, R>
    Collector<T, ?, R> summingInt(ToIntFunction<? super T> mapper,
                                  Collector<Integer, ?, R> downstream) {
        SummingIntCollector sum = new SummingIntCollector();
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> sum.summingInt(mapper, list, downstream));
    }

    <T>
    Collector<T, ?, List<Integer>> summingInt(Comparator<T> comparator,
                                              ToIntFunction<? super T> mapper) {
        SummingIntCollector sum = new SummingIntCollector();
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> sum.summingInt(comparator, mapper, list));
    }

    <T, R>
    Collector<T, ?, R> summingInt(Comparator<T> comparator,
                                  ToIntFunction<? super T> mapper,
                                  Collector<Integer, ?, R> downstream) {
        SummingIntCollector sum = new SummingIntCollector();
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> sum.summingInt(comparator, mapper, list, downstream));
    }
}