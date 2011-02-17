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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;

/**
 * G is a helper class which contains a min-priority queue list which holds the  
 * group identifier for each non-empty group. Its order is ascending: the 
 * group with the smallest start timestamp first. 
 * 
 * @author Jendrik Poloczek
 */
public class ObjectTrackingPriorityQueueG<M extends ObjectTrackingMetadata<Object>> {
	
	/*
	 * groupsQueue stores sweep areas in ascending order of the smallest
	 * timestamp of a PartialNest
	 */
	
	List<DefaultTISweepArea<ObjectTrackingPartialNest<M>>> groupsQueue;	
	Map<DefaultTISweepArea<ObjectTrackingPartialNest<M>>, Integer> gToId;
	
	public ObjectTrackingPriorityQueueG(){
		this.groupsQueue = 
			new ArrayList<DefaultTISweepArea<ObjectTrackingPartialNest<M>>>();
		this.gToId = 
			new HashMap<DefaultTISweepArea<ObjectTrackingPartialNest<M>>, 
			Integer>();
	};
	
	public ObjectTrackingPriorityQueueG(ObjectTrackingPriorityQueueG<M> g){
		this.groupsQueue.addAll(g.groupsQueue);
		this.gToId.putAll(g.gToId);
	}
	
	synchronized public void insert(
			DefaultTISweepArea<ObjectTrackingPartialNest<M>> sa,
			Integer groupID) {
		this.groupsQueue.add(sa);
		this.gToId.put(sa, groupID);
	}

	public boolean isEmpty() {
		return this.groupsQueue.size() == 0;
	}

	synchronized public Integer min() {
		if (isEmpty())
			return -1;

		// die Elemente der queue k�nnen sich �ndern ...
		// TODO: Nicht sehr effizient... Sortieren ist gar nicht
		// notwendig!!
		Collections.sort(this.groupsQueue);
		return gToId.get(this.groupsQueue.get(0));
	}

	synchronized public void removeLastMin() {
		if (isEmpty())
			return;
		gToId.remove(this.groupsQueue.remove(0));
	}
	
	@Override
	public ObjectTrackingPriorityQueueG<M> clone(){
		return new ObjectTrackingPriorityQueueG<M>(this);
	}
}
