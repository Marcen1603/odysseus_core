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

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;

public abstract class AbstractNonBlockingWindowTIPO<T extends IStreamObject<? extends ITimeInterval>>
		extends AbstractWindowTIPO<T> {

	public AbstractNonBlockingWindowTIPO(AbstractWindowAO algebraOp) {
		super(algebraOp);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
//		System.err.println("MG WINDOW DEBUG: IN " + object);

		ITimeInterval time = object.getMetadata();
		PointInTime end = this.calcWindowEnd(time);
		if (end.after(time.getStart())) {
			time.setEnd(end);
			this.transfer(object);
		}
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	protected abstract PointInTime calcWindowEnd(ITimeInterval interval);


}
