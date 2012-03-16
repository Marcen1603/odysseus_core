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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

public abstract class AbstractFrequentPO<T extends Tuple<?>> extends AbstractPipe<T,T>{

	private double size = 0;
	private int[] onAttributes;
	
	public AbstractFrequentPO(int[] onAttributes, double size){
		this.size = size;
		this.onAttributes = onAttributes;
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp, port);		
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
	
	public double getSize(){
		return this.size;
	}
	
	
	public int[] getOnAttributes(){
		return this.onAttributes;
	}
	
	public boolean isEqualOnAttributes(T left, T right){
		Tuple<?> leftTuple = left.restrict(onAttributes, true);
		Tuple<?> rightTuple = right.restrict(onAttributes, true);
		return leftTuple.equals(rightTuple);		
	}
	
	@SuppressWarnings("unchecked")
	protected List<T> listtoobjects(Map<Tuple<?>, Integer> countedItems) {
		List<T> list = new ArrayList<T>();
		for(Entry<Tuple<?>, Integer> e : countedItems.entrySet()){
			Tuple<?> newOne = e.getKey().append(e.getValue());
			list.add((T)newOne);
		}		
		return list;
	}
	
	// Object T and its counter as an Integer
	protected HashMap<Tuple<?>, Integer> items = new HashMap<Tuple<?>, Integer>();
	
	

}
