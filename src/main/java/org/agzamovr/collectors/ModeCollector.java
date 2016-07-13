package org.agzamovr.collectors;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

class ModeCollector {
    static final ModeCollector MODE_COLLECTOR = new ModeCollector();

    <R>
    Set<R> modeFinisher(Map<R, Long> map) {
        if (map.isEmpty())
            return Collections.emptySet();
        Long max = Collections.max(map.values());
        return map.entrySet().stream()
                .filter(e -> Objects.equals(e.getValue(), max))
                .collect(mapping(Entry::getKey, toSet()));
    }

    <T, R>
    void accumulator(Map<R, Long> map, T item, Function<? super T, R> mapper) {
        R result = mapper.apply(item);
        map.merge(result, 1L, this::add);
    }

    <R>
    Map<R, Long> combiner(Map<R, Long> left, Map<R, Long> right) {
        right.forEach((item, count) -> left.merge(item, count, this::add));
        return left;
    }

    Long add(Long a, Long b) {
        return a + b;
    }

    <T>
    Collector<T, ?, Set<T>> mode() {
        return mode(Function.identity());
    }

    <T, R>
    Collector<T, ?, Set<R>> mode(Function<? super T, R> mapper) {
        return Collector.of((Supplier<Map<R, Long>>) HashMap::new,
                (map, item) -> accumulator(map, item, mapper),
                this::combiner,
                this::modeFinisher);
    }
}