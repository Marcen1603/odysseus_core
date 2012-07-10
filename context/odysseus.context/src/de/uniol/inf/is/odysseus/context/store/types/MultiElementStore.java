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
/** Copyright 2011 The Odysseus Team
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
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * A store with no history, but with more than one valid item at the same time
 * (e.g. a list of detected objects that are currently valid. It is necessary to
 * define an unique key attribute for different objects, so that the store can
 * determine if an inserted element is possibly a new version of an old one.
 * 
 * @author Dennis Geesen Created at: 06.02.2012
 */
public class MultiElementStore<T extends Tuple<? extends ITimeInterval>> extends AbstractContextStore<T> {

	private DefaultTISweepArea<T> sweepArea = new DefaultTISweepArea<T>();

	public MultiElementStore(String name, SDFSchema schema, int size) {
		super(name, schema, size);		
	}

	@Override
	public void insertValue(T value) {
		if (validateSchemaSizeOfValue(value)) {
			//sweepArea.purgeElements(value, Order.LeftRight);			
			T tail = sweepArea.peekLast();
			if(tail!=null){
				tail.getMetadata().setEnd(value.getMetadata().getStart());
			}
			sweepArea.insert(value);
			if(sweepArea.size()>this.getSize()){
				sweepArea.poll();				
			}
			notifyListener();
		//	System.err.println(sweepArea.getSweepAreaAsString(value.getMetadata().getStart()));
		} else {
			logger.warn("Context store failure: size of value and schema do not match");
		}
	}

	@Override
	public List<T> getValues(ITimeInterval timeinterval) {
		List<T> list = new ArrayList<T>();		
		Iterator<T> iter = sweepArea.queryOverlaps(timeinterval);
		while (iter.hasNext()) {					
			list.add(iter.next());
		}
		return list;
	}

	@Override
	public List<T> getLastValues() {
		List<T> list = new ArrayList<T>();
		return list;
	}

	@Override
	public List<T> getAllValues() {
		List<T> list = new ArrayList<T>();
		Iterator<T> iter = sweepArea.iterator();
		while (iter.hasNext()) {
			list.add(iter.next());
		}
		return list;
	}
	
	@Override
	public void processTime(PointInTime time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void internalClear() {
		this.sweepArea.clear();		
	}

}
