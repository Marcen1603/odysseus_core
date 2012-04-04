/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

@LogicalOperator(minInputPorts = 1, maxInputPorts = 1, name = "WINDOW")
public class WindowAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 349442832133715634L;

	private WindowType windowType = null;

	private long windowSize = -1;

	private long windowAdvance = -1;

	private List<SDFAttribute> partitionedBy;

	private long windowSlide = -1;

	@GetParameter(name = "ADVANCE")
	public long getWindowAdvance() {
		return windowAdvance;
	}

	@GetParameter(name = "SLIDE")
	public long getWindowSlide() {
		return windowSlide;
	}

	@Parameter(type = LongParameter.class, name = "SLIDE", optional = true)
	public void setWindowSlide(long slide) {
		this.windowSlide = slide;
	}

	@Parameter(type = LongParameter.class, name = "ADVANCE", optional = true)
	public void setWindowAdvance(long windowAdvance) {
		this.windowAdvance = windowAdvance;
	}

	public WindowAO(WindowType windowType) {
		super();
		this.windowType = windowType;
	}

	public WindowAO(WindowAO windowPO) {
		super(windowPO);
		this.windowSize = windowPO.windowSize;
		this.windowAdvance = windowPO.windowAdvance;
		this.windowSlide = windowPO.windowSlide;
		this.partitionedBy = windowPO.partitionedBy;
		this.windowType = windowPO.windowType;
	}

	public WindowAO() {
		super();
	}

	@Override
	public WindowAO clone() {
		return new WindowAO(this);
	}

	@GetParameter(name = "SIZE")
	public long getWindowSize() {
		return windowSize;
	}

	@Parameter(type = LongParameter.class, name = "SIZE", optional = true)
	public void setWindowSize(long windowSize) {
		this.windowSize = windowSize;
	}

	public void setWindowSizeString(String size) {
		this.windowSize = Long.parseLong(size);
	}

	@GetParameter(name = "TYPE")
	public WindowType getWindowType() {
		return windowType;
	}

	@Parameter(type = EnumParameter.class, name = "TYPE")
	public void setWindowType(WindowType windowType) {
		this.windowType = windowType;
	}

	@Override
	public String getName() {
		return WindowAO.class.getSimpleName();
	}

	@GetParameter(name = "PARTITION")
	public List<SDFAttribute> getPartitionBy() {
		if (partitionedBy != null) {
			return Collections.unmodifiableList(partitionedBy);
		}
        return new ArrayList<SDFAttribute>();
	}

	public boolean isPartitioned() {
		return this.partitionedBy != null;
	}

	@Parameter(name = "PARTITION", type = ResolvedSDFAttributeParameter.class, optional = true, isList = true)
	public void setPartitionBy(List<SDFAttribute> partitionedBy) {
		this.partitionedBy = partitionedBy;
	}

	@Override
	public boolean isValid() {
		switch (windowType) {
		case TIME:
			if (this.partitionedBy != null) {
				addError(new IllegalParameterException("can't use partition in time window"));
				return false;
			}
			if (this.windowSlide > 0 && this.windowAdvance > 0) {
				addError(new IllegalParameterException("can't use slide and advance at the same time"));
				return false;
			}
			return true;
		case TUPLE:
			if (this.windowSlide != -1) {
				addError(new IllegalParameterException("can't use slide in tuple window"));
				return false;
			}
			return true;
		case UNBOUNDED:
			boolean isValid = true;
			if (this.windowSize != -1) {
				isValid = false;
				addError(new IllegalParameterException("can't use size in unbounded window"));
			}
			if (this.windowAdvance != -1) {
				isValid = false;
				addError(new IllegalParameterException("can't use advance in unbounded window"));
			}
			if (this.partitionedBy != null) {
				isValid = false;
				addError(new IllegalParameterException("can't use partition in unbounded window"));
			}
			if (this.windowSlide != -1) {
				isValid = false;
				addError(new IllegalParameterException("can't use slide in unbounded window"));
			}
			return isValid;
		}
		return true;
	}
}
