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
package de.uniol.inf.is.odysseus.mining.frequentitem.fpgrowth;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

/**
 * @author Dennis Geesen
 *
 */
public class FListEntry<M extends IMetaAttribute> implements Comparable<FListEntry<M>> {

	private int count = 0;
	private Tuple<M> tuple;
	
	public FListEntry(Tuple<M> tuple){
		this.tuple = tuple;
	}
	
	public void increaseCount(){
		count++;
	}
	
	public void decreaseCount(){
		count--;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof FListEntry){
			return this.tuple.equals(((FListEntry<?>)obj).tuple);
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {	
		return this.tuple.hashCode();
	}
	
	@Override
	public int compareTo(FListEntry<M> o) {
		if(o.count>this.count){
			return -1;
		}
		if(o.count<this.count){
			return 1;
		}
		return 0;
	}

}
