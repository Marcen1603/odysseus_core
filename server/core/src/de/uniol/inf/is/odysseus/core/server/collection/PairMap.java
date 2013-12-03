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
package de.uniol.inf.is.odysseus.core.server.collection;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.collection.FESortedClonablePair;
import de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class PairMap<K1 extends IClone,K2 extends IClone,V extends IClone,M extends IMetaAttribute> extends AbstractStreamObject<M>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 719541540005684180L;
	private Map<FESortedClonablePair<K1,K2>,V> content = null;
	
	public PairMap() {
		content = new HashMap<FESortedClonablePair<K1,K2>, V>();
	}
	
	@SuppressWarnings("unchecked")
	public PairMap(PairMap<K1, K2, V, M> name, boolean deepClone) {
		if (deepClone){
			content = new HashMap<FESortedClonablePair<K1,K2>, V>();
			for (Entry<FESortedClonablePair<K1,K2>,V> entry:name.entrySet()){
				content.put(entry.getKey().clone(), (V) entry.getValue().clone());
			}
		}else{
			content = new HashMap<FESortedClonablePair<K1,K2>, V>(name.content);
		}
	}

	public void put(K1 k1, K2 k2, V value){
		FESortedClonablePair<K1, K2> p = new FESortedClonablePair<K1, K2>(k1,k2);
		put(p, value);
	}
	
	public void put(FESortedClonablePair<K1, K2> p, V value){
		content.put(p, value);
	}
	
	public V get(K1 k1, K2 k2){
		FESortedClonablePair<K1, K2> p = new FESortedClonablePair<K1,K2>(k1,k2);
		return get(p);
	}
	
	public V get(FESortedClonablePair<K1, K2> p){
		return content.get(p);
	}
	
	public Set<Entry<FESortedClonablePair<K1, K2>, V>> entrySet(){
		return content.entrySet();
	}
	
	@Override
	public String toString() {
		return ""+content;
	}
	
	@Override
	public PairMap<K1,K2,V,M> clone() {
		return new PairMap<K1, K2, V, M>(this, false);
	}

	
}