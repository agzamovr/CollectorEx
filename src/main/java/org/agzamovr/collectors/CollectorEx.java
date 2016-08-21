package org.agzamovr.collectors;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;

import static org.agzamovr.collectors.DistinctCollector.DISTINCT_COLLECTOR;
import static org.agzamovr.collectors.ModeCollector.MODE_COLLECTOR;
import static org.agzamovr.collectors.MultiValueMapCollector.MULTI_VALUE_MAP_COLLECTOR;
import static org.agzamovr.collectors.NTileCollector.N_TILE_COLLECTOR;
import static org.agzamovr.collectors.RankDistinctCollector.RANK_DISTINCT_COLLECTOR;
import static org.agzamovr.collectors.RankingCollector.RANKING_COLLECTOR;
import static org.agzamovr.collectors.SummingBigDecimalCollector.SUMMING_BIG_DECIMAL_COLLECTOR;
import static org.agzamovr.collectors.SummingDoubleCollector.SUMMING_DOUBLE_COLLECTOR;
import static org.agzamovr.collectors.SummingIntCollector.SUMMING_INT_COLLECTOR;
import static org.agzamovr.collectors.SummingLongCollector.SUMMING_LONG_COLLECTOR;
// The MIT License (MIT)
// Copyright (c) 2016 Rustam Agzamov
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
// documentation files (the "Software"), to deal in the Software without restriction, including without limitation
// the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
// Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all copies or substantial portions
// of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
// TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
// THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
// CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
// IN THE SOFTWARE.

/**
 * Java Collectors extensions for using with Java 8 streams. Implements various useful  reduction operations,
 * such as ranking, storing elements into buckets, storing distinct elements according to various criteria, etc.
 *
 * @author Rustam Agzamov
 */
public class CollectorEx {

    /**
     * Computes the rank of object using natural ordering. The ranks are consecutive integers beginning with 1.
     * The largest rank value is the number of unique objects. Rank values are not skipped in the event of ties.
     * Equal objects for the ranking criteria receive the same rank. Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(1, 2, 3, 4, 4, 3, 2, 1);
     *     SortedMap<Integer, List<Integer>> denseRankedMap = list.stream().collect(CollectorEx.denseRank());
     *     System.out.println(denseRankedMap);
     * </pre>
     * This will prints {@code {1=[1, 1], 3=[2, 2], 5=[3, 3], 7=[4, 4]}}.
     *
     * @param <T> the type of stream objects
     * @return {@link SortedMap} containing ranks as keys and ranked objects as values
     */
    public static <T extends Comparable<? super T>>
    Collector<T, ?, SortedMap<Integer, List<T>>> denseRank() {
        return RANKING_COLLECTOR.denseRank();
    }

    /**
     * Computes the rank of object using given comparator. The ranks are consecutive integers beginning with 1.
     * The largest rank value is the number of unique objects. Rank values are not skipped in the event of ties.
     * Equal objects for the ranking criteria receive the same rank. Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(1, 2, 3, 4, 4, 3, 2, 1);
     *     Comparator<Integer> intComparator = Integer::compare;
     *     SortedMap<Integer, List<Integer>> denseRankedMap = list.stream().collect(CollectorEx.denseRank(intComparator.reverse()));
     *     System.out.println(denseRankedMap);
     * </pre>
     * This will prints {@code {1=[4, 4], 2=[3, 3], 3=[2, 2], 4=[1, 1]}}.
     *
     * @param comparator the comparator for custom ordering
     * @param <T>        the type of stream objects
     * @return {@link SortedMap} containing ranks as keys and collection of ranked objects as values
     */
    public static <T>
    Collector<T, ?, SortedMap<Integer, List<T>>> denseRank(Comparator<? super T> comparator) {
        return RANKING_COLLECTOR.denseRank(comparator);
    }

    /**
     * Computes the rank of object using natural ordering and collects the same ranked objects using given downstream collector.
     * The ranks are consecutive integers beginning with 1. The largest rank value is the number of unique objects.
     * Rank values are not skipped in the event of ties. Equal objects for the ranking criteria receive the same rank.
     * Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(1, 2, 3, 4, 4, 3, 2, 1);
     *     SortedMap<Integer, Set<Integer>> denseRankedMap = list.stream().collect(CollectorEx.denseRank(toSet()));
     *     System.out.println(denseRankedMap);
     * </pre>
     * This will prints {@code {1=[1], 2=[2], 3=[3], 4=[4]}}.
     *
     * @param downstream the reduction operation (e.g. the downstream collector)
     * @param <T>        the type of stream objects
     * @param <R>        the result type of the downstream collector
     * @return {@link SortedMap} containing ranks as keys and collection of ranked objects as values
     */
    public static <T extends Comparable<? super T>, R>
    Collector<T, ?, SortedMap<Integer, R>> denseRank(Collector<? super T, ?, R> downstream) {
        return RANKING_COLLECTOR.denseRank(downstream);
    }

