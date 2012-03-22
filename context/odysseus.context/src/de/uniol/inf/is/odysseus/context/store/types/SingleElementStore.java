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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

/**
 * 
 * @author Dennis Geesen
 * Created at: 06.02.2012
 */
public class SingleElementStore<T extends Tuple<? extends ITimeInterval>> extends AbstractContextStore<T>{

	
	private T value;
	
	
	public SingleElementStore(SDFSchema schema) {
		super(schema);		
	}
	
	@Override
	public void insertValue(T value) {
		this.value = value;
	}


	@Override
	public List<T> getValues(ITimeInterval timeinterval) {
		return getLastValues();
	}


	@Override
	public List<T> getLastValues() {
		List<T> list = new ArrayList<T>();
		list.add(value);
		return list;
	}
	
}
