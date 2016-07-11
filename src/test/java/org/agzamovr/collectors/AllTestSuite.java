package org.agzamovr.collectors;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({RankingCollectorTest.class,
        SummingIntCollectorTest.class,
        SummingLongCollectorTest.class,
        SummingDoubleCollectorTest.class,
        SummingBigDecimalCollectorTest.class})
public class AllTestSuite {
}
