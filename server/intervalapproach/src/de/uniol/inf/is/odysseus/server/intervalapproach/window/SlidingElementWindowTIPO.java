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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;

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


//	public SlidingElementWindowTIPO(TimeUnit baseTimeUnit,TimeValueItem windowSize, TimeValueItem windowAdvance,
//			TimeValueItem windowSlide,
//			List<SDFAttribute> partitionedBy, SDFSchema inputSchema){
//		super(WindowType.TUPLE, baseTimeUnit, windowSize, windowAdvance, windowSlide, partitionedBy, inputSchema);
//		advance = this.windowAdvance > 0 ? this.windowAdvance : 1;
//		if (this.windowSize < this.advance) {
//			throw new IllegalArgumentException(
//					"Sorry. Size < Advance currently not implemented!");
//		}
//	}
//
//	protected SlidingElementWindowTIPO(WindowType windowType,
//			TimeValueItem windowSize, TimeValueItem windowAdvance,
//			TimeValueItem windowSlide,
//			List<SDFAttribute> partitionedBy, SDFSchema inputSchema){
//		super(WindowType.TUPLE, null, windowSize, windowAdvance, windowSlide, partitionedBy, inputSchema);
//		advance = this.windowAdvance > 0 ? this.windowAdvance : 1;
//		if (this.windowSize < this.advance) {
//			throw new IllegalArgumentException(
//					"Sorry. Size < Advance currently not implemented!");
//		}
//	}

	@Override
	protected void process(T object, List<T> buffer, Object bufferId, PointInTime ts) {
		synchronized (buffer) {
			buffer.add(object);
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

	@SuppressWarnings("unchecked")
	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof SlidingElementWindowTIPO)){
			return false;
		}
		SlidingElementWindowTIPO<IStreamObject<ITimeInterval>> other = (SlidingElementWindowTIPO<IStreamObject<ITimeInterval>>)ipo;
		if (this.advance != other.advance){
			return false;
		}
		return super.isSemanticallyEqual(ipo);
	}


}
