package org.agzamovr.collectors;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.agzamovr.collectors.ModeCollector.MODE_COLLECTOR;
import static org.assertj.core.api.Assertions.assertThat;

public class ModeCollectorTest {

    @Test
    public void testModeIntegerList() {
        List<Integer> list = Arrays.asList(1, 1, 2, 2, 3, 4, null, null);

        Set<Integer> result = list.stream().collect(MODE_COLLECTOR.mode());

        assertThat(result).contains(1, 2, null);
    }

    @Test
    public void testModeWithCustomMapper() {
        List<Integer> list = Arrays.asList(1, -1, 2, -2, 3, 4);

        Set<Integer> result = list.stream().collect(MODE_COLLECTOR.mode(Math::abs));

        assertThat(result).contains(1, 2);
    }
}