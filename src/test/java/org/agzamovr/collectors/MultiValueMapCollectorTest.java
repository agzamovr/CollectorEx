package org.agzamovr.collectors;

import org.junit.Test;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class MultiValueMapCollectorTest {


    @Test
    public void testListOfMapToMultiValueMap() {
        List<Map<Integer, Integer>> list = asList(singletonMap(null, null), getIdentityMap(2),
                getIdentityMap(3), getIdentityMap(4));
        Map<Integer, List<Integer>> result = list.parallelStream().collect(CollectorEx.mapStreamToMultiValueMap());
        assertThat(result).hasSize(5);
        assertThat(result).containsEntry(null, singletonList(null));
        assertThat(result).containsEntry(0, asList(0, 0, 0));
        assertThat(result).containsEntry(1, asList(1, 1, 1));
        assertThat(result).containsEntry(2, asList(2, 2));
        assertThat(result).containsEntry(3, singletonList(3));
    }

    @Test
    public void testListOfMapToMultiValueMapWithCustomCollector() {
        List<Map<Integer, Integer>> list = asList(singletonMap(null, null), getIdentityMap(2),
                getIdentityMap(3), getIdentityMap(4));
        Map<Integer, Long> result = list.parallelStream()
                .collect(CollectorEx.mapStreamToMultiValueMap(counting()));
        assertThat(result).hasSize(5);
        assertThat(result).containsEntry(null, 1L);
        assertThat(result).containsEntry(0, 3L);
        assertThat(result).containsEntry(1, 3L);
        assertThat(result).containsEntry(2, 2L);
        assertThat(result).containsEntry(3, 1L);
    }

    @Test
    public void testListOfEntriesToMultiValueMap() {
        List<Entry<Integer, Integer>> list = getEntryList();
        Map<Integer, List<Integer>> result = list.parallelStream().collect(CollectorEx.entryStreamToMultiValueMap());
        assertThat(result).hasSize(5);
        assertThat(result).containsEntry(null, singletonList(null));
        assertThat(result).containsEntry(1, asList(1, 2));
        assertThat(result).containsEntry(2, asList(2, 4));
        assertThat(result).containsEntry(3, asList(3, 6));
        assertThat(result).containsEntry(4, asList(4, 8));
    }

    @Test
    public void testListOfEntriesToMultiValueMapWithCustomCollector() {
        List<Entry<Integer, Integer>> list = getEntryList();
        Map<Integer, Long> result = list.parallelStream().collect(CollectorEx.entryStreamToMultiValueMap(counting()));

        assertThat(result).hasSize(5);
        assertThat(result).containsEntry(null, 1L);
        assertThat(result).containsEntry(1, 2L);
        assertThat(result).containsEntry(2, 2L);
        assertThat(result).containsEntry(3, 2L);
        assertThat(result).containsEntry(4, 2L);
    }

    private Map<Integer, Integer> getIdentityMap(int i) {
        Map<Integer, Integer> map = new HashMap<>();
        IntStream.range(0, i).forEach(k -> map.put(k, k));
        return map;
    }

    private List<Entry<Integer, Integer>> getEntryList() {
        List<Entry<Integer, Integer>> entries1 = IntStream.range(1, 5)
                .mapToObj(i -> new SimpleEntry<>(i, i))
                .collect(toList());
        List<Entry<Integer, Integer>> entries2 = IntStream.range(1, 5)
                .mapToObj(i -> new SimpleEntry<>(i, 2 * i))
                .collect(toList());
        entries1.addAll(entries2);
        entries1.add(new SimpleEntry<>(null, null));
        return entries1;
    }
}