package org.agzamovr.collectors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.Comparator.*;

public class CollectorEx {

    public static <T extends Comparable<? super T>>
    Collector<T, ?, SortedMap<Integer, List<T>>> rank() {
        return rank(nullsLast(Comparator.<T>naturalOrder()));
    }

    public static <T extends Comparable<? super T>>
    Collector<T, ?, SortedMap<Integer, List<T>>> denseRank() {
        Rank<T> rank = new Rank<>(nullsLast(Comparator.<T>naturalOrder()), true);
        return rank(rank);
    }

    public static <T extends Comparable<? super T>>
    Collector<T, ?, SortedMap<Integer, List<T>>> rankNullsFirst() {
        return rank(nullsFirst(Comparator.<T>naturalOrder()));
    }

    public static <T extends Comparable<? super T>>
    Collector<T, ?, SortedMap<Integer, List<T>>> denseRankNullsFirst() {
        Rank<T> rank = new Rank<>(nullsFirst(Comparator.<T>naturalOrder()), true);
        return rank(rank);
    }

    public static <T extends Comparable<? super T>>
    Collector<T, ?, SortedMap<Integer, List<T>>> rankReversed() {
        return rank(nullsLast(Comparator.<T>naturalOrder().reversed()));
    }

    public static <T extends Comparable<? super T>>
    Collector<T, ?, SortedMap<Integer, List<T>>> rankReversedNullsFirst() {
        return rank(nullsFirst(Comparator.<T>naturalOrder().reversed()));
    }

    public static <T extends Comparable<? super T>>
    Collector<T, ?, SortedMap<Integer, List<T>>> denseRankReversed() {
        Rank<T> rank = new Rank<>(nullsLast(Comparator.<T>naturalOrder().reversed()), true);
        return rank(rank);
    }

    public static <T extends Comparable<? super T>>
    Collector<T, ?, SortedMap<Integer, List<T>>> denseRankReversedNullsFirst() {
        Rank<T> rank = new Rank<>(nullsFirst(Comparator.<T>naturalOrder().reversed()), true);
        return rank(rank);
    }

    public static <T>
    Collector<T, ?, SortedMap<Integer, List<T>>> rank(Comparator<T> comparator) {
        Rank<T> rank = new Rank<>(comparator);
        return rank(rank);
    }

    public static <T>
    Collector<T, ?, SortedMap<Integer, List<T>>> rank(Comparator<T> comparator,
                                                            Comparator<Integer> rankOrder) {
        Rank<T> rank = new Rank<>(comparator, rankOrder);
        return rank(rank);
    }

    public static <T extends Comparable<? super T>, A, R>
    Collector<T, ?, SortedMap<Integer, R>> rank(Collector<? super T, A, R> downstream) {
        Rank<T> rank = new Rank<>(Comparator.<T>nullsLast(naturalOrder()));
        return rank(rank, downstream);
    }

    public static <T>
    Collector<T, ?, SortedMap<Integer, List<T>>> rank(Rank<T> rank) {
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                rank::rank);
    }

    public static <T, R>
    Collector<T, ?, SortedMap<Integer, R>> rank(Rank<T> rank,
                                                Collector<? super T, ?, R> downstream) {
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> rank.rank(list, downstream));
    }

    private static <T> List<T> listCombiner(List<T> left, List<T> right) {
        left.addAll(right);
        return left;
    }
}