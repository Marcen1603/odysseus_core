/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