    /**
     * Computes the rank of object using given comparator. The ranks are consecutive integers beginning with 1
     * <b>sorted by according to given second comparator</b>. The largest rank value is the number of unique objects.
     * Rank values are not skipped in the event of ties. Equal objects for the ranking criteria receive the same rank.
     * Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(1, 2, 3, 4, 4, 3, 2, 1);
     *     Comparator<Integer> intComparator = Integer::compare;
     *     SortedMap<Integer, List<Integer>> denseRankedMap = list.stream()
     *                                          .collect(CollectorEx.denseRank(intComparator, intComparator.reverse()));
     *     System.out.println(denseRankedMap);
     * </pre>
     * This will prints {@code {4=[4, 4], 3=[3, 3], 2=[2, 2], 1=[1, 1]}}.
     *
     * @param comparator the comparator for custom ordering
     * @param rankOrder  the comparator for custom rank ordering
     * @param <T>        the type of stream objects
     * @return {@link SortedMap} containing ranks as keys and collection of ranked objects as values
     */
    public static <T>
    Collector<T, ?, SortedMap<Integer, List<T>>> denseRank(Comparator<? super T> comparator,
                                                           Comparator<Integer> rankOrder) {
        return RANKING_COLLECTOR.denseRank(comparator, rankOrder);
    }

    /**
     * Computes the rank of object using natural ordering. Equal objects for the ranking criteria
     * receive the same rank. Number of tied rows added to the next rank. Therefore, the ranks may not be
     * consecutive numbers. Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(1, 2, 3, 4, 4, 3, 2, 1);
     *     SortedMap<Integer, List<Integer>> rankedMap = list.stream().collect(CollectorEx.rank());
     *     System.out.println(rankedMap);
     * </pre>
     * This will prints {@code {1=[1, 1], 3=[2, 2], 5=[3, 3], 7=[4, 4]}}.
     *
     * @param <T> the type of stream objects
     * @return {@link SortedMap} containing ranks as keys and collection of ranked objects as values
     */
    public static <T extends Comparable<? super T>>
    Collector<T, ?, SortedMap<Integer, List<T>>> rank() {
        return RANKING_COLLECTOR.rank();
    }

    /**
     * Computes the rank of object using given comparator. Equal objects for the ranking criteria
     * receive the same rank. Number of tied rows added to the next rank. Therefore, the ranks may not be
     * consecutive numbers. Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(1, 2, 3, 4, 4, 3, 2, 1);
     *     Comparator<Integer> intComparator = Integer::compare;
     *     SortedMap<Integer, List<Integer>> rankedMap = list.stream().collect(CollectorEx.rank(intComparator.reversed()));
     *     System.out.println(rankedMap);
     * </pre>
     * This will prints {@code {1=[4, 4], 3=[3, 3], 5=[2, 2], 7=[1, 1]}}.
     *
     * @param comparator the comparator for custom ordering
     * @param <T>        the type of stream objects
     * @return {@link SortedMap} containing ranks as keys and collection of ranked objects as values
     */
    public static <T>
    Collector<T, ?, SortedMap<Integer, List<T>>> rank(Comparator<? super T> comparator) {
        return RANKING_COLLECTOR.rank(comparator);
    }

    /**
     * Computes the rank of object using given comparator. Equal objects for the ranking criteria
     * receive the same rank. Number of tied rows added to the next rank. Therefore, the ranks may not be
     * consecutive numbers. Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(1, 2, 3, 4, 4, 3, 2, 1);
     *     Comparator<Integer> intComparator = Integer::compare;
     *     SortedMap<Integer, List<Integer>> rankedMap = list.stream().collect(CollectorEx.rank(intComparator.reversed()));
     *     System.out.println(rankedMap);
     * </pre>
     * This will prints {@code {1=[4, 4], 3=[3, 3], 5=[2, 2], 7=[1, 1]}}.
     *
     * @param downstream the reduction operation (e.g. the downstream collector)
     * @param <T>        the type of stream objects
     * @param <R>        the result type of the downstream collector
     * @return {@link SortedMap} containing ranks as keys and collection of ranked objects as values
     */
    public static <T extends Comparable<? super T>, R>
    Collector<T, ?, SortedMap<Integer, R>> rank(Collector<? super T, ?, R> downstream) {
        return RANKING_COLLECTOR.rank(downstream);
    }

