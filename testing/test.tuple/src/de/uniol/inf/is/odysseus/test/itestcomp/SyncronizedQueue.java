package de.uniol.inf.is.odysseus.test.itestcomp;


class SynchronizedQueue<V> {

  private Object[] elements;

  private int head;

  private int tail;

  private int size;

  public SynchronizedQueue(int capacity) {
    elements = new Object[capacity];
    head = 0;
    tail = 0;
    size = 0;
  }

  public synchronized V remove() throws InterruptedException {
    while (size == 0)
      wait();
    @SuppressWarnings("unchecked")
	V r = (V) elements[head];
    head++;
    size--;
    if (head == elements.length)
      head = 0;
    notifyAll();
    return r;
  }

  public synchronized void add(V newValue) throws InterruptedException {
    while (size == elements.length)
      wait();
    elements[tail] = newValue;
    tail++;
    size++;
    if (tail == elements.length)
      tail = 0;
    notifyAll();
  }

}