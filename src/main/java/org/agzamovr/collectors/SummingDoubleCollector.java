package org.agzamovr.collectors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collector;

import static java.util.Comparator.nullsLast;
import static java.util.stream.Collectors.toList;

class SummingDoubleCollector {
    static final SummingDoubleCollector SUMMING_DOUBLE_COLLECTOR = new SummingDoubleCollector();

    private void validateInput(ToDoubleFunction<?> mapper,
                               Comparator<?> comparator,
                               Collector<?, ?, ?> downstream) {
        Objects.requireNonNull(mapper, "Mapper cannot be null");
        Objects.requireNonNull(comparator, "Comparator cannot be null");
        Objects.requireNonNull(downstream, "Downstream collector cannot be null");
    }

    <T, A, R> R summingDoubleFinisher(ToDoubleFunction<? super T> mapper,
                                      Comparator<T> comparator,
                                      List<T> list,
                                      Collector<Double, A, R> downstream) {
        validateInput(mapper, comparator, downstream);
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

    <T extends Comparable<? super T>>
    Collector<T, ?, List<Double>> summingDouble(ToDoubleFunction<? super T> mapper) {
        return summingDouble(mapper, nullsLast(Comparator.<T>naturalOrder()));
    }

    <T extends Comparable<? super T>, R>
    Collector<T, ?, R> summingDouble(ToDoubleFunction<? super T> mapper,
                                     Collector<Double, ?, R> downstream) {
        return summingDouble(mapper, nullsLast(Comparator.<T>naturalOrder()), downstream);
    }

    <T>
    Collector<T, ?, List<Double>> summingDouble(ToDoubleFunction<? super T> mapper,
                                                Comparator<T> comparator) {
        return summingDouble(mapper, comparator, toList());
    }

    <T, R>
    Collector<T, ?, R> summingDouble(ToDoubleFunction<? super T> mapper,
                                     Comparator<T> comparator,
                                     Collector<Double, ?, R> downstream) {
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> summingDoubleFinisher(mapper, comparator, list, downstream));
    }
}