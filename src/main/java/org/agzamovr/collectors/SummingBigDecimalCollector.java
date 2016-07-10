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

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;
import static java.util.stream.Collectors.toList;

class SummingBigDecimalCollector {
    static final SummingBigDecimalCollector SUMMING_BIG_DECIMAL_COLLECTOR = new SummingBigDecimalCollector();

    <T extends Comparable<? super T>>
    List<BigDecimal> summingBigDecimal(Function<? super T, BigDecimal> mapper,
                                       List<T> list) {
        return summingBigDecimal(nullsLast(naturalOrder()), mapper, list);
    }

    <T extends Comparable<? super T>, R>
    R summingBigDecimal(Function<? super T, BigDecimal> mapper,
                        List<T> list,
                        Collector<BigDecimal, ?, R> downstream) {
        return summingBigDecimal(nullsLast(naturalOrder()), mapper, list, downstream);
    }

    <T> List<BigDecimal> summingBigDecimal(Comparator<T> comparator,
                                                  Function<? super T, BigDecimal> mapper,
                                                  List<T> list) {
        return summingBigDecimal(comparator, mapper, list, toList());
    }

    <T, A, R> R summingBigDecimal(Comparator<T> comparator,
                                         Function<? super T, BigDecimal> mapper,
                                         List<T> list,
                                         Collector<BigDecimal, A, R> downstream) {
        Objects.requireNonNull(comparator, "Comparator cannot be null");
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
        SummingBigDecimalCollector sum = new SummingBigDecimalCollector();
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> sum.summingBigDecimal(mapper, list));
    }

    <T extends Comparable<? super T>, R>
    Collector<T, ?, R> summingBigDecimal(Function<? super T, BigDecimal> mapper,
                                         Collector<BigDecimal, ?, R> downstream) {
        SummingBigDecimalCollector sum = new SummingBigDecimalCollector();
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> sum.summingBigDecimal(mapper, list, downstream));
    }

    <T>
    Collector<T, ?, List<BigDecimal>> summingBigDecimal(Comparator<T> comparator,
                                                        Function<? super T, BigDecimal> mapper) {
        SummingBigDecimalCollector sum = new SummingBigDecimalCollector();
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> sum.summingBigDecimal(comparator, mapper, list));
    }

    <T, R>
    Collector<T, ?, R> summingBigDecimal(Comparator<T> comparator,
                                         Function<? super T, BigDecimal> mapper,
                                         Collector<BigDecimal, ?, R> downstream) {
        SummingBigDecimalCollector sum = new SummingBigDecimalCollector();
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> sum.summingBigDecimal(comparator, mapper, list, downstream));
    }
}