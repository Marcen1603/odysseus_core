/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.context.store.types;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.context.store.types.AbstractContextStore;
import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.sweeparea.PartitionedSweepArea;

/**
 * @author msalous
 *
 */


/**
 * This class is used to compare between tuples using their identifiers     
 * */
class TupleComperator<M extends Tuple<? extends IStreamObject<IMetaAttribute>>> implements Comparator<M>{

	private int identifierIndex = 0;
	
	public TupleComperator(int identifierIndex){
		this.identifierIndex = identifierIndex;
	}
	
	@Override
	public int compare(M o1, M o2) {
		if(o1.getAttribute(identifierIndex).toString().compareTo(o2.getAttribute(identifierIndex).toString()) == 0)
			return 0;
		return 1;
	}
	
}

/**
 * This class represents a history store for incoming observations from sensors.
 * It stores them as partitions, each partition represent a history of a sensor.  
 */
public class PartitionedMultiElementStore<T extends Tuple<? extends ITimeInterval>> extends AbstractContextStore<T> {
	
	private PartitionedSweepArea<T> sweepArea;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PartitionedMultiElementStore(String name, SDFSchema schema, int size, int partitionBy) {
		super(name, schema, size);
		TupleComperator<Tuple<? extends IStreamObject<IMetaAttribute>>> comparator = new TupleComperator<>(partitionBy);
		this.sweepArea = new PartitionedSweepArea(comparator, size);
	}
	
	@Override
	public void insertValue(T value) {
		if (validateSchemaSizeOfValue(value)) {
			this.sweepArea.insert(value);
			notifyListener();
        }
        else {
            logger.warn("Context store failure: sizes of value and schema do not match");
        }
	}

	@Override
	public List<T> getValues(ITimeInterval timeinterval) {
		List<T> list = new ArrayList<T>();		
		Iterator<T> iter = this.sweepArea.queryOverlaps(timeinterval);
		while (iter.hasNext()) {					
			list.add(iter.next());
		}
		return list;
	}

	@Override
	public List<T> getLastValues() {
		List<T> list = new ArrayList<T>();		
		Iterator<T> iter = this.sweepArea.getLastValuesIterator();
		while (iter.hasNext()) {					
			list.add(iter.next());
		}
		return list;
	}

	@Override
	public List<T> getAllValues() {
		List<T> list = new ArrayList<T>();
		Iterator<T> iter = this.sweepArea.iterator();
		while (iter.hasNext()) {
			list.add(iter.next());
		}
		return list;
	}

	@Override
	protected void internalClear() {
		this.sweepArea.clear();
	}
	
	@Override
	public void processTime(PointInTime time) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public Iterator<T> query(T element, Order order){
		return this.sweepArea.query(element, order);
	}

	@Override
	public Iterator<T> queryCopy(T element, Order order, boolean extract){
		return this.sweepArea.queryCopy(element, order, extract);
	}
	
	@Override
	public void setQueryPredicate(IPredicate<? super T> queryPredicate){
		this.sweepArea.setQueryPredicate(queryPredicate);
	}
}
