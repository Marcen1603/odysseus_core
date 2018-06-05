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

import java.util.LinkedList;
import java.util.List;

import com.xafero.kitea.collections.api.ModificationEvent;
import com.xafero.kitea.collections.api.ModificationListener;

/**
 * The observable container is the base class with listeners attached.
 *
 * @param <T>
 *            the generic type
 */
public abstract class ObservableContainer<T> {

	/** The listeners. */
	protected final List<ModificationListener<T>> listeners = new LinkedList<ModificationListener<T>>();

	/**
	 * Fire modification listeners.
	 *
	 * @param event
	 *            the event
	 */
	protected synchronized void fireModificationListeners(ModificationEvent<T> event) {
		for (ModificationListener<T> listener : listeners)
			listener.onModification(event);
	}

	/**
	 * Adds the modification listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public synchronized void addModificationListener(ModificationListener<T> listener) {
		listeners.add(listener);
	}

	/**
	 * Gets the modification listeners.
	 *
	 * @return the modification listeners
	 */
	@SuppressWarnings("unchecked")
	public synchronized ModificationListener<T>[] getModificationListeners() {
		return listeners.toArray(new ModificationListener[listeners.size()]);
	}

	/**
	 * Removes the modification listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public synchronized void removeModificationListener(ModificationListener<T> listener) {
		listeners.remove(listener);
	}
}