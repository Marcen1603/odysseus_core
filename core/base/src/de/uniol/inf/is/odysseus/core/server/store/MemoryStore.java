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
package de.uniol.inf.is.odysseus.core.server.store;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class MemoryStore<IDType extends Comparable<?>,STORETYPE> implements IStore<IDType ,STORETYPE>{

	private Map<IDType, STORETYPE> elements = new TreeMap<IDType, STORETYPE>();
	
	@Override
	public STORETYPE get(IDType username) {
		return elements.get(username);
	}

	@Override
	public void put(IDType id, STORETYPE element) {
		elements.put(id, element);
	}
	
	@Override
	public Set<Entry<IDType, STORETYPE>> entrySet() {
		return elements.entrySet();
	}
	
	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	@Override
	public void clear() {
		elements.clear();
	}

	@Override
	public boolean containsKey(IDType key) {
		return elements.containsKey(key);
	}
	
	@Override
	public STORETYPE remove(IDType id) {
		return elements.remove(id);
	}

	@Override
	public Set<IDType> keySet() {
		return elements.keySet();
	}

	@Override
	public Collection<STORETYPE> values() {
		return elements.values();
	}
	
	
	@Override
	public void commit() {
		// Nothing to do in MainMemory
	}


	
}
