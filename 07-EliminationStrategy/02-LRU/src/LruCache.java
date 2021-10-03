import java.util.HashMap;

public class LruCache<K, V> {

  private LinkedDeque<K> lruDeque;  // 这里规定越靠右删除优先级越高
  private final int maxSize;

  private HashMap<K, V> cache;
  private int size;


  public LruCache(int maxSize) {
    this.maxSize = maxSize;
    this.cache = new HashMap<>();
    lruDeque = new LinkedDeque<>();
  }

  public void put(K key, V val) {
    discard();
    cache.put(key, val);
    lruDeque.addAtHead(key, lruDeque.findAndRemove(key));
    size += 1;
  }

  public V get(K key) {
    if (cache.containsKey(key)) {
      lruDeque.addAtHead(key, lruDeque.findAndRemove(key));
      return cache.get(key);
    } else {
      return null;
    }
  }

  private void discard() {
    if (size >= maxSize) {
      K key = lruDeque.popRight();
      cache.remove(key);
      this.size -= 1;
    }
  }
}
