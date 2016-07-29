package org.agzamovr.collectors;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collector;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

class ModeCollector {
    static final ModeCollector MODE_COLLECTOR = new ModeCollector();

    <D, R>
    R modeFinisher(Map<D, Long> map,
                   Collector<? super D, ?, R> downstream) {
        Long max = Collections.max(map.values());
        return map.entrySet().stream()
                .filter(e -> Objects.equals(e.getValue(), max))
                .collect(mapping(Entry::getKey, downstream));
    }

    <T, D>
    void accumulator(Map<D, Long> map, T item, Function<? super T, D> mapper) {
        D result = mapper.apply(item);
        map.merge(result, 1L, this::add);
    }

    <D>
    Map<D, Long> combiner(Map<D, Long> left, Map<D, Long> right) {
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

    <T, D>
    Collector<T, ?, Set<D>> mode(Function<? super T, D> mapper) {
        return mode(mapper, toSet());
    }

    <T, R>
    Collector<T, ?, R> mode(Collector<? super T, ?, R> downstream) {
        return mode(Function.identity(), downstream);
    }

    <T, D, R>
    Collector<T, Map<D, Long>, R> mode(Function<? super T, D> mapper,
                                       Collector<? super D, ?, R> downstream) {
        return Collector.of(HashMap::new,
                (map, item) -> accumulator(map, item, mapper),
                this::combiner,
                (list) -> modeFinisher(list, downstream));
    }
}