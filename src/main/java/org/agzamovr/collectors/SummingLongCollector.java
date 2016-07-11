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

import static java.util.Comparator.nullsLast;
import static java.util.stream.Collectors.toList;

class SummingLongCollector {
    static final SummingLongCollector SUMMING_LONG_COLLECTOR = new SummingLongCollector();

    private void validateInput(ToLongFunction<?> mapper,
                               Comparator<?> comparator,
                               Collector<?, ?, ?> downstream) {
        Objects.requireNonNull(mapper, "Mapper cannot be null");
        Objects.requireNonNull(comparator, "Comparator cannot be null");
        Objects.requireNonNull(downstream, "Downstream collector cannot be null");
    }

    <T, A, R> R summingLong(ToLongFunction<? super T> mapper,
                            Comparator<T> comparator,
                            List<T> list,
                            Collector<Long, A, R> downstream) {
        validateInput(mapper, comparator, downstream);
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
        return summingLong(mapper, nullsLast(Comparator.<T>naturalOrder()));
    }

    <T extends Comparable<? super T>, R>
    Collector<T, ?, R> summingLong(ToLongFunction<? super T> mapper,
                                   Collector<Long, ?, R> downstream) {
        return summingLong(mapper, nullsLast(Comparator.<T>naturalOrder()), downstream);
    }

    <T>
    Collector<T, ?, List<Long>> summingLong(ToLongFunction<? super T> mapper,
                                            Comparator<T> comparator) {
        return summingLong(mapper, comparator, toList());
    }

    <T, R>
    Collector<T, ?, R> summingLong(ToLongFunction<? super T> mapper,
                                   Comparator<T> comparator,
                                   Collector<Long, ?, R> downstream) {
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> summingLong(mapper, comparator, list, downstream));
    }
}