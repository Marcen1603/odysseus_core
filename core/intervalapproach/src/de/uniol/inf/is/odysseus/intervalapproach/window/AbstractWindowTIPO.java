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
package de.uniol.inf.is.odysseus.intervalapproach.window;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IWindow;

public abstract class AbstractWindowTIPO<T extends IStreamObject<? extends ITimeInterval>>
		extends AbstractPipe<T, T> implements IWindow {
	protected final long windowSize;
	protected final long windowAdvance;
	protected final WindowType windowType;
	protected final boolean partitioned;

	public AbstractWindowTIPO(WindowAO ao) {
		this.windowSize = ao.getWindowSize();
		this.windowAdvance = ao.getWindowAdvance();
		// this.windowAO = ao;
		this.windowType = ao.getWindowType();
		this.partitioned = ao.isPartitioned();
		setName(getName() + " s=" + windowSize + " a=" + windowAdvance);
	}

	public AbstractWindowTIPO(AbstractWindowTIPO<T> window) {
		this.windowSize = window.windowSize;
		this.windowAdvance = window.windowAdvance;
		// this.windowAO = window.windowAO.clone();
		this.windowType = window.windowType;
		this.partitioned = window.partitioned;
	}

	@Override
	public long getWindowSize() {
		return windowSize;
	}

	public long getWindowAdvance() {
		return windowAdvance;
	}

	@Override
	public WindowType getWindowType() {
		return this.windowType;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof AbstractWindowTIPO)
				|| !this.getClass().toString()
						.equals(ipo.getClass().toString())) {
			return false;
		}

		// System.out.println("SAME WINDOWS - Step 1");
		// System.out.println(this.getClass().toString());
		// System.out.println(ipo.getClass().toString());
		AbstractWindowTIPO<T> awtipo = (AbstractWindowTIPO<T>) ipo;

		// Falls die Operatoren verschiedene Quellen haben, wird false zurück
		// gegeben
		if (!this.hasSameSources(awtipo)) {
			return false;
		}
		// System.out.println("SAME WINDOWS - Step 2");
		if (this.windowSize == awtipo.windowSize
				&& this.windowAdvance == awtipo.windowAdvance
				&& this.windowType.compareTo(awtipo.windowType) == 0) {
			// System.out.println("SAME WINDOWS - Step 3");
			return true;
		}
		return false;
	}

	public boolean isPartitioned() {
		return partitioned;
	}
}
