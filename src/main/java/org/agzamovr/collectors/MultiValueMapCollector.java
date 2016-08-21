package org.agzamovr.collectors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;

class MultiValueMapCollector {
    static final MultiValueMapCollector MULTI_VALUE_MAP_COLLECTOR = new MultiValueMapCollector();

    <T, V, R>
    Map<T, R> multiValueMapFinisher(Map<T, List<V>> map,
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
    Collector<Map<T, V>, Map<T, List<V>>, Map<T, R>> mapStreamToMultiValueMap(Collector<? super V, ?, R> downstream) {
        return Collector.of(HashMap::new,
                this::accumulator,
                this::combiner,
                (map) -> multiValueMapFinisher(map, downstream));
    }

    <T, V>
    Collector<Entry<T, V>, ?, Map<T, List<V>>> entryStreamToMultiValueMap() {
        return toMultiValueMap(Entry::getKey, Entry::getValue);
    }

    <T, V, R>
    Collector<Entry<T, V>, Map<T, List<V>>, Map<T, R>> entryStreamToMultiValueMap(Collector<? super V, ?, R> downstream) {
        return toMultiValueMap(Entry::getKey, Entry::getValue, downstream);
    }

    <T, K, V>
    Collector<T, Map<K, List<V>>, Map<K, List<V>>> toMultiValueMap(Function<? super T, ? extends K> keyMapper,
                                                                   Function<? super T, ? extends V> valueMapper) {
        BiConsumer<Map<K, List<V>>, T> accumulator
                = (map, element) -> {
            K key = keyMapper.apply(element);
            V value = valueMapper.apply(element);
            List<V> list = map.computeIfAbsent(key, k -> new ArrayList<>());
            list.add(value);
        };
        return Collector.of(HashMap::new,
                accumulator,
                this::combiner,
                Characteristics.IDENTITY_FINISH);
    }

    <T, K, V, R>
    Collector<T, Map<K, List<V>>, Map<K, R>> toMultiValueMap(Function<? super T, ? extends K> keyMapper,
                                                             Function<? super T, ? extends V> valueMapper,
                                                             Collector<? super V, ?, R> downstream) {
        BiConsumer<Map<K, List<V>>, T> accumulator
                = (map, element) -> {
            K key = keyMapper.apply(element);
            V value = valueMapper.apply(element);
            List<V> list = map.computeIfAbsent(key, k -> new ArrayList<>());
            list.add(value);
        };
        return Collector.of(HashMap::new,
                accumulator,
                this::combiner,
                (map) -> multiValueMapFinisher(map, downstream));
    }
}
