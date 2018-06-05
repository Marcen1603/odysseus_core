package de.uniol.inf.is.odysseus.core.collection;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;

@SuppressWarnings("unchecked")
public class IdentityArrayList<E> extends AbstractList<E> implements RandomAccess {

	private transient Object[] content;
	private int currentSize;

	public IdentityArrayList() {
		this(10);
	}

	public IdentityArrayList(int initialCapacity) {
		super();
		assert(initialCapacity >= 0);
		this.content = new Object[initialCapacity];
	}

	public IdentityArrayList(Collection<E> other) {
		content = other.toArray();
		currentSize = content.length;
		// toArray might (incorrectly) not return Object[] (see 6260652)
		if (content.getClass() != Object[].class) {
			content = Arrays.copyOf(content, currentSize, Object[].class);
		}
	}

	@Override
	public boolean contains(Object o) {
		for (int i = 0; i < currentSize ; i++) {
			if (o == content[i]) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o:c){
			if (!contains(o)){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean add(E e) {
		ensureCapacity(currentSize + 1);
		content[currentSize++] = e;
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		Object[] a = c.toArray();
		int numNew = a.length;
		ensureCapacity(currentSize + numNew);
		System.arraycopy(a, 0, content, currentSize, numNew);
		currentSize += numNew;
		return numNew != 0;
	}

	@Override
	public E set(int index, E element) {
		rangeCheck(index);

		E retValue = (E) content[index];
		content[index] = element;

		return retValue;
	}

	@Override
	public E get(int index) {
		rangeCheck(index);

		return (E) content[index];
	}

	@Override
	public E remove(int index) {
		rangeCheck(index);
		return uncheckedRemove(index);
	}

	@Override
	public boolean remove(Object o) {
		for (int i = 0; i < currentSize; i++) {
			if (content[i] == o) {
				uncheckedRemove(i);
				return true;
			}
		}
		return false;
	}

	private E uncheckedRemove(int index) {
		E valueToRemove = (E) content[index];
		int toMove = currentSize - index - 1;
		if (toMove > 0) {
			System.arraycopy(content, index + 1, content, index, toMove);
		}
		// Clear now free last entry
		content[--currentSize] = null;
		return valueToRemove;
	}

	@Override
	public void clear() {
		for (int i = 0; i < currentSize; i++) {
			content[i] = null;
		}
		currentSize = 0;
	}

	@Override
	public int indexOf(Object o) {
		for (int i = 0; i < currentSize - 1; i++) {
			if (o == content[i]) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public int size() {
		return currentSize;
	}
	
	@Override
	public Object[] toArray() {
		return Arrays.copyOf(content, currentSize);
	}

	@Override
	public boolean isEmpty() {
		return currentSize == 0;
	}
	
	private void ensureCapacity(int requiredCapacity) {
		int curSize = content.length;
		if (requiredCapacity > curSize) {
			int newCap = (curSize * 3) / 2 + 1; // increase 50 %
			if (newCap < requiredCapacity) {
				newCap = curSize;
			}
			content = Arrays.copyOf(content, newCap);
		}

	}

	private void rangeCheck(int index) {
		if (index >= currentSize) {
			throw new IndexOutOfBoundsException(index + ">" + currentSize);
		}
	}
	
}
