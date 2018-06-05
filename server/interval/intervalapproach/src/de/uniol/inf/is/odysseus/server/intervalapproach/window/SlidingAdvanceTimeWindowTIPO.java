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
 * Allgemeine Klasse fuer SlidingTimeWindow. Spezielle implementierungen fuer SlidingTimeWindow mit Delta = 1 und FixedWindow mit Delta = WindowSize
 * vorhanden, da diese performanter sind, als diese allgemeine Implementierung.
 *
 * @author abolles
 *
 * @param <T>
 */
public class SlidingAdvanceTimeWindowTIPO <M extends ITimeInterval, T extends IStreamObject<M>> extends AbstractNonBlockingWindowTIPO<M, T>{

	public SlidingAdvanceTimeWindowTIPO(AbstractWindowAO algebraOp) {
		super(algebraOp);
	}

	public SlidingAdvanceTimeWindowTIPO(TimeUnit baseTimeUnit,
			TimeValueItem windowSize, TimeValueItem windowAdvance, SDFSchema inputSchema){
		super(WindowType.TIME, baseTimeUnit, windowSize, windowAdvance,null, false, null, inputSchema);
	}



	@Override
	protected PointInTime calcWindowEnd(PointInTime time) {
		// Hint: This is an integer division!
		return new PointInTime((time.getMainPoint() / windowAdvance)
				* windowAdvance + windowSize);
	}

}
