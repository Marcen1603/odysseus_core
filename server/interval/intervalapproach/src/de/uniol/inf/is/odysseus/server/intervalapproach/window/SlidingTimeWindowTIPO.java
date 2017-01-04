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

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

/**
 * Effizientere Implementierung eines SlidingAdvanceTimeWindow
 * @author Jonas Jacobi
 */
public class SlidingTimeWindowTIPO<M extends ITimeInterval, T extends IStreamObject<M>> extends
		AbstractNonBlockingWindowTIPO<M,T> {

	public SlidingTimeWindowTIPO(AbstractWindowAO algebraOp) {
		super(algebraOp);
	}

	public SlidingTimeWindowTIPO(TimeUnit baseTimeUnit,
			TimeValueItem windowSize, SDFSchema inputSchema){
		super(WindowType.TIME, baseTimeUnit, windowSize, null ,null, false, null, inputSchema);
	}

	@Override
	protected PointInTime calcWindowEnd(PointInTime time) {
		return time.sum(windowSize);
	}

}
