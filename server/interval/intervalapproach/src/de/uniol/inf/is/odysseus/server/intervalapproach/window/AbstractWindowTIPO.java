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
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IWindow;

public abstract class AbstractWindowTIPO<T extends IStreamObject<? extends ITimeInterval>>
		extends AbstractPipe<T, T> implements IWindow {
	protected final long windowSize;
	protected final long windowAdvance;
	protected final WindowType windowType;
	protected final boolean partitioned;
	final boolean usesSlideParam;

	private final TimeUnit baseTimeUnit;

	public AbstractWindowTIPO(AbstractWindowAO ao) {
		this.baseTimeUnit = ao.getBaseTimeUnit();

		if (ao.getWindowSize() != null) {
			this.windowSize = ao.getBaseTimeUnit().convert(
					ao.getWindowSize().getTime(), ao.getWindowSize().getUnit());
		} else {
			this.windowSize = -1;
		}
		if (ao.getWindowAdvance() == null && ao.getWindowSlide() == null) {
			this.windowAdvance = 1;
			usesSlideParam = false;
		} else if (ao.getWindowAdvance() != null) {
			this.windowAdvance = ao.getBaseTimeUnit().convert(
					ao.getWindowAdvance().getTime(),
					ao.getWindowAdvance().getUnit());
			usesSlideParam = false;
		} else {
			this.windowAdvance = ao.getBaseTimeUnit().convert(
					ao.getWindowSlide().getTime(),
					ao.getWindowSlide().getUnit());
			usesSlideParam = true;
		}
		// this.windowAO = ao;
		this.windowType = ao.getWindowType();
		this.partitioned = ao.isPartitioned();
		// setName(getName() + " s=" + windowSize + " a=" + windowAdvance);
		addParameterInfo("unit-based size", windowSize);
		if (!usesSlideParam) {
			addParameterInfo("unit-based advance", windowAdvance);
		} else {
			addParameterInfo("unit-based slide", windowAdvance);
		}
	}

	public AbstractWindowTIPO(AbstractWindowTIPO<T> window) {
		super(window);
		this.baseTimeUnit = window.baseTimeUnit;
		this.windowSize = window.windowSize;
		this.windowAdvance = window.windowAdvance;
		// this.windowAO = window.windowAO.clone();
		this.windowType = window.windowType;
		this.partitioned = window.partitioned;
		this.usesSlideParam = window.usesSlideParam;
	}

	@Override
	public long getWindowSize() {
		return windowSize;
	}

	public long getWindowAdvance() {
		return windowAdvance;
	}

	public long getWindowSizeMillis() {
		return TimeUnit.MILLISECONDS.convert(getWindowSize(), baseTimeUnit);
	}

	public long getWindowAdvanceMillis() {
		return TimeUnit.MILLISECONDS.convert(getWindowAdvance(), baseTimeUnit);
	}

	@Override
	public WindowType getWindowType() {
		return this.windowType;
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

		// System.out.println("SAME WINDOWS - Step 2");
		if (this.windowSize == awtipo.windowSize
				&& this.windowAdvance == awtipo.windowAdvance
				&& this.windowType.compareTo(awtipo.windowType) == 0
				&& this.partitioned == awtipo.isPartitioned()
				&& this.usesSlideParam == awtipo.usesSlideParam) {
			// System.out.println("SAME WINDOWS - Step 3");
			return true;
		}
		return false;
	}

	@Override
	abstract public void processPunctuation(IPunctuation punctuation, int port);

	public boolean isPartitioned() {
		return partitioned;
	}
}
