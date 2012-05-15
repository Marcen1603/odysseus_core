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
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;

/**
 * 
 * @author Dennis Geesen
 * Created at: 15.05.2012
 */
public class ClusterDescription<M extends ITimeInterval> {
		
	private List<Tuple<M>> tuples = new ArrayList<Tuple<M>>();
	
	
	public void addTuple(Tuple<M> tuple){
		this.tuples.add(tuple);
	}
	
	public void removeTuple(Tuple<M> tuple){
		this.tuples.remove(tuple);
	}
	
	public List<Tuple<M>> getTuples(){
		return Collections.unmodifiableList(this.tuples);
	}
	
	public Tuple<M> getMean(){
		Tuple<M> mean = new Tuple<M>(tuples.get(0).size());
		for(int i=0;i<mean.size();i++){
			double sum = 0.0;
			int count = 0;
			for(Tuple<M> tuple : this.tuples){
				count++;
				Number val = tuple.getAttribute(i);
				sum = sum + val.doubleValue();
			}
			double currentMean = sum / count;
			mean.setAttribute(i, currentMean);
		}		
		return mean;
	}
	
}
