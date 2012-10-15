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
package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

/**
 * Stores temporally punctuations for later processing, when they get out of
 * date.
 * 
 * @author jan steinke
 * 
 * @param <W>
 * @param <R>
 */
public class PunctuationStorage<W extends IStreamObject<?>, R  extends IStreamObject<?>> {

	private List<List<PointInTime>> storage = new ArrayList<List<PointInTime>>();

	private int currentPort = 0;

	private IPunctuationPipe<R, W> pipe;

	public PunctuationStorage(IPunctuationPipe<R, W> pipe) {
		this.pipe = pipe;
	}

	public int getCurrentPort() {
		return currentPort;
	}

	public void setCurrentPort(int currentPort) {
		this.currentPort = currentPort;
	}

	/**
	 * Tests if the given input object matches with a number of stored
	 * punctuations and is used to handle punctuations when they get out of date
	 * (cleaning the punctuationStorage, send punctuations, ...).
	 * 
	 * @param object
	 *            input data with a timestamp
	 */
	public void updatePunctuationData(W object) {

		if (!storage.isEmpty()) {
			ITimeInterval time = (ITimeInterval) object.getMetadata();
			PointInTime start = time.getStart();

			Iterator<PointInTime> it = storage.get(currentPort).iterator();

			PointInTime lastPoint = null;

			while (it.hasNext()) {
				PointInTime curPoint = it.next();
				if (start.before(curPoint)) {
					break;
				}
                lastPoint = curPoint;
                it.remove();
			}

			if (lastPoint != null) {
				pipe.sendPunctuation(lastPoint);
			}
		}
	}

	public void subscribePort(int inputPortCount) {
		for (int i = storage.size(); i < inputPortCount; ++i) {
			storage.add(new LinkedList<PointInTime>());
		}
	}

	public void storePunctuation(PointInTime timestamp) {
		storage.get(currentPort).add(timestamp);
	}

	public int size() {
		int size = 0;
		for (List<PointInTime> each : storage) {
			size += each.size();
		}
		return size;
	}

}
