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
/** Copyright 2012 The Odysseus Team
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

package de.uniol.inf.is.odysseus.context.store;

import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.context.IContextStoreListener;
import de.uniol.inf.is.odysseus.context.physicaloperator.StorePO;
import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * 
 * @author Dennis Geesen
 * Created at: 22.03.2012
 */
public interface IContextStore<T extends Tuple<? extends ITimeInterval>>{

	public String getName();
	public SDFSchema getSchema();
	public void insertValue(T value);	
	public List<T> getValues(ITimeInterval timeinterval);
	public List<T> getLastValues();	
	public List<T> getAllValues();	
	public void processTime(PointInTime time);
	
	public void addListener(IContextStoreListener listener);	
	public void removeListener(IContextStoreListener listener);	
	
	public void setWriter(StorePO<T> storePO);
	public boolean hasWriter();
	public void removeWriter();
	public StorePO<T> getWriter();
	public void close();
	public void open();
	public int getSize();
	
	/**
	 * This method is used to iterate over elements in the context store. 
	 * The element will be used together with the queryPredicate
	 * 
	 * @param element
	 *            The new element from an input stream
	 * @param order
	 *            LeftRight, if element is from the left input stream RightLeft,
	 *            if element is from the right input stream
	 * @return
	 * @throws Exception 
	 */
	public Iterator<T> query(T element, Order order) throws Exception;

	/**
	 * same as above, but you are guaranteed to iterate over your own copy of
	 * the query results
	 * 
	 * @param extract
	 *            if set to true, elements will be remove from sweep area
	 * @throws Exception 
	 */
	public Iterator<T> queryCopy(T element, Order order, boolean extract) throws Exception;
	
	/**
	 * This predicate is used to retrieve elements from the sweep area
	 * 
	 * @param queryPredicate
	 */
	public void setQueryPredicate(IPredicate<? super T> queryPredicate) throws Exception;
}
