

public class LinkedDeque<T> {

  private Node<T> head;
  private Node<T> tail;
  private int size;

  private static class Node<T> {

    private T val;
    private Node<T> next;
    private Node<T> prev;
    private int freq;

    public Node(T val) {
      this.val = val;
      this.next = null;
      this.prev = null;
      this.freq = 1;
    }

    public Node(T val, Node<T> next, Node<T> prev) {
      this.val = val;
      this.next = next;
      this.prev = prev;
      this.freq = 1;
    }
  }

  public LinkedDeque(T val) {
    Node<T> node = new Node<>(val);
    this.head = node;
    this.tail = node;
    this.size = 1;
  }

  public LinkedDeque() {
    this.head = null;
    this.tail = null;
    this.size = 0;
  }

  private void addFirst(T val, int freq) {
    if (this.size == 0) {
      Node<T> node = new Node<>(val);
      node.freq = freq;
      this.head = node;
      this.tail = node;
      this.size = 1;
    }
  }

  private void addFirst(T val) {
    addFirst(val, 1);
  }

  public void addAtHead(T val, int freq) {
    if (this.size == 0) {
      this.addFirst(val, freq);
    } else {
      Node<T> node = new Node<>(val);
      node.freq = 1;
      node.next = this.head;
      this.head.prev = node;
      this.head = node;
      this.size += 1;
    }
  }

  public void addAtHead(T val) {
    addAtHead(val, 1);
  }

  public void addAtTail(T val, int freq) {
    if (this.size == 0) {
      this.addFirst(val, freq);
    } else {
      Node<T> node = new Node<>(val);
      node.freq = freq;
      node.prev = this.tail;
      this.tail.next = node;
      this.tail = node;
      this.size += 1;
    }
  }

  public void addAtTail(T val) {
    addAtTail(val, 1);
  }

  private T popLast() {
    if (this.size == 1) {
      T res = this.head.val;
      this.head = null;
      this.tail = null;
      this.size = 0;
      return res;
    } else {
      throw new RuntimeException("这个错误不会发生的，相信我");
    }
  }

  public T pop() {
    return popRight();
  }

  public T popRight() {
    if (this.size == 1) {
      return this.popLast();
    } else {
      T res = this.tail.val;
      this.tail = this.tail.prev;
      this.tail.next = null;
      this.size -= 1;
      return res;
    }
  }

  public T popLeft() {
    if (this.size == 1) {
      return this.popLast();
    } else {
      T res = this.head.val;
      this.head = this.head.next;
      this.head.prev = null;
      this.size -= 1;
      return res;
    }
  }

  @Deprecated
  public T get(int index) {
    if (index >= this.size) {
      throw new IndexOutOfBoundsException("超出长度");
    } else {
      Node<T> curr = this.head;
      for (int i = 0; i < index; i++) {
        curr = curr.next;
      }
      return curr.val;
    }
  }

  private Node<T> getNode(T val) {
    Node<T> curr = head;
    if (curr != null) {
      do {
        if (curr.val == val) {
          return curr;
        }
        curr = curr.next;
      } while (curr != null);
    }
    return null;
  }

  public T fastGet(int index) {
    return fastGetNode(index).val;
  }

  private Node<T> fastGetNode(int index) {
    if (index >= this.size) {
      throw new IndexOutOfBoundsException("超出长度");
    } else {
      if (index <= this.size / 2) {
        // 正向找
        Node<T> curr = this.head;
        for (int i = 0; i < index; i++) {
          curr = curr.next;
        }
        return curr;
      } else {
        // 逆向找
        Node<T> curr = this.tail;
        for (int i = 0, loop = this.size - index - 1; i < loop; i++) {
          curr = curr.prev;
        }
        return curr;
      }
    }
  }

  public void set(int index, T val, int freq) {
    if (index >= this.size) {
      throw new IndexOutOfBoundsException("超出长度");
    } else {
      Node<T> tNode = fastGetNode(index);
      tNode.val = val;
      tNode.freq = freq;
    }
  }

  public void set(int index, T val) {
    set(index, val, 1);
  }

  public void insert(int index, T val, int freq) {
    if (index > this.size) {
      throw new IndexOutOfBoundsException("超出长度");
    } else if (index == 0) {
      addAtHead(val, freq);
    } else if (index == this.size) {
      addAtTail(val, freq);
    } else {
      Node<T> next = fastGetNode(index);
      Node<T> prev = next.prev;
      Node<T> node = new Node<>(val);
      node.freq = freq;
      prev.next = node;
      next.prev = node;
      node.prev = prev;
      node.next = next;
      this.size += 1;
    }
  }

  public void insert(int index, T val) {
    insert(index, val, 1);
  }

  public boolean contain(T val) {
    boolean res = false;
    Node<T> curr = this.head;
    while (curr != null && curr.next != null) {
      if (res = curr.val == val) {
        return res;
      }
      curr = curr.next;
    }
    return res;
  }

  public int findAndRemove(T val) {
    int res = 0;
    Node<T> curr = this.head;
    while (curr != null && curr.next != null) {
      if (curr.val == val) {
        // 删除curr
        Node<T> prev = curr.prev;
        Node<T> next = curr.next;
        prev.next = next;
        next.prev = prev;
        this.size -= 1;
        res = curr.freq;
        break;
      }
      curr = curr.next;
      if (curr == tail && curr.val == val) {
        res = curr.freq;
        popRight();
        break;
      }
    }
    return res;
  }

  public void updateGet(T val) {
    Node<T> node = getNode(val);
    if (node != null) {
      node.freq += 1;
      bubble(node);
    }
  }

  /**
   *
   * @param val
   * @return if the first add return true
   */
  public boolean updatePut(T val) {
    Node<T> node = getNode(val);
    if (node != null) {
      node.freq += 1;
      bubble(node);
      return false;
    } else {
      addAtTail(val, 1);
      bubble(this.tail);
      return true;
    }
  }

  private void bubble(Node<T> curr) {
    while (curr != head && curr.freq >= curr.prev.freq) {
      Node<T> prev = curr.prev.prev;
      Node<T> next = curr.next;
      Node<T> node = curr.prev;  // 待交换节点
      curr.next = node;  // 交换节点
      curr.prev = prev;
      node.prev = curr;
      node.next = next;
      if (prev == null) {
        // 说明待交换的节点是头结点
       next.prev = node;
       this.head = curr;
      } else if (next == null) {
        // 说明当前节点是尾节点
        prev.next = curr;
        this.tail = node;
      } else {
        prev.next = curr;
        next.prev = node;
      }
    }
  }
}