    /**
     * Computes the rank of object using given comparator and sorts ranks according the second comparator.
     * Equal objects for the ranking criteria receive the same rank. Number of tied rows added to
     * the next rank. Therefore, the ranks may not be consecutive numbers. Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(1, 2, 3, 4, 4, 3, 2, 1);
     *     Comparator<Integer> intComparator = Integer::compare;
     *     SortedMap<Integer, List<Integer>> rankedMap = list.stream()
     *                          .collect(CollectorEx.rank(intComparator, intComparator.reversed()));
     *     System.out.println(rankedMap);
     * </pre>
     * This will prints {@code {7=[4, 4], 5=[3, 3], 3=[2, 2], 1=[1, 1]}}.
     *
     * @param comparator the comparator for custom ordering
     * @param rankOrder  the comparator for custom rank ordering
     * @return {@link SortedMap} containing ranks as keys and collection of ranked objects as values
     */
    public static <T>
    Collector<T, ?, SortedMap<Integer, List<T>>> rank(Comparator<? super T> comparator,
                                                      Comparator<Integer> rankOrder) {
        return RANKING_COLLECTOR.rank(comparator, rankOrder);
    }

    /**
     * Computes the rank of object using given comparator and sorts ranks according the second comparator.
     * Returns {@link SortedMap} with ranks as keys and result of applied reduction operation as values.
     * Equal objects for the ranking criteria receive the same rank. Number of tied rows added to
     * the next rank. Therefore, the ranks may not be consecutive numbers. Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(1, 2, 3, 4, 4, 3, 2, 1);
     *     Comparator<Integer> intComparator = Integer::compare;
     *     SortedMap<Integer, List<Integer>> rankedMap = list.stream()
     *                          .collect(CollectorEx.rank(intComparator, intComparator.reversed()));
     *     System.out.println(rankedMap);
     * </pre>
     * This will prints {@code {7=[4, 4], 5=[3, 3], 3=[2, 2], 1=[1, 1]}}.
     *
     * @param comparator the comparator for custom ordering
     * @param rankOrder  the comparator for custom rank ordering
     * @param denseRank  the dense rank flag
     * @param downstream the reduction operation (e.g. the downstream collector)
     * @return {@link SortedMap} containing ranks as keys and collection of ranked objects as values
     */
    public static <T, R>
    Collector<T, ?, SortedMap<Integer, R>> rank(Comparator<? super T> comparator,
                                                Comparator<Integer> rankOrder,
                                                boolean denseRank,
                                                Collector<? super T, ?, R> downstream) {
        return RANKING_COLLECTOR.rank(comparator, rankOrder, denseRank, downstream);
    }

    /**
     * Maps stream of objects to the computed rank using natural ordering. The ranks are consecutive integers
     * beginning with 1. The largest rank value is the number of unique objects. Rank values are not skipped
     * in the event of ties. Equal objects for the ranking criteria receive the same rank. Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(-1, -2, -3, -4, -4, -3, -2, -1);
     *     Map<Integer, Integer> denseRankedMap = list.stream().collect(CollectorEx.mapObjToDenseRank());
     *     System.out.println(denseRankedMap);
     * </pre>
     * This will prints {@code {-1=4, -2=3, -3=2, -4=1}}.
     *
     * @param <T> the type of stream objects
     * @return {@link Map} containing objects as keys and ranks as values
     */
    public static <T extends Comparable<? super T>>
    Collector<T, ?, Map<T, Integer>> mapObjToDenseRank() {
        return RANKING_COLLECTOR.mapObjToDenseRank();
    }

    /**
     * Maps stream of objects to the computed rank using given comparator. The ranks are consecutive integers
     * beginning with 1. The largest rank value is the number of unique objects. Rank values are not skipped
     * in the event of ties. Equal objects for the ranking criteria receive the same rank. Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(-1, -2, -3, -4, -4, -3, -2, -1);
     *     Map<Integer, Integer> denseRankedMap = list.stream().collect(CollectorEx.mapObjToDenseRank());
     *     System.out.println(denseRankedMap);
     * </pre>
     * This will prints {@code {-1=4, -2=3, -3=2, -4=1}}.
     *
     * @param comparator the comparator for custom ordering
     * @param <T>        the type of stream objects
     * @return {@link Map} containing objects as keys and ranks as values
     */
    public static <T>
    Collector<T, ?, Map<T, Integer>> mapObjToDenseRank(Comparator<? super T> comparator) {
        return RANKING_COLLECTOR.mapObjToDenseRank(comparator);
    }

    /**
     * Maps result of applied <code>mapper</code> function to stream of objects to the computed rank using natural ordering.
     * The ranks are consecutive integers beginning with 1. The largest rank value is the number of unique objects.
     * Rank values are not skipped in the event of ties. Equal objects for the ranking criteria receive the same rank.
     *
     * @param mapper the mapper
     * @param <T>    the type of stream objects
     * @return {@link Map} containing objects as keys and ranks as values
     */
    public static <T extends Comparable<? super T>, R>
    Collector<T, ?, Map<R, Integer>> mapObjToDenseRank(Function<T, R> mapper) {
        return RANKING_COLLECTOR.mapObjToDenseRank(mapper);
    }

