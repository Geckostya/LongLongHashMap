package nedikov.compactMap;

public interface LongLongMap {
  long get(long key);
  long put(long key, long value);
  long remove(long key);
}
