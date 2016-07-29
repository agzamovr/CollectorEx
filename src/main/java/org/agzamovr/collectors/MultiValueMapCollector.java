package org.agzamovr.collectors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;

public class MultiValueMapCollector {
    static final MultiValueMapCollector MULTI_VALUE_MAP_COLLECTOR = new MultiValueMapCollector();

    <T, V, R>
    Map<T, R> finisher(Map<T, List<V>> map,
                       Collector<? super V, ?, R> downstream) {
        Map<T, R> resultMap = new HashMap<>();
        map.forEach((key, value) -> resultMap.put(key, value.stream().collect(downstream)));
        return resultMap;
    }

    <T, V>
    void accumulator(Map<T, List<V>> map, Map<T, V> item) {
        item.forEach((key, value) -> map.computeIfAbsent(key, k -> new ArrayList<>()).add(value));
    }

    <T, V>
    void accumulator(Map<T, List<V>> map, Entry<T, V> item) {
        map.computeIfAbsent(item.getKey(), k -> new ArrayList<>()).add(item.getValue());
    }

    <T, V>
    Map<T, List<V>> combiner(Map<T, List<V>> left, Map<T, List<V>> right) {
        right.forEach((key, value) -> left.merge(key, value, CollectorEx::listCombiner));
        return left;
    }

    <T, V>
    Collector<Map<T, V>, ?, Map<T, List<V>>> mapStreamToMultiValueMap() {
        return Collector.of(HashMap::new,
                this::accumulator,
                this::combiner,
                Characteristics.IDENTITY_FINISH);
    }

    <T, V, R>
    Collector<Map<T, V>, ?, Map<T, R>> mapStreamToMultiValueMap(Collector<? super V, ?, R> downstream) {
        return Collector.of((Supplier<Map<T, List<V>>>) HashMap::new,
                this::accumulator,
                this::combiner,
                (map) -> finisher(map, downstream));
    }

    <T, V>
    Collector<Entry<T, V>, ?, Map<T, List<V>>> entryStreamToMultiValueMap() {
        return Collector.of(HashMap::new,
                this::accumulator,
                this::combiner,
                Characteristics.IDENTITY_FINISH);
    }

    <T, V, R>
    Collector<Entry<T, V>, ?, Map<T, R>> entryStreamToMultiValueMap(Collector<? super V, ?, R> downstream) {
        return Collector.of((Supplier<Map<T, List<V>>>) HashMap::new,
                this::accumulator,
                this::combiner,
                (map) -> finisher(map, downstream));
    }
}
