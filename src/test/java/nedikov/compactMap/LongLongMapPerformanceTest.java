package nedikov.compactMap;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;
import java.util.function.Supplier;

public class LongLongMapPerformanceTest {

  @Ignore
  @Test
  public void performanceTest() {
    performanceTest(CompactLongLongMap::new);
    performanceTest(OpenLongLongMap::new);
    performanceTest(JavaLongLongMap::new);
  }

  private void performanceTest(Supplier<LongLongMap> mapConsumer) {
    int times = 300;

    for (int seed = 0; seed < 100; seed++) {
      performanceTest(mapConsumer.get(), seed);
    }

    long maxTime = Long.MIN_VALUE;
    long minTime = Long.MAX_VALUE;
    long totalTime = 0;
    for (int seed = 0; seed < times; seed++) {
      LongLongMap map = mapConsumer.get();
      System.gc();
      long time = System.nanoTime();
      performanceTest(map, seed);
      time = System.nanoTime() - time;
      totalTime += time;
      maxTime = Math.max(time, maxTime);
      minTime = Math.min(time, minTime);
    }
    System.out.println(mapConsumer.get().getClass().getSimpleName() + ";\t"
        + "max time: " + toMs(maxTime)
        + "; min time: " + toMs(minTime)
        + "; average time: " + toMs(totalTime / times));
  }

  private static long toMs(long time) {
    return Math.round(time * 1e-6);
  }

  private static void performanceTest(LongLongMap map, int seed) {
    Random random = new Random(seed);
    int keyMax = 300_000;
    for (int i = 0; i < 500_000; i++) {
      switch (random.nextInt(3)) {
        case 0:
        case 1:
          map.put(random.nextInt(keyMax), random.nextLong());
          break;
        case 2:
          map.remove(random.nextInt(keyMax));
      }
    }
  }
}
