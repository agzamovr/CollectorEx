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

import static java.util.Comparator.nullsLast;
import static java.util.stream.Collectors.toList;

class SummingIntCollector {
    static final SummingIntCollector SUMMING_INT_COLLECTOR = new SummingIntCollector();

    private void validateInput(ToIntFunction<?> mapper,
                               Comparator<?> comparator,
                               Collector<?, ?, ?> downstream) {
        Objects.requireNonNull(mapper, "Mapper cannot be null");
        Objects.requireNonNull(comparator, "Comparator cannot be null");
        Objects.requireNonNull(downstream, "Downstream collector cannot be null");
    }

    <T, A, R> R summingIntFinisher(ToIntFunction<? super T> mapper,
                                   Comparator<? super T> comparator,
                                   List<T> list,
                                   Collector<Integer, A, R> downstream) {
        validateInput(mapper, comparator, downstream);
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
        return summingInt(mapper, nullsLast(Comparator.<T>naturalOrder()));
    }

    <T extends Comparable<? super T>, R>
    Collector<T, ?, R> summingInt(ToIntFunction<? super T> mapper,
                                  Collector<Integer, ?, R> downstream) {
        return summingInt(mapper, nullsLast(Comparator.<T>naturalOrder()), downstream);
    }

    <T>
    Collector<T, ?, List<Integer>> summingInt(ToIntFunction<? super T> mapper,
                                              Comparator<? super T> comparator) {
        return summingInt(mapper, comparator, toList());
    }

    <T, R>
    Collector<T, ?, R> summingInt(ToIntFunction<? super T> mapper,
                                  Comparator<? super T> comparator,
                                  Collector<Integer, ?, R> downstream) {
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> summingIntFinisher(mapper, comparator, list, downstream));
    }
}