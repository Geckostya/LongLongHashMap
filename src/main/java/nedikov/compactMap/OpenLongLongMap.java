package nedikov.compactMap;

public class OpenLongLongMap implements LongLongMap {
  private final int INIT_SIZE = 4;
  private long[] keys = new long[INIT_SIZE];
  private long[] values = new long[INIT_SIZE];
  private boolean[] used = new boolean[INIT_SIZE];
  private boolean[] deleted = new boolean[INIT_SIZE];
  private int size = 0;
  private long NULL_VALUE = Long.MIN_VALUE;

  private int getIndex(long key) {
    int i = (int) key % keys.length;
    while (used[i] && keys[i] != key) {
      if (++i == keys.length) {
        i = 0;
      }
    }
    return i;
  }

  @Override
  public long get(long key) {
    int i = getIndex(key);
    return isActual(i) ? values[i] : NULL_VALUE;
  }

  @Override
  public long put(long key, long value) {
    int i = getIndex(key);
    boolean actual = isActual(i);
    long prevValue = actual ? values[i] : NULL_VALUE;
    keys[i] = key;
    values[i] = value;
    if (!actual) {
      used[i] = true;
      deleted[i] = false;
      size++;
      if (2 * size > keys.length) {
        resize(2 * keys.length);
      }
    }
    return prevValue;
  }

  @Override
  public long remove(long key) {
    int i = getIndex(key);
    if (isActual(i)) {
      deleted[i] = true;
      size--;
      if (8 * size < values.length && size > 0) {
        resize(values.length / 2);
      }
      return values[i];
    }
    return NULL_VALUE;
  }

  private boolean isActual(int i) {
    return used[i] && !deleted[i];
  }

  private void resize(int length) {
    long[] oldKeys = keys;
    long[] oldValues = values;
    boolean[] oldUsed = used;
    boolean[] oldDeleted = deleted;
    keys = new long[length];
    values = new long[length];
    used = new boolean[length];
    deleted = new boolean[length];
    size = 0;
    for (int i = 0; i < oldKeys.length; i++) {
      if (oldUsed[i] && !oldDeleted[i]) {
        put(oldKeys[i], oldValues[i]);
      }
    }
  }

}
