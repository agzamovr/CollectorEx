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

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;
import static java.util.stream.Collectors.toList;

class SummingDoubleCollector {
    static final SummingDoubleCollector SUMMING_DOUBLE_COLLECTOR = new SummingDoubleCollector();

    <T extends Comparable<? super T>>
    List<Double> summingDouble(ToDoubleFunction<? super T> mapper,
                               List<T> list) {
        return summingDouble(nullsLast(naturalOrder()), mapper, list);
    }

    <T extends Comparable<? super T>, R>
    R summingDouble(ToDoubleFunction<? super T> mapper,
                    List<T> list,
                    Collector<Double, ?, R> downstream) {
        return summingDouble(nullsLast(naturalOrder()), mapper, list, downstream);
    }

    <T> List<Double> summingDouble(Comparator<T> comparator,
                                          ToDoubleFunction<? super T> mapper,
                                          List<T> list) {
        return summingDouble(comparator, mapper, list, toList());
    }

    <T, A, R> R summingDouble(Comparator<T> comparator,
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

    <T extends Comparable<? super T>>
    Collector<T, ?, List<Double>> summingDouble(ToDoubleFunction<? super T> mapper) {
        SummingDoubleCollector sum = new SummingDoubleCollector();
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> sum.summingDouble(mapper, list));
    }

    <T extends Comparable<? super T>, R>
    Collector<T, ?, R> summingDouble(ToDoubleFunction<? super T> mapper,
                                     Collector<Double, ?, R> downstream) {
        SummingDoubleCollector sum = new SummingDoubleCollector();
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> sum.summingDouble(mapper, list, downstream));
    }

    <T>
    Collector<T, ?, List<Double>> summingDouble(Comparator<T> comparator,
                                                ToDoubleFunction<? super T> mapper) {
        SummingDoubleCollector sum = new SummingDoubleCollector();
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> sum.summingDouble(comparator, mapper, list));
    }

    <T, R>
    Collector<T, ?, R> summingDouble(Comparator<T> comparator,
                                     ToDoubleFunction<? super T> mapper,
                                     Collector<Double, ?, R> downstream) {
        SummingDoubleCollector sum = new SummingDoubleCollector();
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> sum.summingDouble(comparator, mapper, list, downstream));
    }
}