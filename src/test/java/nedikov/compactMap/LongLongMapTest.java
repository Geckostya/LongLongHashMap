package nedikov.compactMap;

import org.junit.Test;

import java.util.Random;
import java.util.function.Supplier;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LongLongMapTest {

  @Test
  public void testJavaLongLongMap() {
    testLongLongMap(JavaLongLongMap::new);
  }

  @Test
  public void testOpenLongLongMap() {
    testLongLongMap(OpenLongLongMap::new);
  }

  @Test
  public void testCompactLongLongMap() {
    testLongLongMap(CompactLongLongMap::new); // 9712 / 7883560
  }

  private void testLongLongMap(Supplier<LongLongMap> supplier) {
    testThatPutReturnsPreviousValue(supplier.get());
    testThatGetReturnsExistenceValues(supplier.get());
    testThatRemoveReturnsRemovedValue(supplier.get());
    testThatRemoveRemovesElement(supplier.get());
    stressTest(supplier.get());
  }

  private void testThatPutReturnsPreviousValue(LongLongMap map) {
    map.put(1, 2);
    assertThat(map.put(1, 3), is(2L));
    assertThat(map.put(1, 4), is(3L));
    map.put(2, 2);
    assertThat(map.put(1, 1), is(4L));
    assertThat(map.put(2, 1), is(2L));
  }

  private void testThatGetReturnsExistenceValues(LongLongMap map) {
    map.put(1, 2);
    map.put(2, 2);
    map.put(3, 4);

    assertThat(map.get(1), is(2L));
    assertThat(map.get(2), is(2L));
    assertThat(map.get(3), is(4L));

    map.put(2, 3);

    assertThat(map.get(1), is(2L));
    assertThat(map.get(2), is(3L));
    assertThat(map.get(3), is(4L));
  }

  private void testThatRemoveReturnsRemovedValue(LongLongMap map) {
    map.put(1, 2);
    map.put(2, 3);
    map.put(3, 4);

    assertThat(map.remove(1), is(2L));
    assertThat(map.remove(3), is(4L));
    assertThat(map.remove(2), is(3L));
  }

  private void testThatRemoveRemovesElement(LongLongMap map) {
    assertThat(map.get(1), is(Long.MIN_VALUE));
    map.put(1, 2);
    assertThat(map.get(1), is(2L));
    map.remove(1);
    assertThat(map.get(1), is(Long.MIN_VALUE));
  }

  @SuppressWarnings("unused")
  private void stressTest(LongLongMap map) {
    Random random = new Random(0x1234);
    int keyMax = 400;
    int putCalls = 0;
    int uniquePuts = 0;
    int removeCalls = 0;
    int successRemoves = 0;
    for (int i = 0; i < 1_000_000; i++) {
      switch (random.nextInt(3)) {
        case 0:
        case 1:
          long key = random.nextInt(keyMax);
          long oldValue = map.get(key);
          long value = random.nextLong();
          assertThat("i = " + i, map.put(key, value), is(oldValue));
          assertThat("i = " + i, map.get(key), is(value));
          putCalls++;
          if (oldValue == Long.MIN_VALUE) {
            uniquePuts++;
          }
          break;
        case 2:
          key = random.nextInt(keyMax);
          oldValue = map.get(key);
          assertThat("i = " + i, map.remove(key), is(oldValue));
          removeCalls++;
          if (oldValue != Long.MIN_VALUE) {
            successRemoves++;
          }
      }
    }

//    try {
//      Thread.sleep(30000);
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }

//    System.out.println("unique put: " + Math.round(100f * uniquePuts / putCalls) + "%"
//        + "; successful remove: " + Math.round(100f * successRemoves / removeCalls) + "%");
  }
}