    /**
     * Maps stream of objects to the computed rank using natural ordering. Equal objects for the ranking criteria
     * receive the same rank. Number of tied rows added to the next rank. Therefore, the ranks may not be
     * consecutive numbers. Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(-1, -2, -3, -4, -4, -3, -2, -1);
     *     Map<Integer, Integer> rankedMap = list.stream().collect(CollectorEx.mapObjToRank());
     *     System.out.println(rankedMap);
     * </pre>
     * This will prints {@code {-1=7, -2=5, -3=3, -4=1}}.
     *
     * @param <T> the type of stream objects
     * @return {@link Map} containing ranks as keys and collection of ranked objects as values
     */
    public static <T extends Comparable<? super T>>
    Collector<T, ?, Map<T, Integer>> mapObjToRank() {
        return RANKING_COLLECTOR.mapObjToRank();
    }

    /**
     * Maps stream of objects to the computed rank using given comparator. Equal objects for the ranking criteria
     * receive the same rank. Number of tied rows added to the next rank. Therefore, the ranks may not be
     * consecutive numbers. Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(-1, -2, -3, -4, -4, -3, -2, -1);
     *     Map<Integer, Integer> rankedMap = list.stream().collect(CollectorEx.mapObjToRank());
     *     System.out.println(rankedMap);
     * </pre>
     * This will prints {@code {-1=7, -2=5, -3=3, -4=1}}.
     *
     * @param comparator the comparator for custom ordering
     * @param <T>        the type of stream objects
     * @return {@link Map} containing ranks as keys and collection of ranked objects as values
     */
    public static <T>
    Collector<T, ?, Map<T, Integer>> mapObjToRank(Comparator<? super T> comparator) {
        return RANKING_COLLECTOR.mapObjToRank(comparator);
    }

    /**
     * Maps result of applied <code>mapper</code> function to stream of objects to the computed rank using
     * natural ordering. Equal objects for the ranking criteria receive the same rank. Number of tied rows added
     * to the next rank. Therefore, the ranks may not be consecutive numbers. Example:
     *
     * @param mapper the mapper
     * @param <T>    the type of stream objects
     * @return {@link Map} containing ranks as keys and collection of ranked objects as values
     */
    public static <T extends Comparable<? super T>, R>
    Collector<T, ?, Map<R, Integer>> mapObjToRank(Function<T, R> mapper) {
        return RANKING_COLLECTOR.mapObjToRank(mapper);
    }

    /**
     * Maps result of applied <code>mapper</code> function to stream of objects to the computed rank using
     * given comparator. Equal objects for the ranking criteria receive the same rank. Number of tied rows added
     * to the next rank. Therefore, the ranks may not be consecutive numbers. Example:
     *
     * @param mapper the mapper
     * @param <T>    the type of stream objects
     * @return {@link Map} containing ranks as keys and collection of ranked objects as values
     */
    public static <T, R>
    Collector<T, ?, Map<R, Integer>> mapObjToRank(Function<T, R> mapper,
                                                  Comparator<? super T> comparator) {
        return RANKING_COLLECTOR.mapObjToRank(mapper, comparator, false);
    }

    /**
     * Returns the cumulative sum of ints for each stream element using natural ordering.
     * Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(1, 2, 3);
     *     List<Integer> result = list.stream().collect(CollectorEx.summingInt(i -> i));
     *     System.out.println(result);
     * </pre>
     * This will prints {@code [1, 3, 6]}.
     *
     * @param mapper the mapper
     * @param <T>    the type of stream objects
     * @return the list of cumulative sums
     */
    public static <T extends Comparable<? super T>>
    Collector<T, ?, List<Integer>> summingInt(ToIntFunction<T> mapper) {
        return SUMMING_INT_COLLECTOR.summingInt(mapper);
    }

    /**
     * Returns the cumulative sum of ints for each stream element using natural ordering.
     * Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(1, 2, 3);
     *     List<Integer> result = list.stream().collect(CollectorEx.summingInt(i -> i));
     *     System.out.println(result);
     * </pre>
     * This will prints {@code [1, 3, 6]}.
     *
     * @param mapper     the mapper
     * @param downstream the reduction operation (e.g. the downstream collector)
     * @param <T>        the type of stream objects
     * @return the list of cumulative sums
     */
    public static <T extends Comparable<? super T>, R>
    Collector<T, ?, R> summingInt(ToIntFunction<T> mapper,
                                  Collector<Integer, ?, R> downstream) {
        return SUMMING_INT_COLLECTOR.summingInt(mapper, downstream);
    }

    /**
     * Returns the cumulative sum of ints for each stream element using given comparator.
     * Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(1, 2, 3);
     *     Comparator<Integer> intComparator = Integer::compare;
     *     List<Integer> result = list.stream()
     *                  .collect(CollectorEx.summingInt(i -> i, intComparator.reversed()));
     *     System.out.println(result);
     * </pre>
     * This will prints {@code [3, 5, 6]}.
     *
     * @param mapper     the mapper
     * @param comparator the comparator for custom ordering
     * @param <T>        the type of stream objects
     * @return the list of cumulative sums
     */
    public static <T>
    Collector<T, ?, List<Integer>> summingInt(ToIntFunction<? super T> mapper,
                                              Comparator<? super T> comparator) {
        return SUMMING_INT_COLLECTOR.summingInt(mapper, comparator);
    }

