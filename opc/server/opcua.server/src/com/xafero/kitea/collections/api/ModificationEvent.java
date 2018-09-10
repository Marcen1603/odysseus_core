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
package com.xafero.kitea.collections.api;

import java.util.EventObject;

/**
 * The modification event of a collection.
 *
 * @param <T>
 *            the generic type
 */
public final class ModificationEvent<T> extends EventObject {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6005957767623784061L;

	/** The item. */
	private T item;

	/** The kind. */
	private ModificationKind kind;

	/**
	 * Instantiates a new modification event.
	 *
	 * @param source
	 *            the source
	 */
	public ModificationEvent(Object source) {
		super(source);
	}

	/**
	 * Gets the item.
	 *
	 * @return the item
	 */
	public T getItem() {
		return item;
	}

	/**
	 * Gets the kind.
	 *
	 * @return the kind
	 */
	public ModificationKind getKind() {
		return kind;
	}

	/**
	 * Sets the item.
	 *
	 * @param item
	 *            the item
	 * @return the modification event
	 */
	public ModificationEvent<T> item(T item) {
		this.item = item;
		return this;
	}

	/**
	 * Sets the kind.
	 *
	 * @param kind
	 *            the kind
	 * @return the modification event
	 */
	public ModificationEvent<T> kind(ModificationKind kind) {
		this.kind = kind;
		return this;
	}

	@Override
	public String toString() {
		return "ModificationEvent [item=" + item + ", kind=" + kind + "]";
	}
}