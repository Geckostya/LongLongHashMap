package nedikov.compactMap;

import java.util.HashMap;

public class JavaLongLongMap implements LongLongMap {
  private HashMap<Long, Long> map = new HashMap<>(2);

  @Override
  public long get(long key) {
    return map.getOrDefault(key, Long.MIN_VALUE);
  }

  @Override
  public long put(long key, long value) {
    return returnNotNull(map.put(key, value));
  }

  @Override
  public long remove(long key) {
    return returnNotNull(map.remove(key));
  }

  private long returnNotNull(Long value) {
    return value != null ? value : Long.MIN_VALUE;
  }
}