    /**
     * Returns the cumulative sum of ints for each stream element using given comparator and downstream collector.
     * Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(1, 2, 3);
     *     Comparator<Integer> intComparator = Integer::compare;
     *     Set<Integer> result = list.stream()
     *                      .collect(CollectorEx.summingInt(i -> i, intComparator.reversed(), Collectors.toSet()));
     *     System.out.println(result);
     * </pre>
     * This will prints {@code [3, 5, 6]}.
     *
     * @param mapper     the mapper
     * @param comparator the comparator for custom ordering
     * @param downstream the reduction operation (e.g. the downstream collector)
     * @param <T>        the type of stream objects
     * @return the list of cumulative sums
     */
    public static <T, R>
    Collector<T, ?, R> summingInt(ToIntFunction<? super T> mapper,
                                  Comparator<? super T> comparator,
                                  Collector<Integer, ?, R> downstream) {
        return SUMMING_INT_COLLECTOR.summingInt(mapper, comparator, downstream);
    }

    /**
     * @see CollectorEx#summingInt(ToIntFunction)
     */
    public static <T extends Comparable<? super T>>
    Collector<T, ?, List<Long>> summingLong(ToLongFunction<T> mapper) {
        return SUMMING_LONG_COLLECTOR.summingLong(mapper);
    }

    /**
     * @see CollectorEx#summingInt(ToIntFunction, Collector)
     */
    public static <T extends Comparable<? super T>, R>
    Collector<T, ?, R> summingLong(ToLongFunction<T> mapper,
                                   Collector<Long, ?, R> downstream) {
        return SUMMING_LONG_COLLECTOR.summingLong(mapper, downstream);
    }

    /**
     * @see CollectorEx#summingInt(ToIntFunction, Comparator)
     */
    public static <T>
    Collector<T, ?, List<Long>> summingLong(ToLongFunction<? super T> mapper,
                                            Comparator<? super T> comparator) {
        return SUMMING_LONG_COLLECTOR.summingLong(mapper, comparator);
    }

    /**
     * @see CollectorEx#summingInt(ToIntFunction, Comparator, Collector)
     */
    public static <T, R>
    Collector<T, ?, R> summingLong(ToLongFunction<? super T> mapper,
                                   Comparator<? super T> comparator,
                                   Collector<Long, ?, R> downstream) {
        return SUMMING_LONG_COLLECTOR.summingLong(mapper, comparator, downstream);
    }

    /**
     * Returns the cumulative sum of doubles for each stream element using natural ordering.
     * Numbers are summed using <a href="https://en.wikipedia.org/wiki/Kahan_summation_algorithm">Kahan summation algorith</a>.
     *
     * @see CollectorEx#summingInt(ToIntFunction)
     */
    public static <T extends Comparable<? super T>>
    Collector<T, ?, List<Double>> summingDouble(ToDoubleFunction<T> mapper) {
        return SUMMING_DOUBLE_COLLECTOR.summingDouble(mapper);
    }

    /**
     * Returns the cumulative sum of doubles for each stream element using natural ordering and given downstream collector.
     * Numbers are summed using <a href="https://en.wikipedia.org/wiki/Kahan_summation_algorithm">Kahan summation algorith</a>.
     *
     * @see CollectorEx#summingInt(ToIntFunction, Collector)
     */
    public static <T extends Comparable<? super T>, R>
    Collector<T, ?, R> summingDouble(ToDoubleFunction<T> mapper,
                                     Collector<Double, ?, R> downstream) {
        return SUMMING_DOUBLE_COLLECTOR.summingDouble(mapper, downstream);
    }

    /**
     * Returns the cumulative sum of doubles for each stream element using given comparator.
     * Numbers are summed using <a href="https://en.wikipedia.org/wiki/Kahan_summation_algorithm">Kahan summation algorith</a>.
     *
     * @see CollectorEx#summingInt(ToIntFunction, Comparator)
     */
    public static <T>
    Collector<T, ?, List<Double>> summingDouble(ToDoubleFunction<? super T> mapper,
                                                Comparator<? super T> comparator) {
        return SUMMING_DOUBLE_COLLECTOR.summingDouble(mapper, comparator);
    }

    /**
     * Returns the cumulative sum of doubles for each stream element using given comparator and downstream collector.
     * Numbers are summed using <a href="https://en.wikipedia.org/wiki/Kahan_summation_algorithm">Kahan summation algorith</a>.
     *
     * @see CollectorEx#summingInt(ToIntFunction, Comparator, Collector)
     */
    public static <T, R>
    Collector<T, ?, R> summingDouble(ToDoubleFunction<? super T> mapper,
                                     Comparator<? super T> comparator,
                                     Collector<Double, ?, R> downstream) {
        return SUMMING_DOUBLE_COLLECTOR.summingDouble(mapper, comparator, downstream);
    }

