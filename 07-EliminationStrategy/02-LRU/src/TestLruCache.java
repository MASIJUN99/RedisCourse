public class TestLruCache {

  public static void main(String[] args) {
    LruCache<Integer, Integer> lruCache = new LruCache<>(3);

    lruCache.put(1, 1);
    lruCache.put(2, 2);
    lruCache.put(3, 3);
    lruCache.get(1);
    lruCache.put(4, 4);
    lruCache.put(3, 33);

    System.out.println(lruCache.get(1));
    System.out.println(lruCache.get(2));
    System.out.println(lruCache.get(3));
    System.out.println(lruCache.get(4));
  }

}
