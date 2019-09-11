package nedikov.compactMap;

@SuppressWarnings("WeakerAccess")
public class LongArrayList {
  private long[] data;
  private int size;
  private int capacity;

  public LongArrayList(int capacity) {
    data = new long[capacity];
    this.capacity = capacity;
  }

  public long get(int index) {
    return data[index];
  }

  public void set(int index, long value) {
    data[index] = value;
  }

  public int size() {
    return size;
  }

  public int getCapacity() {
    return capacity;
  }

  public void add(long element) {
    if (capacity == size) {
      resize(2 * capacity);
    }
    data[size++] = element;
  }

  private void resize(int capacity) {
    long[] newData = new long[capacity];
    System.arraycopy(data, 0, newData, 0, size);
    data = newData;
    this.capacity = capacity;
  }

  public void removeFast(int ind) {
    data[ind] = data[--size];
    if (size < capacity / 4 && size > 0) {
      resize(capacity / 2);
    }
  }
}
