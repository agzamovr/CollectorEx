package org.agzamovr.collectors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.function.*;
import java.util.stream.Collector;

import static org.agzamovr.collectors.RankingCollector.RANKING_COLLECTOR;
import static org.agzamovr.collectors.SummingBigDecimalCollector.SUMMING_BIG_DECIMAL_COLLECTOR;
import static org.agzamovr.collectors.SummingDoubleCollector.SUMMING_DOUBLE_COLLECTOR;
import static org.agzamovr.collectors.SummingIntCollector.SUMMING_INT_COLLECTOR;
import static org.agzamovr.collectors.SummingLongCollector.SUMMING_LONG_COLLECTOR;

public class CollectorEx {

    public static <T extends Comparable<? super T>>
    Collector<T, ?, SortedMap<Integer, List<T>>> denseRank() {
        return RANKING_COLLECTOR.denseRank();
    }

    public static <T>
    Collector<T, ?, SortedMap<Integer, List<T>>> denseRank(Comparator<T> comparator) {
        return RANKING_COLLECTOR.denseRank(comparator);
    }

    public static <T extends Comparable<? super T>, R>
    Collector<T, ?, SortedMap<Integer, R>> denseRank(Collector<T, ?, R> downstream) {
        return RANKING_COLLECTOR.rank(downstream);
    }

    public static <T extends Comparable<? super T>>
    Collector<T, ?, SortedMap<Integer, List<T>>> rank() {
        return RANKING_COLLECTOR.rank();
    }

    public static <T>
    Collector<T, ?, SortedMap<Integer, List<T>>> rank(Comparator<T> comparator) {
        return RANKING_COLLECTOR.rank(comparator);
    }

    public static <T extends Comparable<? super T>, R>
    Collector<T, ?, SortedMap<Integer, R>> rank(Collector<T, ?, R> downstream) {
        return RANKING_COLLECTOR.rank(downstream);
    }

    public static <T>
    Collector<T, ?, SortedMap<Integer, List<T>>> rank(Comparator<T> comparator,
                                                      Comparator<Integer> rankOrder) {
        return RANKING_COLLECTOR.rank(comparator, rankOrder);
    }

    public static <T, R>
    Collector<T, ?, SortedMap<Integer, R>> rank(Comparator<T> comparator,
                                                Comparator<Integer> rankOrder,
                                                boolean denseRank,
                                                Collector<? super T, ?, R> downstream) {
        return RANKING_COLLECTOR.rank(comparator, rankOrder, denseRank, downstream);
    }

    public static <T extends Comparable<? super T>>
    Collector<T, ?, List<Integer>> summingInt(ToIntFunction<T> mapper) {
        return SUMMING_INT_COLLECTOR.summingInt(mapper);
    }

    public static <T extends Comparable<? super T>, R>
    Collector<T, ?, R> summingInt(ToIntFunction<T> mapper,
                                  Collector<Integer, ?, R> downstream) {
        return SUMMING_INT_COLLECTOR.summingInt(mapper, downstream);
    }

    public static <T>
    Collector<T, ?, List<Integer>> summingInt(Comparator<T> comparator,
                                              ToIntFunction<? super T> mapper) {
        return SUMMING_INT_COLLECTOR.summingInt(comparator, mapper);
    }

    public static <T, R>
    Collector<T, ?, R> summingInt(Comparator<T> comparator,
                                  ToIntFunction<? super T> mapper,
                                  Collector<Integer, ?, R> downstream) {
        return SUMMING_INT_COLLECTOR.summingInt(comparator, mapper, downstream);
    }

    public static <T extends Comparable<? super T>>
    Collector<T, ?, List<Long>> summingLong(ToLongFunction<T> mapper) {
        return SUMMING_LONG_COLLECTOR.summingLong(mapper);
    }

    public static <T extends Comparable<? super T>, R>
    Collector<T, ?, R> summingLong(ToLongFunction<T> mapper,
                                   Collector<Long, ?, R> downstream) {
        return SUMMING_LONG_COLLECTOR.summingLong(mapper, downstream);
    }

    public static <T>
    Collector<T, ?, List<Long>> summingLong(Comparator<T> comparator,
                                            ToLongFunction<? super T> mapper) {
        return SUMMING_LONG_COLLECTOR.summingLong(comparator, mapper);
    }

    public static <T, R>
    Collector<T, ?, R> summingLong(Comparator<T> comparator,
                                   ToLongFunction<? super T> mapper,
                                   Collector<Long, ?, R> downstream) {
        return SUMMING_LONG_COLLECTOR.summingLong(comparator, mapper, downstream);
    }

    public static <T extends Comparable<? super T>>
    Collector<T, ?, List<Double>> summingDouble(ToDoubleFunction<T> mapper) {
        return SUMMING_DOUBLE_COLLECTOR.summingDouble(mapper);
    }

    public static <T extends Comparable<? super T>, R>
    Collector<T, ?, R> summingDouble(ToDoubleFunction<T> mapper,
                                     Collector<Double, ?, R> downstream) {
        return SUMMING_DOUBLE_COLLECTOR.summingDouble(mapper, downstream);
    }

    public static <T>
    Collector<T, ?, List<Double>> summingDouble(Comparator<T> comparator,
                                                ToDoubleFunction<? super T> mapper) {
        return SUMMING_DOUBLE_COLLECTOR.summingDouble(comparator, mapper);
    }

    public static <T, R>
    Collector<T, ?, R> summingDouble(Comparator<T> comparator,
                                     ToDoubleFunction<? super T> mapper,
                                     Collector<Double, ?, R> downstream) {
        return SUMMING_DOUBLE_COLLECTOR.summingDouble(comparator, mapper, downstream);
    }

    public static <T extends Comparable<? super T>>
    Collector<T, ?, List<BigDecimal>> summingBigDecimal(Function<T, BigDecimal> mapper) {
        return SUMMING_BIG_DECIMAL_COLLECTOR.summingBigDecimal(mapper);
    }

    public static <T extends Comparable<? super T>, R>
    Collector<T, ?, R> summingBigDecimal(Function<T, BigDecimal> mapper,
                                         Collector<BigDecimal, ?, R> downstream) {
        return SUMMING_BIG_DECIMAL_COLLECTOR.summingBigDecimal(mapper, downstream);
    }

    public static <T>
    Collector<T, ?, List<BigDecimal>> summingBigDecimal(Comparator<T> comparator,
                                                        Function<? super T, BigDecimal> mapper) {
        SummingBigDecimalCollector sum = new SummingBigDecimalCollector();
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> sum.summingBigDecimal(comparator, mapper, list));
    }

    public static <T, R>
    Collector<T, ?, R> summingBigDecimal(Comparator<T> comparator,
                                         Function<? super T, BigDecimal> mapper,
                                         Collector<BigDecimal, ?, R> downstream) {
        SummingBigDecimalCollector sum = new SummingBigDecimalCollector();
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> sum.summingBigDecimal(comparator, mapper, list, downstream));
    }

    static <T> List<T> listCombiner(List<T> left, List<T> right) {
        left.addAll(right);
        return left;
    }
}