/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.priority_interval;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.intervalapproach.IDummyDataCreationFunction;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class PriorityDataCreationFunction<K extends IMetaAttribute,T extends IMetaAttributeContainer<K>> implements IDummyDataCreationFunction<K,T>{

	Class<K> type;
	
	public PriorityDataCreationFunction(Class<K> type) {
		this.type = type;
	}
	
	@Override
	public T createMetadata(T source) {
		try {
			source.setMetadata(type.newInstance());
			((IPriority)source.getMetadata()).setPriority((byte) 0);
			return source;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean hasMetadata(T source) {
		if(((IPriority)source.getMetadata()).getPriority() > 0) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public PriorityDataCreationFunction<K,T> clone() {
		return new PriorityDataCreationFunction<K, T>(type);
	}

}
