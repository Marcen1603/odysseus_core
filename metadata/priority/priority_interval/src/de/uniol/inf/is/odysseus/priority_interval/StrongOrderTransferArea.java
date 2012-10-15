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
package de.uniol.inf.is.odysseus.priority_interval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;

public class StrongOrderTransferArea<K extends ITimeIntervalPriority, R extends IStreamObject<K>, W extends IStreamObject<K>>
		extends TITransferArea<R,W> {

	@SuppressWarnings("unchecked")
	@Override
	public void newHeartbeat(PointInTime heartbeat, int port) {
		ArrayList<W> output = new ArrayList<W>();
		PointInTime minimum = null;
		synchronized (minTs) {
			minTs[port] = heartbeat;
			if (minTs[0] != null && minTs[1] != null) {
				minimum = PointInTime.min(minTs[0], minTs[1]);
			}
		}
		if (minimum != null) {
			synchronized (this.outputQueue) {
				// don't use an iterator, it does NOT guarantee ordered
				// traversal!
				W elem = this.outputQueue.peek();
				while (elem != null
						&& elem.getMetadata().getStart()
								.beforeOrEquals(minimum)) {
					this.outputQueue.poll();
					output.add(elem);
					elem = this.outputQueue.peek();
				}

				Object[] sortedOutput = output.toArray();
				Arrays.sort(sortedOutput, new Comparator<Object>() {

					@Override
					public int compare(Object o1, Object o2) {
						W t1 = (W) o1;
						W t2 = (W) o2;
						byte t1priority = t1.getMetadata().getPriority();
						byte t2priority = t2.getMetadata().getPriority();
						if (t1priority > t2priority) {
							return -1;
						}
                        if (t1priority < t2priority) {
                        	return 1; 
                        }
                        return t1.getMetadata().compareTo(t2.getMetadata());
					}
				});
				for (Object element : sortedOutput) {
					transfer((W) element);
				}
			}
		}
	}

}
