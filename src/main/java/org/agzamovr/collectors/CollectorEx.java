package org.agzamovr.collectors;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;

import static org.agzamovr.collectors.DistinctCollector.DISTINCT_COLLECTOR;
import static org.agzamovr.collectors.ModeCollector.MODE_COLLECTOR;
import static org.agzamovr.collectors.NTileCollector.N_TILE_COLLECTOR;
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
    Collector<T, ?, SortedMap<Integer, List<T>>> denseRank(Comparator<? super T> comparator) {
        return RANKING_COLLECTOR.denseRank(comparator);
    }

    public static <T extends Comparable<? super T>, R>
    Collector<T, ?, SortedMap<Integer, R>> denseRank(Collector<? super T, ?, R> downstream) {
        return RANKING_COLLECTOR.denseRank(downstream);
    }

    public static <T>
    Collector<T, ?, SortedMap<Integer, List<T>>> denseRank(Comparator<? super T> comparator,
                                                           Comparator<Integer> rankOrder) {
        return RANKING_COLLECTOR.denseRank(comparator, rankOrder);
    }

    public static <T extends Comparable<? super T>>
    Collector<T, ?, SortedMap<Integer, List<T>>> rank() {
        return RANKING_COLLECTOR.rank();
    }

    public static <T>
    Collector<T, ?, SortedMap<Integer, List<T>>> rank(Comparator<? super T> comparator) {
        return RANKING_COLLECTOR.rank(comparator);
    }

    public static <T extends Comparable<? super T>, R>
    Collector<T, ?, SortedMap<Integer, R>> rank(Collector<? super T, ?, R> downstream) {
        return RANKING_COLLECTOR.rank(downstream);
    }

    public static <T>
    Collector<T, ?, SortedMap<Integer, List<T>>> rank(Comparator<? super T> comparator,
                                                      Comparator<Integer> rankOrder) {
        return RANKING_COLLECTOR.rank(comparator, rankOrder);
    }

    public static <T, R>
    Collector<T, ?, SortedMap<Integer, R>> rank(Comparator<? super T> comparator,
                                                Comparator<Integer> rankOrder,
                                                boolean denseRank,
                                                Collector<? super T, ?, R> downstream) {
        return RANKING_COLLECTOR.rank(comparator, rankOrder, denseRank, downstream);
    }

    public static <T extends Comparable<? super T>>
    Collector<T, ?, Map<T, Integer>> mapObjToDenseRank() {
        return RANKING_COLLECTOR.mapObjToDenseRank();
    }

    public static <T>
    Collector<T, ?, Map<T, Integer>> mapObjToDenseRank(Comparator<? super T> comparator) {
        return RANKING_COLLECTOR.mapObjToDenseRank(comparator);
    }

    public static <T extends Comparable<? super T>, R>
    Collector<T, ?, Map<R, Integer>> mapObjToDenseRank(Function<T, R> mapper) {
        return RANKING_COLLECTOR.mapObjToDenseRank(mapper);
    }

    public static <T extends Comparable<? super T>>
    Collector<T, ?, Map<T, Integer>> mapObjToRank() {
        return RANKING_COLLECTOR.mapObjToRank();
    }

    public static <T>
    Collector<T, ?, Map<T, Integer>> mapObjToRank(Comparator<? super T> comparator) {
        return RANKING_COLLECTOR.mapObjToRank(comparator);
    }

    public static <T extends Comparable<? super T>, R>
    Collector<T, ?, Map<R, Integer>> mapObjToRank(Function<T, R> mapper) {
        return RANKING_COLLECTOR.mapObjToRank(mapper);
    }

    public static <T, R>
    Collector<T, ?, Map<R, Integer>> mapObjToRank(Function<T, R> mapper, Comparator<? super T> comparator) {
        return RANKING_COLLECTOR.mapObjToRank(mapper, comparator, false);
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
    Collector<T, ?, List<Integer>> summingInt(ToIntFunction<? super T> mapper,
                                              Comparator<? super T> comparator) {
        return SUMMING_INT_COLLECTOR.summingInt(mapper, comparator);
    }

    public static <T, R>
    Collector<T, ?, R> summingInt(ToIntFunction<? super T> mapper,
                                  Comparator<? super T> comparator,
                                  Collector<Integer, ?, R> downstream) {
        return SUMMING_INT_COLLECTOR.summingInt(mapper, comparator, downstream);
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
    Collector<T, ?, List<Long>> summingLong(Comparator<? super T> comparator,
                                            ToLongFunction<? super T> mapper) {
        return SUMMING_LONG_COLLECTOR.summingLong(mapper, comparator);
    }

    public static <T, R>
    Collector<T, ?, R> summingLong(ToLongFunction<? super T> mapper,
                                   Comparator<? super T> comparator,
                                   Collector<Long, ?, R> downstream) {
        return SUMMING_LONG_COLLECTOR.summingLong(mapper, comparator, downstream);
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
    Collector<T, ?, List<Double>> summingDouble(ToDoubleFunction<? super T> mapper,
                                                Comparator<? super T> comparator) {
        return SUMMING_DOUBLE_COLLECTOR.summingDouble(mapper, comparator);
    }

    public static <T, R>
    Collector<T, ?, R> summingDouble(ToDoubleFunction<? super T> mapper,
                                     Comparator<? super T> comparator,
                                     Collector<Double, ?, R> downstream) {
        return SUMMING_DOUBLE_COLLECTOR.summingDouble(mapper, comparator, downstream);
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
    Collector<T, ?, List<BigDecimal>> summingBigDecimal(Function<? super T, BigDecimal> mapper,
                                                        Comparator<? super T> comparator) {
        return SUMMING_BIG_DECIMAL_COLLECTOR.summingBigDecimal(mapper, comparator);
    }

    public static <T, R>
    Collector<T, ?, R> summingBigDecimal(Function<? super T, BigDecimal> mapper,
                                         Comparator<? super T> comparator,
                                         Collector<BigDecimal, ?, R> downstream) {
        return SUMMING_BIG_DECIMAL_COLLECTOR.summingBigDecimal(mapper, comparator, downstream);
    }

    public static <T>
    Collector<T, ?, Set<T>> mode() {
        return MODE_COLLECTOR.mode();
    }

    public static <T, R>
    Collector<T, ?, Set<R>> mode(Function<? super T, R> mapper) {
        return MODE_COLLECTOR.mode(mapper);
    }

    public static <T extends Comparable<? super T>>
    Collector<T, ?, Map<Integer, List<T>>> ntile(int tiles) {
        return N_TILE_COLLECTOR.ntile(tiles);
    }

    public static <T>
    Collector<T, ?, Map<Integer, List<T>>> ntile(int tiles,
                                                 Comparator<? super T> comparator) {
        return N_TILE_COLLECTOR.ntile(tiles, comparator);
    }

    public static <T extends Comparable<? super T>, R>
    Collector<T, ?, Map<Integer, R>> ntile(int tiles,
                                           Collector<? super T, ?, R> downstream) {
        return N_TILE_COLLECTOR.ntile(tiles, downstream);
    }

    public static <T, R>
    Collector<T, ?, Map<Integer, R>> ntile(int tiles,
                                           Comparator<? super T> comparator,
                                           Collector<? super T, ?, R> downstream) {
        return N_TILE_COLLECTOR.ntile(tiles, comparator, downstream);
    }

    public static <T extends Comparable<? super T>>
    Collector<T, ?, List<T>> distinct() {
        return DISTINCT_COLLECTOR.distinct();
    }

    public static <T>
    Collector<T, ?, List<T>> distinct(Comparator<? super T> comparator) {
        return DISTINCT_COLLECTOR.distinct(comparator);
    }

    public static <T extends Comparable<? super T>, R>
    Collector<T, ?, R> distinct(Collector<? super T, ?, R> downstream) {
        return DISTINCT_COLLECTOR.distinct(downstream);
    }

    public static <T, D extends Comparable<? super D>>
    Collector<T, ?, List<D>> distinct(Function<? super T, D> mapper) {
        return DISTINCT_COLLECTOR.distinct(mapper);
    }

    public static <T, R>
    Collector<T, ?, R> distinct(Comparator<? super T> comparator, Collector<? super T, ?, R> downstream) {
        return DISTINCT_COLLECTOR.distinct(comparator, downstream);
    }

    public static <T, D>
    Collector<T, ?, List<D>> distinct(Comparator<? super D> comparator, Function<? super T, D> mapper) {
        return DISTINCT_COLLECTOR.distinct(comparator, mapper);
    }

    public static <T, D extends Comparable<? super D>, R>
    Collector<T, ?, R> distinct(Function<? super T, D> mapper, Collector<? super D, ?, R> downstream) {
        return DISTINCT_COLLECTOR.distinct(mapper, downstream);
    }

    public static <T, D, R>
    Collector<T, ?, R> distinct(Comparator<? super D> comparator,
                                Function<? super T, D> mapper,
                                Collector<? super D, ?, R> downstream) {
        return DISTINCT_COLLECTOR.distinct(comparator, mapper, downstream);
    }

    static <T> List<T> listCombiner(List<T> left, List<T> right) {
        left.addAll(right);
        return left;
    }
}