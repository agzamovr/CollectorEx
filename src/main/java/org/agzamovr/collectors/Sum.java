package org.agzamovr.collectors;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.*;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toList;

public class Sum<T> {
    private final Comparator<T> comparator;

    public Sum(Comparator<T> comparator) {
        Objects.requireNonNull(comparator, "Comparator cannot be null");
        this.comparator = comparator;
    }

    public List<Integer> summingInt(ToIntFunction<? super T> mapper,
                                    List<T> list) {
        return summingInt(mapper, list, toList());
    }

    public <A, R> R summingInt(ToIntFunction<? super T> mapper,
                               List<T> list,
                               Collector<Integer, A, R> downstream) {
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

    public List<Long> summingLong(ToLongFunction<? super T> mapper,
                                  List<T> list) {
        return summingLong(mapper, list, toList());
    }

    public <A, R> R summingLong(ToLongFunction<? super T> mapper,
                                List<T> list,
                                Collector<Long, A, R> downstream) {
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

    public List<Double> summingDouble(ToDoubleFunction<? super T> mapper,
                                      List<T> list) {
        return summingDouble(mapper, list, toList());
    }

    public <A, R> R summingDouble(ToDoubleFunction<? super T> mapper,
                                  List<T> list,
                                  Collector<Double, A, R> downstream) {
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

    public List<BigDecimal> summingBigDecimal(Function<? super T, BigDecimal> mapper,
                                          List<T> list) {
        return summingBigDecimal(mapper, list, toList());
    }

    public <A, R> R summingBigDecimal(Function<? super T, BigDecimal> mapper,
                                      List<T> list,
                                      Collector<BigDecimal, A, R> downstream) {
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