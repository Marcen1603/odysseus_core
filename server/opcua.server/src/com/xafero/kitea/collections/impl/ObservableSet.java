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

import java.util.Set;

/**
 * An observable set which fires on modification.
 *
 * @param <T>
 *            the generic type
 */
public class ObservableSet<T> extends ObservableCollection<T> implements Set<T> {

	/**
	 * Instantiates a new observable set.
	 *
	 * @param set
	 *            the set
	 */
	public ObservableSet(Set<T> set) {
		super(set);
	}

	/**
	 * Wrap.
	 *
	 * @param <E>
	 *            the element type
	 * @param set
	 *            the set
	 * @return the observable set
	 */
	public static <E> ObservableSet<E> wrap(Set<E> set) {
		return new ObservableSet<E>(set);
	}
}