    /**
     * @see CollectorEx#summingInt(ToIntFunction)
     */
    public static <T extends Comparable<? super T>>
    Collector<T, ?, List<BigDecimal>> summingBigDecimal(Function<T, BigDecimal> mapper) {
        return SUMMING_BIG_DECIMAL_COLLECTOR.summingBigDecimal(mapper);
    }

    /**
     * @see CollectorEx#summingInt(ToIntFunction, Collector)
     */
    public static <T extends Comparable<? super T>, R>
    Collector<T, ?, R> summingBigDecimal(Function<T, BigDecimal> mapper,
                                         Collector<BigDecimal, ?, R> downstream) {
        return SUMMING_BIG_DECIMAL_COLLECTOR.summingBigDecimal(mapper, downstream);
    }

    /**
     * @see CollectorEx#summingInt(ToIntFunction, Comparator)
     */
    public static <T>
    Collector<T, ?, List<BigDecimal>> summingBigDecimal(Function<? super T, BigDecimal> mapper,
                                                        Comparator<? super T> comparator) {
        return SUMMING_BIG_DECIMAL_COLLECTOR.summingBigDecimal(mapper, comparator);
    }

    /**
     * @see CollectorEx#summingInt(ToIntFunction, Comparator, Collector)
     */
    public static <T, R>
    Collector<T, ?, R> summingBigDecimal(Function<? super T, BigDecimal> mapper,
                                         Comparator<? super T> comparator,
                                         Collector<BigDecimal, ?, R> downstream) {
        return SUMMING_BIG_DECIMAL_COLLECTOR.summingBigDecimal(mapper, comparator, downstream);
    }

    /**
     * Returns set of elements which appears most often in a stream.
     * Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(1, 3, 6, 6, 6, 6, 7, 7, 12, 12, 17);
     *     Set<Integer> result = list.stream().collect(CollectorEx.mode());
     *     System.out.println(result);
     * </pre>
     * This will prints {@code [6]}
     *
     * @param <T> the type of stream objects
     * @return the {@link Set} of elements
     */
    public static <T>
    Collector<T, ?, Set<T>> mode() {
        return MODE_COLLECTOR.mode();
    }

    /**
     * Applies given <code>mapper function</code> to elements of stream and returns set of elements which
     * resulting values appears most often in a stream.
     * Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(1, -1, 2, -2, 3, 4);
     *     Set<Integer> result = list.stream().collect(CollectorEx.mode(Math::abs));
     *     System.out.println(result);
     * </pre>
     * This will prints {@code [1, 2]}
     *
     * @param <T> the type of stream objects
     * @return the {@link Set} of elements
     */
    public static <T, D>
    Collector<T, ?, Set<D>> mode(Function<? super T, D> mapper) {
        return MODE_COLLECTOR.mode(mapper);
    }

    /**
     * Returns collection of elements which appears most often in a stream.
     * Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(1, 3, 6, 6, 6, 6, 7, 7, 12, 12, 17);
     *     List<Integer> result = list.stream().collect(CollectorEx.mode(Collectors.toList()));
     *     System.out.println(result);
     * </pre>
     * This will prints {@code [6]}
     *
     * @param downstream the reduction operation (e.g. the downstream collector)
     * @param <T>        the type of stream objects
     * @return the {@link Set} of elements
     */
    public static <T, R>
    Collector<T, ?, R> mode(Collector<? super T, ?, R> downstream) {
        return MODE_COLLECTOR.mode(downstream);
    }

    /**
     * Applies given <code>mapper function</code> to elements of stream and returns collection of elements which
     * resulting values appears most often in a stream.
     * Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(1, -1, 2, -2, 3, 4);
     *     List<Integer> result = list.stream().collect(CollectorEx.mode(Math::abs, Collectors.toList()));
     *     System.out.println(result);
     * </pre>
     * This will prints {@code [1, 2]}
     *
     * @param downstream the reduction operation (e.g. the downstream collector)
     * @param <T>        the type of stream objects
     * @return the {@link Set} of elements
     */
    public static <T, D, R>
    Collector<T, ?, R> mode(Function<? super T, D> mapper,
                            Collector<? super D, ?, R> downstream) {
        return MODE_COLLECTOR.mode(mapper, downstream);
    }

    /**
     * Divides stream of objects into a number of buckets using natural ordering.
     * Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(null, 1, 1, 2, 3, null);
     *     Map<Integer, List<Integer>> result = list.stream().collect(CollectorEx.ntile(2));
     *     System.out.println(result);
     * </pre>
     * This will prints {@code {1=[1, 1, 2], 2=[3, null, null]}}
     *
     * @param tiles number of tiles
     * @param <T>   the type of stream objects
     * @return {@link Map} with tile indexes as keys and collection of objects as values
     */
    public static <T extends Comparable<? super T>>
    Collector<T, ?, Map<Integer, List<T>>> ntile(int tiles) {
        return N_TILE_COLLECTOR.ntile(tiles);
    }

