import java.util.HashMap;

public class FifoCache<K, V> {

  private LinkedDeque<K> stack;
  private final int maxSize;

  private HashMap<K, V> cache;
  private int size;

  public FifoCache(int maxSize) {
    this.maxSize = maxSize;
    this.cache = new HashMap<>();
    stack = new LinkedDeque<>();
  }

  public void put(K key, V value) {
    discard();
    cache.put(key, value);
    stack.addAtTail(key);
    size += 1;
  }

  private void discard() {
    if (maxSize == size) {
      // 满了 要执行丢弃策略 弹出栈底元素
      K key = stack.popLeft();
      cache.remove(key);
      this.size -= 1;
    }
  }

  public V get(K key) {
    return cache.get(key);
  }

}
