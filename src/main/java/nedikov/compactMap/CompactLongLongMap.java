package nedikov.compactMap;

public class CompactLongLongMap implements LongLongMap {
  private static final long NULL_VALUE = Long.MIN_VALUE;
  private static final long NEW_SIZE_MULTIPLIER = 32;

  private FastArrayList<MapBlock> blocks = new FastArrayList<>(new MapBlock[2]);
  private int size;

  public CompactLongLongMap() {
    blocks.add(new MapBlock());
    blocks.add(new MapBlock());
  }

  @Override
  public long get(long key) {
    return blocks.get(hash(key)).find(key);
  }

  @Override
  public long put(long key, long value) {
    if (size > NEW_SIZE_MULTIPLIER * blocks.size()) {
      growTable();
    }
    return blocks.get(hash(key)).put(key, value);
  }

  private void growTable() {
    int ind = blocks.size();
    blocks.add(new MapBlock());

    int rehashInd = ind & (blocks.getCapacity() / 2 - 1);
    rehash(rehashInd);
  }

  private int hash(long value) {
    int capacity = blocks.getCapacity();
    int m = ((int) value) & (capacity - 1);
    return m < blocks.size() ? m : (m & capacity / 2 - 1);
  }

  private void rehash(int blockInd) {
    MapBlock block = blocks.get(blockInd);
    for (int i = 0; i < block.keys.size();) {
      long key = block.keys.get(i);
      int newHash = hash(key);
      if (newHash != blockInd) {
        blocks.get(newHash).add(key, block.values.get(i));
        block.removeByInd(i);
      } else {
        i++;
      }
    }
  }

  @Override
  public long remove(long key) {
    long value =  blocks.get(hash(key)).remove(key);
    if (size < blocks.size() / 2 && size > 0) {
      removeBlock();
    }
    return value;
  }

  private void removeBlock() {
    blocks.removeLast();
    rehash(blocks.size());
  }

  private class MapBlock {
    LongArrayList keys = new LongArrayList(1);
    LongArrayList values = new LongArrayList(1);

    long find(long key) {
      for (int i = 0; i < keys.size(); i++) {
        if (keys.get(i) == key) {
          return values.get(i);
        }
      }
      return NULL_VALUE;
    }

    long put(long key, long value) {
      for (int i = 0; i < keys.size(); i++) {
        if (keys.get(i) == key) {
          long oldValue = values.get(i);
          values.set(i, value);
          return oldValue;
        }
      }
      add(key, value);
      return NULL_VALUE;
    }

    void add(long key, long value) {
      size++;
      keys.add(key);
      values.add(value);
    }

    long remove(long key) {
      for (int i = 0; i < keys.size(); i++) {
        if (keys.get(i) == key) {
          long oldValue = values.get(i);
          removeByInd(i);
          return oldValue;
        }
      }
      return NULL_VALUE;
    }

    void removeByInd(int ind) {
      size--;
      keys.removeFast(ind);
      values.removeFast(ind);
    }
  }
}
