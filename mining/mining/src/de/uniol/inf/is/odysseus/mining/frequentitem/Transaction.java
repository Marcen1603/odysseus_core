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
package de.uniol.inf.is.odysseus.mining.frequentitem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;

/**
 * @author Dennis Geesen
 * 
 */
public class Transaction<M extends ITimeInterval> {

	private List<Tuple<M>> elements = new ArrayList<Tuple<M>>();
	private PointInTime minTime = PointInTime.getZeroTime();
	private PointInTime maxTime = PointInTime.getInfinityTime();

	public void addElement(Tuple<M> tuple) {
		this.elements.add(tuple);
		minTime = PointInTime.max(getMinTime(), tuple.getMetadata().getStart());
		maxTime = PointInTime.min(getMaxTime(), tuple.getMetadata().getEnd());
		recalcTimes();
	}

	private void recalcTimes() {
		for (Tuple<M> tuple : elements) {
			tuple.getMetadata().setStartAndEnd(getMinTime(), getMaxTime());
		}
	}

	public List<Tuple<M>> getElements() {
		return Collections.unmodifiableList(elements);
	}

	@Override
	public String toString() {
		String s = "Transaction [";
		for (Tuple<M> t : elements) {
			s = s + "; " + t;
		}
		s = s + "]";
		return s;
	}

	public List<Tuple<M>> getFBasedList(List<Pair<Tuple<M>, Integer>> fList) {
		List<Tuple<M>> liste = new ArrayList<Tuple<M>>();
		for (Pair<Tuple<M>, Integer> fItem : fList) {
			if (this.elements.contains(fItem.getE1())) {
				liste.add(fItem.getE1());
			}
			// the new list is at most as long as this transaction
			if (liste.size() == this.elements.size()) {
				break;
			}
		}
		return liste;
	}

	/**
	 * @return the minTime
	 */
	public PointInTime getMinTime() {
		return minTime;
	}
	

	/**
	 * @return the maxTime
	 */
	public PointInTime getMaxTime() {
		return maxTime;
	}
	

}
