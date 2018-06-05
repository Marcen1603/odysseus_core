/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.sweeparea;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

public interface ISweepArea<T> extends Iterable<T>, Serializable {

	/**
	 * Initializes this sweep area. All elements are removed
	 */
	void init();

	/**
	 * Remove all elements from sweep area
	 */
	void clear();

	/**
	 * Returns true if the sweep area contains no elements
	 * @return
	 */
	boolean isEmpty();
	
	/**
	 * Return the current count of elements stored in the sweep area
	 * @return
	 */
	int size();

	/**
	 * Insert a new element to the sweep area
	 * 
	 * @param element
	 */
	void insert(T element);

	/**
	 * Insert a list to elements to the sweep area
	 * 
	 * @param toBeInserted
	 */
	void insertAll(List<T> toBeInserted);

	/**
	 * Removes the element given from the sweep area
	 * TODO: What if element is contained multiple times?
	 * @param element
	 * @return
	 */
	boolean remove(T element);

	/**
	 * Removes all elements given in the toBeRemoved list from the sweep area
	 * TODO: What if element is contained multiple times?
	 * @param toBeRemoved
	 */
	void removeAll(List<T> toBeRemoved);

	
	/**
	 * Take a look at the top element of the sweep area. 
	 * @return
	 */
	T peek();

	/**
	 * Remove the top element from the sweep area
	 * @return
	 */
	T poll();

	
	/**
	 * This method is used to iterate over elements in the sweep area the
	 * element will be used together with the queryPredicate
	 * 
	 * @param element
	 *            The new element from an input stream
	 * @param order
	 *            LeftRight, if element is from the left input stream RightLeft,
	 *            if element is from the right input stream
	 * @return
	 */
	Iterator<T> query(T element, Order order);

	/**
	 * same as above, but you are guaranteed to iterate over your own copy of
	 * the query results
	 * 
	 * @param extract
	 *            if set to true, elements will be remove from sweep area
	 */
	Iterator<T> queryCopy(T element, Order order, boolean extract);

	/**
	 * Same as query but removes elements from the sweep area
	 * 
	 * @param element
	 * @param order
	 * @return
	 */
	Iterator<T> extractElements(T element, Order order);

	/**
	 * This predicate is used to retrieve elements from the sweep area
	 * 
	 * @param queryPredicate
	 */
	void setQueryPredicate(IPredicate<? super T> queryPredicate);

	/**
	 * Returns the current set query predicate
	 * @return
	 */
	IPredicate<? super T> getQueryPredicate();

	/**
	 * Remove elements from the sweep area depending on the element and the
	 * remove predicate
	 * 
	 * @param element
	 *            The element for which the predicate should be evaluated
	 * @param order
	 *            The predicate has two inputs, one for the element and one for
	 *            input from the sweep area. With order you can state if element
	 *            should be the left or the right input of the predicate
	 */
	void purgeElements(T element, Order order);

	/**
	 * This predicate is used to remove elements from the sweep area when purge
	 * elements is called
	 * 
	 * @param removePredicate
	 */
	void setRemovePredicate(IPredicate<? super T> removePredicate);

	IPredicate<? super T> getRemovePredicate();
	
	/**
	 * Remove all elements of this sweep area and deliver an iterator
	 * @return
	 */
	Iterator<T> extractAllElements();

	/**
	 * Remove all elements of this sweep area and deliver a list
	 * @return
	 */
	List<T> extractAllElementsAsList();
	
	/**
	 * Create an identical copy of this area (no deep clone!) Elements are not cloned!
	 * @return
	 */
	ISweepArea<T> clone();

	String getName();
	
	ISweepArea<T> newInstance(OptionMap options);

	/**
	 * For debugging: 
	 * @param name
	 */
	void setAreaName(String name);
	String getAreaName();
}
