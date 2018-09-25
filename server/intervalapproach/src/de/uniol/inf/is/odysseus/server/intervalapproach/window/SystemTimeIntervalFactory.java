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
import de.uniol.inf.is.odysseus.core.server.metadata.AbstractMetadataUpdater;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

/**
 * @author Jonas Jacobi
 */
public class SystemTimeIntervalFactory<M extends ITimeInterval, T extends IStreamObject<M>> extends AbstractMetadataUpdater<M, T> {

	private boolean clearEnd;

	public void clearEnd(boolean clear) {
		this.clearEnd = clear;
	}

	public boolean isClearEnd() {
		return clearEnd;
	}

	@Override
	public void updateMetadata(T inElem) {
		if (clearEnd) {
			inElem.getMetadata().setEnd(PointInTime.getInfinityTime());
		} else {
			M metadata = inElem.getMetadata();
			metadata.setStart(PointInTime.currentPointInTime());
		}
	}

}
