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
package de.uniol.inf.is.odysseus.kdds.frequent.physical;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class LossyCountingFrequentItem<T extends RelationalTuple<?>> extends AbstractFrequentPO<T> {

	private int count = 0;
	private int delta = 0;
	
	public LossyCountingFrequentItem(int[] onAttributes, double size) {
		super(onAttributes, size);		
	}

	public LossyCountingFrequentItem(LossyCountingFrequentItem<?> other) {
		this(other.getOnAttributes(), other.getSize());
		this.count = other.count;
	}

	@Override
	public LossyCountingFrequentItem<T> clone() {
		return new LossyCountingFrequentItem<T>(this);
	}

	
	
	@Override
	protected void process_next(T newObject, int port) {
		count++;
		RelationalTuple<?> tuple = newObject.restrict(getOnAttributes(), true);
		if (this.items.containsKey(tuple)) {
			int oldCount = this.items.get(tuple);
			this.items.put(tuple, oldCount + 1);
		} else {			
			this.items.put(tuple, 1+delta);
		}
			
		if(Math.floor(this.count/getSize())!=this.delta){
			this.delta = (int) Math.floor(this.count/getSize());
			@SuppressWarnings("unchecked")
			Map<T, Integer> walker = (Map<T, Integer>) this.items.clone();

			for (Entry<T, Integer> e : walker.entrySet()) {
				if (e.getValue() < this.delta) {
					this.items.remove(e.getKey());
				} 
			}
		}
		
		List<T> liste = listtoobjects(items);
		transfer(liste);
	}

}
