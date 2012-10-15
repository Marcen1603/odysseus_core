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
 * @author Dennis Geesen Created at: 15.05.2012
 */
public class Cluster<M extends ITimeInterval> {

	private List<Tuple<M>> tuples = new ArrayList<Tuple<M>>();
	private Tuple<M> calculatedMean;
	private int id; 

	public Cluster(int id, Tuple<M> initialMean) {
		this.tuples.add(initialMean);
		calculatedMean = initialMean.clone();
		this.id = id;
		
	}

	public void addTuple(Tuple<M> tuple) {
		this.tuples.add(tuple);
	}

	/**
	 * @param attribute
	 * @param attribute2
	 * @return
	 */
	private Object add(Object attribute, Object attribute2) {
		double left = 0;
		double right = 0;
		if ((attribute instanceof Number) && (attribute instanceof Number)) {
			left = ((Number) attribute).doubleValue();
			right = ((Number) attribute2).doubleValue();
		} else {
			if (attribute.equals(attribute2)) {
				return 0;
			} else {
				return 1;
			}
		}
		return (left + right);
	}

	public boolean removeTuple(Tuple<M> tuple) {
		return this.tuples.remove(tuple);
	}

	public List<Tuple<M>> getTuples() {
		return Collections.unmodifiableList(this.tuples);
	}

	public Tuple<M> getMean() {
		return this.calculatedMean;
	}

	public int size() {
		return this.tuples.size();
	}

	public boolean isEmpty() {
		return (this.tuples.size() == 0);
	}
	
	public void recalcMean(){
		Tuple<M> mean = this.tuples.get(0).clone();
		for (Tuple<M> tuple : this.tuples) {
			for (int i = 1; i < tuple.size(); i++) {
				mean.setAttribute(i, add(tuple.getAttribute(i), mean.getAttribute(i)));
			}
		}
		for(int i=0;i<mean.size();i++){
			Object meanAttribute = ((Number)mean.getAttribute(i)).doubleValue() / tuples.size();
			mean.setAttribute(i, meanAttribute);
		}
		this.calculatedMean = mean;
	}
	
	public int getNumber() {
		return id;
	}

}
