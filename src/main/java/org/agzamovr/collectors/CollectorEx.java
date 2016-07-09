package org.agzamovr.collectors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.stream.Collector;

import static java.util.Comparator.*;

public class CollectorEx {

    public static <T extends Comparable<? super T>>
    Collector<T, List<T>, SortedMap<Integer, List<T>>> rank() {
        return rank(nullsLast(Comparator.<T>naturalOrder()));
    }

    public static <T extends Comparable<? super T>>
    Collector<T, List<T>, SortedMap<Integer, List<T>>> denseRank() {
        Ranker<T> ranker = new Ranker<>(nullsLast(Comparator.<T>naturalOrder()), true);
        return rank(ranker);
    }

    public static <T extends Comparable<? super T>>
    Collector<T, List<T>, SortedMap<Integer, List<T>>> rankNullsFirst() {
        return rank(nullsFirst(Comparator.<T>naturalOrder()));
    }

    public static <T extends Comparable<? super T>>
    Collector<T, List<T>, SortedMap<Integer, List<T>>> rankReversed() {
        return rank(nullsLast(Comparator.<T>naturalOrder().reversed()));
    }

    public static <T extends Comparable<? super T>>
    Collector<T, List<T>, SortedMap<Integer, List<T>>> rankReversedNullsFirst() {
        return rank(nullsFirst(Comparator.<T>naturalOrder().reversed()));
    }

    public static <T>
    Collector<T, List<T>, SortedMap<Integer, List<T>>> rank(Comparator<T> comparator) {
        Ranker<T> ranker = new Ranker<>(comparator);
        return rank(ranker);
    }

    public static <T>
    Collector<T, List<T>, SortedMap<Integer, List<T>>> rank(Comparator<T> comparator,
                                                            Comparator<Integer> rankOrder) {
        Ranker<T> ranker = new Ranker<>(comparator, rankOrder);
        return rank(ranker);
    }

    public static <T extends Comparable<? super T>, A, R>
    Collector<T, List<T>, SortedMap<Integer, R>> rank(Collector<? super T, A, R> downstream) {
        Ranker<T> ranker = new Ranker<>(Comparator.<T>nullsLast(naturalOrder()));
        return rank(ranker, downstream);
    }

    public static <T, A, R>
    Collector<T, List<T>, SortedMap<Integer, R>> rank(Comparator<T> comparator,
                                                      Collector<? super T, A, R> downstream) {
        Ranker<T> ranker = new Ranker<>(comparator);
        return rank(ranker, downstream);
    }

    public static <T, A, R>
    Collector<T, List<T>, SortedMap<Integer, R>> rank(Comparator<Integer> rankOrder,
                                                      Comparator<T> comparator,
                                                      Collector<? super T, A, R> downstream) {
        Ranker<T> ranker = new Ranker<>(comparator, rankOrder);
        return rank(ranker, downstream);
    }

    public static <T>
    Collector<T, List<T>, SortedMap<Integer, List<T>>> rank(Ranker<T> ranker) {
        return Collector.of(ArrayList::new,
                List::add,
                CollectorEx::combiner,
                ranker::rank);
    }

    public static <T, R>
    Collector<T, List<T>, SortedMap<Integer, R>> rank(Ranker<T> ranker,
                                                      Collector<? super T, ?, R> downstream) {
        return Collector.of(ArrayList::new,
                List::add,
                CollectorEx::combiner,
                (list) -> ranker.rank(list, downstream));
    }

    private static <T> List<T> combiner(List<T> left, List<T> right) {
        left.addAll(right);
        return left;
    }
}