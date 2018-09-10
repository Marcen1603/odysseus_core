/*******************************************************************************
 * Copyright 2016 Georg Berendt
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
package com.xafero.kitea.collections.impl;

import java.util.Iterator;

import com.xafero.kitea.collections.api.ModificationEvent;
import com.xafero.kitea.collections.api.ModificationKind;

/**
 * An observable iterable with fires on modification.
 *
 * @param <T>
 *            the generic type
 */
public class ObservableIterable<T> extends ObservableContainer<T> implements Iterable<T> {

	/** The iterable. */
	protected final Iterable<T> iterable;

	/**
	 * Instantiates a new observable iterable.
	 *
	 * @param iterable
	 *            the iterable
	 */
	public ObservableIterable(Iterable<T> iterable) {
		this.iterable = iterable;
	}

	@Override
	public Iterator<T> iterator() {
		return new ObservableIterator(iterable.iterator());
	}

	/**
	 * The Class ObservableIterator.
	 */
	private class ObservableIterator implements Iterator<T> {

		/** The iterator. */
		private final Iterator<T> iterator;

		/** The last. */
		private T last;

		/**
		 * Instantiates a new observable iterator.
		 *
		 * @param iterator
		 *            the iterator
		 */
		private ObservableIterator(Iterator<T> iterator) {
			this.iterator = iterator;
		}

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public T next() {
			return last = iterator.next();
		}

		@Override
		public void remove() {
			iterator.remove();
			fireModificationListeners(
					(new ModificationEvent<T>(ObservableIterable.this)).kind(ModificationKind.Remove).item(last));
		}
	}

	/**
	 * Wrap.
	 *
	 * @param <E>
	 *            the element type
	 * @param iterable
	 *            the iterable
	 * @return the observable iterable
	 */
	public static <E> ObservableIterable<E> wrap(Iterable<E> iterable) {
		return new ObservableIterable<E>(iterable);
	}
}