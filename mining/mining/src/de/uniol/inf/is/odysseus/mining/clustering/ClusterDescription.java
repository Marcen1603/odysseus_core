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
/** Copyright 2012 The Odysseus Team
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

package de.uniol.inf.is.odysseus.mining.clustering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

/**
 * 
 * @author Dennis Geesen
 * Created at: 15.05.2012
 */
public class ClusterDescription<M extends ITimeInterval> {
		
	private List<Tuple<M>> tuples = new ArrayList<Tuple<M>>();
	private int[] attributes;
	
	public ClusterDescription(int[] attributes){
		this.attributes = attributes;
	}
	
	public void addTuple(Tuple<M> tuple){
		this.tuples.add(tuple);
	}
	
	public boolean removeTuple(Tuple<M> tuple){
		return this.tuples.remove(tuple);
	}
	
	public Tuple<M> removeTuple(int tuple){
		return this.tuples.remove(tuple);
	}
	
	public List<Tuple<M>> getTuples(){
		return Collections.unmodifiableList(this.tuples);
	}
	
	public int size(){
		return this.tuples.size();
	}
	
	public Tuple<M> getMean(){
		if(tuples.size()==0){
			System.err.println("KEINE TUPLES UM MEAN ZU BERECHNEN!!");
		}
		Tuple<M> mean = tuples.get(0).clone();
		for(int i=0;i<attributes.length;i++){
			Number val = tuples.get(0).getAttribute(attributes[i]);
			double sum = val.doubleValue();
			int count = 1;
			for(int k=1;k<tuples.size();k++){
				count++;				
				val = tuples.get(k).getAttribute(attributes[i]);
				sum = sum + val.doubleValue();
			}			
			double currentMean = sum / count;
			mean.setAttribute(attributes[i], currentMean);
		}		
		return mean;
	}
	
	public boolean isEmpty(){
		return (this.tuples.size()==0);
	}
	
}
