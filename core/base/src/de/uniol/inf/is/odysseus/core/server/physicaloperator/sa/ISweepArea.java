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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.sa;

import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

public interface ISweepArea<T> extends Iterable<T> {

	public void init();

	public void insert(T element);

	public void insertAll(List<T> toBeInserted);

	public void purgeElements(T element, Order order);

	public void clear();

	/**
	 * 
	 * @param element
	 *            The new element from an input stream
	 * @param order
	 *            LeftRight, if element is from the left input stream RightLeft,
	 *            if element is from the right input stream
	 * @return
	 */
	public Iterator<T> query(T element, Order order);

	/**
	 * same as above, but you are guaranteed to iterate over your own copy of
	 * the query results
	 */
	public Iterator<T> queryCopy(T element, Order order);

	public Iterator<T> extractElements(T element, Order order);

	public T peek();

	public T poll();

	public boolean remove(T element);

	public void removeAll(List<T> toBeRemoved);

	public boolean isEmpty();

	public int size();

	public IPredicate<? super T> getQueryPredicate();

	public void setQueryPredicate(IPredicate<? super T> updatePredicate);

	public void setRemovePredicate(IPredicate<? super T> removePredicate);

	public ISweepArea<T> clone();
}
