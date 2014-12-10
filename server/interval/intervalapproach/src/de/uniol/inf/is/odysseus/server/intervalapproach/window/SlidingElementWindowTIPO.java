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
package de.uniol.inf.is.odysseus.server.intervalapproach.window;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class SlidingElementWindowTIPO<T extends IStreamObject<ITimeInterval>>
		extends AbstractPartitionedWindowTIPO<T> implements IStatefulPO {

	static final Logger LOG = LoggerFactory.getLogger(SlidingElementWindowTIPO.class);
	
	private final long advance;

	public SlidingElementWindowTIPO(AbstractWindowAO ao) {
		super(ao);
		advance = windowAdvance > 0 ? windowAdvance : 1;
		if (windowSize < advance) {
			throw new IllegalArgumentException(
					"Sorry. Size < Advance currently not implemented!");
		}
	}

	@Override
	protected void process(T object, List<T> buffer, Long bufferId, PointInTime ts) {
		synchronized (buffer) {
			// test if buffer has reached limit
			if (buffer.size() == this.windowSize + 1) {

				long elemsToSend = advance;
				// TODO: Problem: Window size smaller than advance
				// if (windowSize < windowAdvance) {
				// elemsToSend = windowSize;
				// }

				transferBuffer(buffer, elemsToSend, ts);

				// We need to determine the oldest element in all buffers and
				// send a punctuation to the transfer area
				ping();
			}
		}
	}
	

	@Override
	public AbstractPipe<T, T> clone() {
		throw new IllegalArgumentException("Currently not implemented!");
	}


}
