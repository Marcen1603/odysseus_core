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

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;

/**
 * Allgemeine Klasse fuer SlidingTimeWindow. Spezielle implementierungen fuer SlidingTimeWindow mit Delta = 1 und FixedWindow mit Delta = WindowSize
 * vorhanden, da diese performanter sind, als diese allgemeine Implementierung.
 * 
 * @author abolles
 *
 * @param <T>
 */
public class SlidingAdvanceTimeWindowTIPO <T extends IStreamObject<? extends ITimeInterval>> extends AbstractNonBlockingWindowTIPO<T>{

	public SlidingAdvanceTimeWindowTIPO(AbstractWindowAO algebraOp) {
		super(algebraOp);
	}

	public SlidingAdvanceTimeWindowTIPO(SlidingAdvanceTimeWindowTIPO<T> name) {
		super(name);
	}
		
	@Override
	protected PointInTime calcWindowEnd(ITimeInterval time) {		
		// Hint: This is an integer division!
		return new PointInTime((time.getStart().getMainPoint() / windowAdvance)
				* windowAdvance + windowSize);
	}

	@Override
	public SlidingAdvanceTimeWindowTIPO<T> clone() {
		return new SlidingAdvanceTimeWindowTIPO<T>(this);
	}
	

}
