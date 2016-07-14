package org.agzamovr.collectors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.Comparator.nullsLast;
import static java.util.stream.Collectors.toList;

class SummingBigDecimalCollector {
    static final SummingBigDecimalCollector SUMMING_BIG_DECIMAL_COLLECTOR = new SummingBigDecimalCollector();

    private void validateInput(Function<?, BigDecimal> mapper,
                               Comparator<?> comparator,
                               Collector<?, ?, ?> downstream) {
        Objects.requireNonNull(mapper, "Mapper cannot be null");
        Objects.requireNonNull(comparator, "Comparator cannot be null");
        Objects.requireNonNull(downstream, "Downstream collector cannot be null");
    }

    <T, A, R> R summingBigDecimalFinisher(Function<? super T, BigDecimal> mapper,
                                          Comparator<? super T> comparator,
                                          List<T> list,
                                          Collector<BigDecimal, A, R> downstream) {
        validateInput(mapper, comparator, downstream);
        list.sort(comparator);
        BigDecimal sum = BigDecimal.ZERO;
        Supplier<A> downstreamSupplier = downstream.supplier();
        Function<A, R> finisher = downstream.finisher();
        BiConsumer<A, BigDecimal> downstreamAccumulator = downstream.accumulator();
        A container = downstreamSupplier.get();
        for (T item : list) {
            sum = sum.add(mapper.apply(item));
            downstreamAccumulator.accept(container, sum);
        }
        return finisher.apply(container);
    }

    <T extends Comparable<? super T>>
    Collector<T, ?, List<BigDecimal>> summingBigDecimal(Function<? super T, BigDecimal> mapper) {
        return summingBigDecimal(mapper, nullsLast(Comparator.<T>naturalOrder()));
    }

    <T extends Comparable<? super T>, R>
    Collector<T, ?, R> summingBigDecimal(Function<? super T, BigDecimal> mapper,
                                         Collector<BigDecimal, ?, R> downstream) {
        return summingBigDecimal(mapper, nullsLast(Comparator.<T>naturalOrder()), downstream);
    }

    <T>
    Collector<T, ?, List<BigDecimal>> summingBigDecimal(Function<? super T, BigDecimal> mapper,
                                                        Comparator<? super T> comparator) {
        return summingBigDecimal(mapper, comparator, toList());
    }

    <T, R>
    Collector<T, ?, R> summingBigDecimal(Function<? super T, BigDecimal> mapper,
                                         Comparator<? super T> comparator,
                                         Collector<BigDecimal, ?, R> downstream) {
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> summingBigDecimalFinisher(mapper, comparator, list, downstream));
    }
}