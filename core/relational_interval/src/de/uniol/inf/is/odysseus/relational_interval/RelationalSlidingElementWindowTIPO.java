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
package de.uniol.inf.is.odysseus.relational_interval;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.window.SlidingElementWindowTIPO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class RelationalSlidingElementWindowTIPO extends
		SlidingElementWindowTIPO<Tuple<ITimeInterval>> {

	private int[] gRestrict;
	Map<Tuple<ITimeInterval>, Integer> keyMap = null;
	int maxId = 0;
	private Map<Integer, List<Tuple<ITimeInterval>>> buffers = null;
	private DefaultTISweepArea<Tuple<ITimeInterval>> outputQueue = new DefaultTISweepArea<Tuple<ITimeInterval>>();

	public RelationalSlidingElementWindowTIPO(WindowAO ao) {
		super(ao);
		init(ao);
	}

	@Override
	public void process_open() {
		keyMap = new HashMap<Tuple<ITimeInterval>, Integer>();
		buffers = new HashMap<Integer, List<Tuple<ITimeInterval>>>();
	}

	private void init(WindowAO ao) {
		List<SDFAttribute> grAttribs = ao.getPartitionBy();
		if (grAttribs != null && grAttribs.size() > 0) {
			gRestrict = new int[grAttribs.size()];
			for (int i = 0; i < grAttribs.size(); i++) {
				gRestrict[i] = ao.getInputSchema().indexOf(
						grAttribs.get(i));
			}
		}
	}

	@Override
	protected synchronized void process_next(
			Tuple<ITimeInterval> object, int port) {
		if (isPartitioned()) {
			int bufferId = getGroupID(object);
			List<Tuple<ITimeInterval>> buffer = buffers.get(bufferId);
			if (buffer == null) {
				buffer = new LinkedList<Tuple<ITimeInterval>>();
				buffers.put(bufferId, buffer);
			}
			buffer.add(object);
			processBuffer(buffer, object);
		} else {
			super.process_next(object, port);
		}
	}

	public int getGroupID(Tuple<ITimeInterval> elem) {
		// Wenn es keine Gruppierungen gibt, ist der Schl�ssel immer gleich 0
		if (gRestrict == null || gRestrict.length == 0)
			return 0;
		// Ansonsten das Tupel auf die Gruppierungsattribute einschr�nken
		Tuple<ITimeInterval> gTuple = elem.restrict(gRestrict, true);
		// Gibt es diese Kombination schon?
		Integer id = keyMap.get(gTuple);
		// Wenn nicht, neu eintragen
		if (id == null) {
			id = ++maxId;
			keyMap.put(gTuple, id);
		}
		return id;
	}

	@Override
	public void transfer(Tuple<ITimeInterval> object) {

		outputQueue.insert(object);
		PointInTime minTS = getMinTS();
		Iterator<Tuple<ITimeInterval>> out = outputQueue
				.extractElementsBefore(minTS);
		while (out.hasNext()) {
			super.transfer(out.next());
		}
	}

	private PointInTime getMinTS() {
		PointInTime minTS = PointInTime.getInfinityTime();
		for (List<Tuple<ITimeInterval>> b : buffers.values()) {
			// an der obersten Stelle eines jeden Puffers steht das pro
			// partition aelteste Element
			PointInTime p = b.get(0).getMetadata().getStart();
			if (p.before(minTS)) {
				minTS = p;
			}
		}
		return minTS;
	}

}