    /**
     * Divides stream of objects into a number of buckets using given comparator.
     * Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(null, 1, 1, 2, 3, null);
     *     Comparator<Integer> integerComparator = Comparator.nullsFirst(Integer::compareTo);
     *     Map<Integer, List<Integer>> result = list.stream().collect(CollectorEx.ntile(2, integerComparator));
     *     System.out.println(result);
     * </pre>
     * This will prints {@code {1=[null, null, 1], 2=[1, 2, 3]}}
     *
     * @param tiles      number of tiles
     * @param comparator the comparator for custom ordering
     * @param <T>        the type of stream objects
     * @return {@link Map} with tile indexes as keys and collection of objects as values
     */
    public static <T>
    Collector<T, ?, Map<Integer, List<T>>> ntile(int tiles,
                                                 Comparator<? super T> comparator) {
        return N_TILE_COLLECTOR.ntile(tiles, comparator);
    }

    /**
     * Divides stream of objects into a number of buckets using natural ordering and given downstream collector.
     * Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(null, 1, 1, 2, 3, null);
     *     Map<Integer, Set<Integer>> result = list.stream().collect(CollectorEx.ntile(2, Collectors.toSet()));
     *     System.out.println(result);
     * </pre>
     * This will prints {@code {1=[1, 2], 2=[null, 3]}}
     *
     * @param tiles      number of tiles
     * @param downstream the reduction operation (e.g. the downstream collector)
     * @param <T>        the type of stream objects
     * @return {@link Map} with tile indexes as keys and collection of objects as values
     */
    public static <T extends Comparable<? super T>, R>
    Collector<T, ?, Map<Integer, R>> ntile(int tiles,
                                           Collector<? super T, ?, R> downstream) {
        return N_TILE_COLLECTOR.ntile(tiles, downstream);
    }

    /**
     * Divides stream of objects into a number of buckets using given comparator and downstream collector.
     * Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(null, 1, 1, 2, 3, null);
     *     Comparator<Integer> integerComparator = Comparator.nullsFirst(Integer::compareTo);
     *     Map<Integer, Set<Integer>> result = list.stream().collect(CollectorEx.ntile(2, integerComparator.reversed(), Collectors.toSet()));
     *     System.out.println(result);
     * </pre>
     * This will prints {@code {1=[1, 2, 3], 2=[null, 1]}}
     *
     * @param tiles      number of tiles
     * @param comparator the comparator for custom ordering
     * @param downstream the reduction operation (e.g. the downstream collector)
     * @param <T>        the type of stream objects
     * @return {@link Map} with tile indexes as keys and collection of objects as values
     */
    public static <T, R>
    Collector<T, ?, Map<Integer, R>> ntile(int tiles,
                                           Comparator<? super T> comparator,
                                           Collector<? super T, ?, R> downstream) {
        return N_TILE_COLLECTOR.ntile(tiles, comparator, downstream);
    }

    public static <T extends Comparable<? super T>>
    Collector<T, ?, List<T>> rankDistinct() {
        return RANK_DISTINCT_COLLECTOR.rankDistinct();
    }

    public static <T>
    Collector<T, ?, List<T>> rankDistinct(Comparator<? super T> comparator) {
        return RANK_DISTINCT_COLLECTOR.rankDistinct(comparator);
    }

    public static <T extends Comparable<? super T>, R>
    Collector<T, ?, R> rankDistinct(Collector<? super T, ?, R> downstream) {
        return RANK_DISTINCT_COLLECTOR.rankDistinct(downstream);
    }

    public static <T, D extends Comparable<? super D>>
    Collector<T, ?, List<D>> rankDistinct(Function<? super T, D> mapper) {
        return RANK_DISTINCT_COLLECTOR.rankDistinct(mapper);
    }

    public static <T, R>
    Collector<T, ?, R> rankDistinct(Comparator<? super T> comparator,
                                    Collector<? super T, ?, R> downstream) {
        return RANK_DISTINCT_COLLECTOR.rankDistinct(comparator, downstream);
    }

    public static <T, D>
    Collector<T, ?, List<D>> rankDistinct(Comparator<? super D> comparator,
                                          Function<? super T, D> mapper) {
        return RANK_DISTINCT_COLLECTOR.rankDistinct(comparator, mapper);
    }

    public static <T, D extends Comparable<? super D>, R>
    Collector<T, ?, R> rankDistinct(Function<? super T, D> mapper,
                                    Collector<? super D, ?, R> downstream) {
        return RANK_DISTINCT_COLLECTOR.rankDistinct(mapper, downstream);
    }

    public static <T, D, R>
    Collector<T, ?, R> rankDistinct(Comparator<? super D> comparator,
                                    Function<? super T, D> mapper,
                                    Collector<? super D, ?, R> downstream) {
        return RANK_DISTINCT_COLLECTOR.rankDistinct(comparator, mapper, downstream);
    }

