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
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ISweepArea.Order;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

/**
 * A store with no history, but with more than one valid item at the same time (e.g. a list of detected objects that 
 * are currently valid. It is necessary to define an unique key attribute for different objects, so that the store
 * can determine if an inserted element is possibly a new version of an old one. 
 * 
 * @author Dennis Geesen
 * Created at: 06.02.2012
 */
public class MultiElementStore<T extends Tuple<? extends ITimeInterval>> extends AbstractContextStore<T> {
	
	private DefaultTISweepArea<T> sweepArea = new DefaultTISweepArea<T>();
	private List<SDFAttribute> keys;	
		
	
	public MultiElementStore(SDFSchema schema, List<SDFAttribute> keys) {	
		super(schema);
		this.keys = keys;
	}
	

	@Override
	public void insertValue(T value) {		
		sweepArea.purgeElements(value, Order.LeftRight);
		sweepArea.insert(value);		
	}


	@Override
	public List<T> getValues(ITimeInterval timeinterval) {
		return null;
	}


	@Override
	public List<T> getLastValues() {
		List<T> list = new ArrayList<T>();		
		return list;
	}

	
}
