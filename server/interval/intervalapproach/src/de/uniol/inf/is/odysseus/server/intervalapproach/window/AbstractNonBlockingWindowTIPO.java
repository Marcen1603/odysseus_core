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
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

public abstract class AbstractNonBlockingWindowTIPO<M extends ITimeInterval, T extends IStreamObject<M>>
		extends AbstractWindowTIPO<T> {

	public AbstractNonBlockingWindowTIPO(AbstractWindowAO algebraOp) {
		super(algebraOp);
	}

	protected AbstractNonBlockingWindowTIPO(WindowType windowType, TimeUnit baseTimeUnit,
			TimeValueItem windowSize, TimeValueItem windowAdvance,
			TimeValueItem windowSlide, boolean partioned,
			List<SDFAttribute> partitionedBy, SDFSchema inputSchema){
		super(windowType, baseTimeUnit, windowSize, windowAdvance, windowSlide, partioned, partitionedBy, inputSchema);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
//		System.err.println("MG WINDOW DEBUG: IN " + object);

		PointInTime start = object.getMetadata().getStart();
		PointInTime end = this.calcWindowEnd(start);

		if (end.after(start)) {
			M m = (M) object.getMetadata().clone();
			m.setEnd(end);
			object.setMetadata(m);
			this.transfer(object);
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	protected abstract PointInTime calcWindowEnd(PointInTime interval);


}
