import java.util.HashMap;

public class LfuCache<K, V> {

  private LinkedDeque<K> lfuCache;
  private final int maxSize;

  private HashMap<K, V> cache;
  private int size;

  public LfuCache(int maxSize) {
    this.maxSize = maxSize;
    this.cache = new HashMap<>();
    lfuCache = new LinkedDeque<>();
  }

  public V get(K key) {
    lfuCache.updateGet(key);
    return cache.get(key);
  }

  public void put(K key, V val) {
    discard();
    cache.put(key, val);
    if (lfuCache.updatePut(key)) {
      size += 1;
    }
  }

  private void discard() {
    if (size == maxSize) {
      K key = lfuCache.popRight();
      cache.remove(key);
      size -= 1;
    }
  }

}
