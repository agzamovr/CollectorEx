package org.agzamovr.collectors;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;

class DistinctCollector {
    static final DistinctCollector DISTINCT_COLLECTOR = new DistinctCollector();

    <T, D, R>
    R distinctFinisher(Map<D, T> map,
                       Collector<? super T, ?, R> downstream) {
        return map.values().stream().collect(downstream);
    }

    <T, D>
    void accumulator(Map<D, T> map, T item, Function<? super T, D> mapper) {
        map.put(mapper.apply(item), item);
    }

    <T, D>
    Map<D, T> combiner(Map<D, T> left, Map<D, T> right) {
        left.putAll(right);
        return left;
    }

    <T, D>
    Collector<T, Map<D, T>, Collection<T>> distinct(Function<? super T, D> mapper) {
        return Collector.of(HashMap::new,
                (map, item) -> accumulator(map, item, mapper),
                this::combiner,
                Map::values);
    }

    <T, D, R>
    Collector<T, Map<D, T>, R> distinct(Function<? super T, D> mapper,
                                        Collector<? super T, ?, R> downstream) {
        return Collector.of(HashMap::new,
                (map, item) -> accumulator(map, item, mapper),
                this::combiner,
                (map) -> distinctFinisher(map, downstream));
    }
}