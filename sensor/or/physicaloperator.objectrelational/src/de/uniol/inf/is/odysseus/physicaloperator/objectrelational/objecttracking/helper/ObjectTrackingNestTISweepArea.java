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
package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.helper;

import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;

/**
 * This sweep area sub class stores grouping attributes for later 
 * evaluation of the partial nest (transforming to a relational tuple) 
 * 
 * @author Jendrik Poloczek
 */
public class ObjectTrackingNestTISweepArea<M extends ObjectTrackingMetadata<Object>> 
    extends DefaultTISweepArea<ObjectTrackingPartialNest<M>> {
	
	Object[] groupingValues;

	public ObjectTrackingNestTISweepArea(Object[] groupingValues) {
		super();
		this.groupingValues = groupingValues.clone();
	}

	public ObjectTrackingNestTISweepArea(ObjectTrackingNestTISweepArea<M> toCopy) {
	    super(toCopy);
	    this.groupingValues = toCopy.groupingValues.clone();
	}
	
	public Object[] getGroupingValues() {
		return this.groupingValues;
	}
	
	@Override
	public ObjectTrackingNestTISweepArea<M> clone() {
	    return new ObjectTrackingNestTISweepArea<M>(this);
	}
}
