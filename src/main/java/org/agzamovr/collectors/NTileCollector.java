package org.agzamovr.collectors;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.Comparator.nullsLast;
import static java.util.stream.Collectors.toList;

public class NTileCollector {
    static final NTileCollector N_TILE_COLLECTOR = new NTileCollector();

    <T, A, R>
    Map<Integer, R> ntileFinisher(List<T> list,
                                  int tiles,
                                  Comparator<? super T> comparator,
                                  Collector<? super T, A, R> downstream) {
        list.sort(comparator);
        int bucketSize = computeBucketSize(list.size(), tiles);
        int tile = 0;
        Map<Integer, R> map = new HashMap<>();
        Supplier<A> downstreamSupplier = downstream.supplier();
        Function<A, R> downstreamFinisher = downstream.finisher();
        BiConsumer<A, ? super T> downstreamAccumulator = downstream.accumulator();
        A container = null;
        for (int i = 0; i < list.size(); i++) {
            if (i % bucketSize == 0) {
                if (container != null)
                    map.put(tile, downstreamFinisher.apply(container));
                tile++;
                container = downstreamSupplier.get();
            }
            downstreamAccumulator.accept(container, list.get(i));
        }
        if (container != null)
            map.put(tile, downstreamFinisher.apply(container));
        return map;
    }

    private int computeBucketSize(int size, int tiles) {
        int r = (size % tiles == 0) ? 0 : 1;
        return size / tiles + r;
    }

    <T extends Comparable<? super T>>
    Collector<T, ?, Map<Integer, List<T>>> ntile(int tiles) {
        return ntile(tiles, nullsLast(Comparator.<T>naturalOrder()), toList());
    }

    <T>
    Collector<T, ?, Map<Integer, List<T>>> ntile(int tiles,
                                                 Comparator<? super T> comparator) {
        return ntile(tiles, comparator, toList());
    }

    <T extends Comparable<? super T>, R>
    Collector<T, ?, Map<Integer, R>> ntile(int tiles,
                                           Collector<? super T, ?, R> downstream) {
        return ntile(tiles, nullsLast(Comparator.<T>naturalOrder()), downstream);
    }

    <T, R>
    Collector<T, ?, Map<Integer, R>> ntile(int tiles,
                                           Comparator<? super T> comparator,
                                           Collector<? super T, ?, R> downstream) {
        return Collector.of((Supplier<List<T>>) ArrayList::new,
                List::add,
                CollectorEx::listCombiner,
                (list) -> ntileFinisher(list, tiles, comparator, downstream));
    }
}
