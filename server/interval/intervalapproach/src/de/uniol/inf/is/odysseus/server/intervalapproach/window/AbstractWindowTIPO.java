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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.IUpdateableWindow;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IWindow;

public abstract class AbstractWindowTIPO<T extends IStreamObject<? extends ITimeInterval>> extends AbstractPipe<T, T>
		implements IWindow, IUpdateableWindow {
	protected long windowSize;
	protected long windowAdvance;
	protected final WindowType windowType;
	protected final boolean partitioned;
	protected boolean usesSlideParam;

	private final TimeUnit baseTimeUnit;
	private final List<SDFAttribute> partitionedBy;
	private final SDFSchema inputSchema;

	public AbstractWindowTIPO(AbstractWindowAO ao) {
		this(ao.getWindowType(), ao.getBaseTimeUnit(), ao.getWindowSize(), ao.getWindowAdvance(), ao.getWindowSlide(),
				ao.isPartitioned(), ao.getPartitionBy(), ao.getInputSchema());
	}

	public AbstractWindowTIPO(WindowType windowType, TimeUnit baseTimeUnit, TimeValueItem windowSize,
			TimeValueItem windowAdvance, TimeValueItem windowSlide, boolean partioned, List<SDFAttribute> partitionedBy,
			SDFSchema inputSchema) {

		if (baseTimeUnit == null) {
			this.baseTimeUnit = TimeUnit.MILLISECONDS;
		} else {
			this.baseTimeUnit = baseTimeUnit;
		}

		this.inputSchema = inputSchema;

		if (windowSize != null) {
			this.windowSize = this.baseTimeUnit.convert(windowSize.getTime(), windowSize.getUnit());
		} else {
			this.windowSize = -1;
		}
		if (windowAdvance == null && windowSlide == null) {
			this.windowAdvance = 1;
			usesSlideParam = false;
		} else if (windowAdvance != null) {
			this.windowAdvance = this.baseTimeUnit.convert(windowAdvance.getTime(), windowAdvance.getUnit());
			usesSlideParam = false;
		} else {
			this.windowAdvance = this.baseTimeUnit.convert(windowSlide.getTime(), windowSlide.getUnit());
			usesSlideParam = true;
		}
		this.windowType = windowType;
		this.partitioned = partioned;
		if (partitionedBy != null) {
			this.partitionedBy = new ArrayList<>(partitionedBy);
		} else {
			this.partitionedBy = null;
		}
		// setName(getName() + " s=" + windowSize + " a=" + windowAdvance);
		addParameterInfo("unit-based size", this.windowSize);
		if (!usesSlideParam) {
			addParameterInfo("unit-based advance", this.windowAdvance);
		} else {
			addParameterInfo("unit-based slide", this.windowAdvance);
		}

	}

	@Override
	public long getWindowSize() {
		return windowSize;
	}

	@Override
	public void setWindowSize(long newWindowSize){
		this.windowSize = newWindowSize;
	}

	@Override
	public long getWindowAdvance() {
		return windowAdvance;
	}

	@Override
	public void setWindowAdvance(long newWindowAdvance){
		this.windowAdvance = newWindowAdvance;
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

	public List<SDFAttribute> getPartitionedBy() {
		return partitionedBy;
	}

	public SDFSchema getInputSchema() {
		return inputSchema;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof AbstractWindowTIPO) || !this.getClass().toString().equals(ipo.getClass().toString())) {
			return false;
		}

		// System.out.println("SAME WINDOWS - Step 1");
		// System.out.println(this.getClass().toString());
		// System.out.println(ipo.getClass().toString());
		AbstractWindowTIPO<T> awtipo = (AbstractWindowTIPO<T>) ipo;

		// System.out.println("SAME WINDOWS - Step 2");
		if (this.windowSize == awtipo.windowSize && this.windowAdvance == awtipo.windowAdvance
				&& this.windowType.compareTo(awtipo.windowType) == 0 && this.partitioned == awtipo.isPartitioned()
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
