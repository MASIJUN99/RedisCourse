public class TestLfuCache {

  public static void main(String[] args) {
    LfuCache<Integer, Integer> lfuCache = new LfuCache<>(3);

    lfuCache.put(1,1);
    System.out.println(lfuCache.get(1));
    lfuCache.put(1,11);
    lfuCache.put(2,2);
    lfuCache.put(3,3);
    System.out.println(lfuCache.get(1));
    lfuCache.put(4,4);

    System.out.println(lfuCache.get(4));
    System.out.println(lfuCache.get(3));
    System.out.println(lfuCache.get(2));
    System.out.println(lfuCache.get(1));
  }

}
