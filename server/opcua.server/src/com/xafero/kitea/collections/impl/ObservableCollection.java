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

import java.util.Collection;

import com.xafero.kitea.collections.api.ModificationEvent;
import com.xafero.kitea.collections.api.ModificationKind;

/**
 * An observable collection which fires events on any modification.
 *
 * @param <T>
 *            the generic type
 */
public class ObservableCollection<T> extends ObservableIterable<T> implements Collection<T> {

	/** The collection. */
	protected final Collection<T> collection;

	/**
	 * Instantiates a new observable collection.
	 *
	 * @param collection
	 *            the collection
	 */
	public ObservableCollection(Collection<T> collection) {
		super(collection);
		this.collection = collection;
	}

	@Override
	public boolean add(T e) {
		boolean added = collection.add(e);
		if (added)
			fireModificationListeners((new ModificationEvent<T>(this)).kind(ModificationKind.Add).item(e));
		return added;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean added = true;
		for (T item : c)
			added = added && add(item);
		return added;
	}

	@Override
	public void clear() {
		@SuppressWarnings("unchecked")
		T[] copy = (T[]) collection.toArray();
		collection.clear();
		for (T item : copy)
			fireModificationListeners((new ModificationEvent<T>(this)).kind(ModificationKind.Remove).item(item));
	}

	@Override
	public boolean contains(Object o) {
		return collection.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return collection.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return collection.isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {
		boolean removed = collection.remove(o);
		if (removed)
			fireModificationListeners((new ModificationEvent<T>(this)).kind(ModificationKind.Remove).item((T) o));
		return removed;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean removed = true;
		for (Object item : c)
			removed = removed && remove(item);
		return removed;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean retainAll(Collection<?> c) {
		Object[] before = collection.toArray();
		boolean retained = collection.retainAll(c);
		if (retained)
			for (Object item : before) {
				if (c.contains(item))
					continue;
				fireModificationListeners(
						(new ModificationEvent<T>(this)).kind(ModificationKind.Remove).item((T) item));
			}
		return retained;
	}

	@Override
	public int size() {
		return collection.size();
	}

	@Override
	public Object[] toArray() {
		return collection.toArray();
	}

	@Override
	public <I> I[] toArray(I[] a) {
		return collection.toArray(a);
	}

	/**
	 * Wrap.
	 *
	 * @param <E>
	 *            the element type
	 * @param collection
	 *            the collection
	 * @return the observable collection
	 */
	public static <E> ObservableCollection<E> wrap(Collection<E> collection) {
		return new ObservableCollection<E>(collection);
	}
}