    /**
     * Return distinct elements of stream using given mapper function.
     * Example:
     * <pre>
     *     List<Integer> list = Arrays.asList(-1, 2, 2, 1, 1);
     *     Collection<Integer> result = list.stream().collect(CollectorEx.distinct(Math::abs));
     *     System.out.println(result);
     * </pre>
     * This will prints {@code [1, 2]}
     *
     * @param mapper the mapping function
     * @param <T>    the type of stream objects
     * @param <D>    the return type of mapping object
     * @return the collection of distinct stream objects
     */
    public static <T, D>
    Collector<T, ?, Collection<T>> distinct(Function<? super T, D> mapper) {
        return DISTINCT_COLLECTOR.distinct(mapper);
    }

    /**
     * Return distinct elements of stream using given mapper function
     * <pre>
     *     List<Integer> list = Arrays.asList(-1, 2, 2, 1, 1);
     *     List<Integer> result = list.stream().collect(CollectorEx.distinct(Math::abs, Collectors.toList()));
     *     System.out.println(result);
     * </pre>
     *
     * @param mapper     the mapping function
     * @param downstream the reduction operation (e.g. the downstream collector)
     * @param <T>        the type of stream objects
     * @param <D>        the return type of mapping object
     * @param <R>        the result type of the reduction operation
     * @return the collection of distinct stream objects
     */
    public static <T, D, R>
    Collector<T, ?, R> distinct(Function<? super T, D> mapper,
                                Collector<? super T, ?, R> downstream) {
        return DISTINCT_COLLECTOR.distinct(mapper, downstream);
    }

    /**
     * Collects stream of {@link Map map's} to multi value map.
     * Example:
     * <pre>
     *
     *     List<Map<Integer, Integer>> list = Arrays.asList(
     *                             singletonMap(null, null), singletonMap(0, 0),
     *                             singletonMap(1, 1), singletonMap(1, -1),
     *                             singletonMap(2, 2), singletonMap(2, -2));
     *     Map<Integer, List<Integer>> result = list.stream().collect(CollectorEx.mapStreamToMultiValueMap());
     *     System.out.println(result);
     * </pre>
     * This will prints {@code {0=[0], null=[null], 1=[1, -1], 2=[2, -2]}}.
     *
     * @param <K> the type of map keys
     * @param <V> the type of map values
     * @return the map with collection of values for the same key entries
     */
    public static <K, V>
    Collector<Map<K, V>, ?, Map<K, List<V>>> mapStreamToMultiValueMap() {
        return MULTI_VALUE_MAP_COLLECTOR.mapStreamToMultiValueMap();
    }

    /**
     * Collects stream of {@link Map map's} to multi value map using given downstream collector.
     * Example:
     * <pre>
     *
     *     List<Map<Integer, Integer>> list = Arrays.asList(
     *                             singletonMap(null, null), singletonMap(0, 0),
     *                             singletonMap(1, 1), singletonMap(1, -1),
     *                             singletonMap(2, 2), singletonMap(2, -2));
     *     Map<Integer, List<Integer>> result = list.stream().collect(Collectors.counting());
     *     System.out.println(result);
     * </pre>
     * This will prints {@code {0=1, null=1, 1=2, 2=2}}.
     *
     * @param downstream the reduction operation (e.g. the downstream collector)
     * @param <K>        the type of map keys
     * @param <V>        the type of map values
     * @return the map with collection of values for the same key entries
     */
    public static <K, V, R>
    Collector<Map<K, V>, ?, Map<K, R>> mapStreamToMultiValueMap(Collector<? super V, ?, R> downstream) {
        return MULTI_VALUE_MAP_COLLECTOR.mapStreamToMultiValueMap(downstream);
    }

    /**
     * Collects stream of {@link Entry map entries} to multi value map.
     *
     * @param <K> the type of map keys
     * @param <V> the type of map values
     * @return the map with collection of values for the same key entries
     */
    public static <K, V>
    Collector<Entry<K, V>, ?, Map<K, List<V>>> entryStreamToMultiValueMap() {
        return MULTI_VALUE_MAP_COLLECTOR.entryStreamToMultiValueMap();
    }

    /**
     * Collects stream of {@link Entry map entries} to multi value map using given downstream collector.
     *
     * @param downstream the reduction operation (e.g. the downstream collector)
     * @param <K>        the type of map keys
     * @param <V>        the type of map values
     * @return the map with collection of values for the same key entries
     */
    public static <K, V, R>
    Collector<Entry<K, V>, ?, Map<K, R>> entryStreamToMultiValueMap(Collector<? super V, ?, R> downstream) {
        return MULTI_VALUE_MAP_COLLECTOR.entryStreamToMultiValueMap(downstream);
    }

    static <T> List<T> listCombiner(List<T> left, List<T> right) {
        left.addAll(right);
        return left;
    }
}