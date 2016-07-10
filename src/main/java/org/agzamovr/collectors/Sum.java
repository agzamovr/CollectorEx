package org.agzamovr.collectors;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.*;
import java.util.stream.Collector;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;
import static java.util.stream.Collectors.toList;

class Sum {
    public <T extends Comparable<? super T>>
    List<Integer> summingInt(ToIntFunction<? super T> mapper,
                             List<T> list) {
        return summingInt(nullsLast(naturalOrder()), mapper, list);
    }

    public <T extends Comparable<? super T>, R>
    R summingInt(ToIntFunction<? super T> mapper,
                 List<T> list,
                 Collector<Integer, ?, R> downstream) {
        return summingInt(nullsLast(naturalOrder()), mapper, list, downstream);
    }

    public <T> List<Integer> summingInt(Comparator<T> comparator,
                                        ToIntFunction<? super T> mapper,
                                        List<T> list) {
        return summingInt(comparator, mapper, list, toList());
    }

    public <T, A, R> R summingInt(Comparator<T> comparator,
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

    public <T extends Comparable<? super T>>
    List<Long> summingLong(ToLongFunction<? super T> mapper,
                           List<T> list) {
        return summingLong(nullsLast(naturalOrder()), mapper, list);
    }

    public <T extends Comparable<? super T>, R>
    R summingLong(ToLongFunction<? super T> mapper,
                  List<T> list,
                  Collector<Long, ?, R> downstream) {
        return summingLong(nullsLast(naturalOrder()), mapper, list, downstream);
    }

    public <T> List<Long> summingLong(Comparator<T> comparator,
                                      ToLongFunction<? super T> mapper,
                                      List<T> list) {
        return summingLong(comparator, mapper, list, toList());
    }

    public <T, A, R> R summingLong(Comparator<T> comparator,
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

    public <T extends Comparable<? super T>>
    List<Double> summingDouble(ToDoubleFunction<? super T> mapper,
                               List<T> list) {
        return summingDouble(nullsLast(naturalOrder()), mapper, list);
    }

    public <T extends Comparable<? super T>, R>
    R summingDouble(ToDoubleFunction<? super T> mapper,
                    List<T> list,
                    Collector<Double, ?, R> downstream) {
        return summingDouble(nullsLast(naturalOrder()), mapper, list, downstream);
    }

    public <T> List<Double> summingDouble(Comparator<T> comparator,
                                          ToDoubleFunction<? super T> mapper,
                                          List<T> list) {
        Objects.requireNonNull(comparator, "Comparator cannot be null");
        return summingDouble(comparator, mapper, list, toList());
    }

    public <T, A, R> R summingDouble(Comparator<T> comparator,
                                     ToDoubleFunction<? super T> mapper,
                                     List<T> list,
                                     Collector<Double, A, R> downstream) {
        Objects.requireNonNull(comparator, "Comparator cannot be null");
        list.sort(comparator);
        double sum = 0.0;
        double compensation = 0.0;
        Supplier<A> downstreamSupplier = downstream.supplier();
        Function<A, R> finisher = downstream.finisher();
        BiConsumer<A, Double> downstreamAccumulator = downstream.accumulator();
        A container = downstreamSupplier.get();
        for (T item : list) {
            double val = mapper.applyAsDouble(item);
            double y = val - compensation;
            double t = sum + y;
            compensation = (t - sum) - y;
            sum = t;
            downstreamAccumulator.accept(container, sum);
        }
        return finisher.apply(container);
    }

    public <T extends Comparable<? super T>>
    List<BigDecimal> summingBigDecimal(Function<? super T, BigDecimal> mapper,
                                       List<T> list) {
        return summingBigDecimal(nullsLast(naturalOrder()), mapper, list);
    }

    public <T extends Comparable<? super T>, R>
    R summingBigDecimal(Function<? super T, BigDecimal> mapper,
                        List<T> list,
                        Collector<BigDecimal, ?, R> downstream) {
        return summingBigDecimal(nullsLast(naturalOrder()), mapper, list, downstream);
    }

    public <T> List<BigDecimal> summingBigDecimal(Comparator<T> comparator,
                                                  Function<? super T, BigDecimal> mapper,
                                                  List<T> list) {
        Objects.requireNonNull(comparator, "Comparator cannot be null");
        return summingBigDecimal(comparator, mapper, list, toList());
    }

    public <T, A, R> R summingBigDecimal(Comparator<T> comparator,
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
}