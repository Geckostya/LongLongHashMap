package nedikov.compactMap;

import java.util.Arrays;

@SuppressWarnings("WeakerAccess")
public class FastArrayList<T> {
  private T[] data;
  private int size;
  private int capacity;

  public FastArrayList(T[] initData) {
    data = initData;
    capacity = initData.length;
  }

  public T get(int index) {
    return data[index];
  }

  public int size() {
    return size;
  }

  public int getCapacity() {
    return capacity;
  }

  public void add(T element) {
    if (capacity == size) {
      resize(2 * capacity);
    }
    data[size++] = element;
  }

  private void resize(int capacity) {
    data = Arrays.copyOf(data, capacity);
    this.capacity = capacity;
  }

  public void removeLast() {
    size--;
    if (size < capacity / 4 && size > 0) {
      resize(capacity / 2);
    }
  }
}