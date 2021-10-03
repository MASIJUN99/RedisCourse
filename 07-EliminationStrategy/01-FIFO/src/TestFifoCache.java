public class TestFifoCache {

  public static void main(String[] args) {
    FifoCache<Integer, Integer> fifoCache = new FifoCache<>(2);

    fifoCache.put(1,1);
    fifoCache.put(2,2);
    fifoCache.put(3,3);
    fifoCache.put(4,4);

    System.out.println(fifoCache.get(1));
    System.out.println(fifoCache.get(2));
    System.out.println(fifoCache.get(3));
    System.out.println(fifoCache.get(4));

    fifoCache.put(5,5);
    System.out.println(fifoCache.get(3));
    System.out.println(fifoCache.get(4));
    System.out.println(fifoCache.get(5));
  }

}
