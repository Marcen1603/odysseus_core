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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

abstract public class AbstractWindowAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 349442832133715634L;

	private WindowType windowType = null;

	private long windowSize = -1;

	private long windowAdvance = -1;

	private TimeUnit timeUnit = TimeUnit.MILLISECONDS;
	
	// For predicate based windows
	private IPredicate<?> startCondition;
	private IPredicate<?> endCondition;
	private boolean sameStarttime;

	private List<SDFAttribute> partitionedBy;

	private long windowSlide = -1;

	public AbstractWindowAO(WindowType windowType) {
		super();
		this.windowType = windowType;
	}

	public AbstractWindowAO(AbstractWindowAO windowPO) {
		super(windowPO);
		this.windowSize = windowPO.windowSize;
		this.windowAdvance = windowPO.windowAdvance;
		this.windowSlide = windowPO.windowSlide;
		this.partitionedBy = windowPO.partitionedBy;
		this.windowType = windowPO.windowType;
		this.startCondition = windowPO.startCondition;
		this.endCondition = windowPO.endCondition;
		this.sameStarttime = windowPO.sameStarttime;
		this.timeUnit = windowPO.timeUnit;
	}

	public AbstractWindowAO() {
		super();
	}

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

	public WindowType getWindowType() {
		return windowType;
	}

	public void setWindowType(WindowType windowType) {
		this.windowType = windowType;
	}
	
	public List<String> getWindowTypes(){
		return WindowType.getValues();
	}

	public List<SDFAttribute> getPartitionBy() {
		if (partitionedBy != null) {
			return Collections.unmodifiableList(partitionedBy);
		}
		return new ArrayList<SDFAttribute>();
	}

	public boolean isPartitioned() {
		return this.partitionedBy != null;
	}

	public void setPartitionBy(List<SDFAttribute> partitionedBy) {
		this.partitionedBy = partitionedBy;
	}

	public long getWindowAdvance() {
		return windowAdvance;
	}

	public long getWindowSlide() {
		return windowSlide;
	}

	public void setWindowSlide(long slide) {
		this.windowSlide = slide;
	}

	public void setWindowAdvance(long windowAdvance) {
		this.windowAdvance = windowAdvance;
	}

	public void setStartCondition(IPredicate<?> startCondition) {
		this.startCondition = startCondition;
	}

	public IPredicate<?> getStartCondition() {
		return startCondition;
	}

	public void setEndCondition(IPredicate<?> endCondition) {
		this.endCondition = endCondition;
	}
	
	@Parameter(type = StringParameter.class, name = "Unit", optional = true, possibleValues="__JAVA_TIMEUNITS")
	public void setUnit(String unit){
		this.timeUnit = TimeUnit.valueOf(unit);
	}
	
	public String getUnit() {
		return timeUnit.toString();
	}
	
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public IPredicate<?> getEndCondition() {
		return endCondition;
	}

	/**
	 * @param sameStarttime
	 *            the sameStarttime to set
	 */
	public void setSameStarttime(boolean sameStarttime) {
		this.sameStarttime = sameStarttime;
	}

	/**
	 * @return the sameStarttime
	 */
	public boolean isSameStarttime() {
		return sameStarttime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
	 * #getPredicates()
	 */
	@Override
	public List<IPredicate<?>> getPredicates() {
		List<IPredicate<?>> pred = new LinkedList<IPredicate<?>>();
		if (startCondition != null) {
			pred.add(startCondition);
		}
		if (endCondition != null) {
			pred.add(endCondition);
		}
		return pred;

	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator#providesPredicates()
	 */
	@Override
	public boolean providesPredicates() {
		return true;
	}

	@Override
	public boolean isValid() {
		switch (windowType) {
		case TIME:
			if (this.partitionedBy != null) {
				addError(new IllegalParameterException(
						"can't use partition in time window"));
				return false;
			}
			if (this.windowSlide > 0 && this.windowAdvance > 0) {
				addError(new IllegalParameterException(
						"can't use slide and advance at the same time"));
				return false;
			}
			return true;
		case TUPLE:
			if (this.windowSlide > 0 && this.windowAdvance > 0) {
				addError(new IllegalParameterException(
						"can't use slide and advance at the same time"));
				return false;
			}
			return true;
		case UNBOUNDED:
			boolean isValid = true;
			if (this.windowSize != -1) {
				isValid = false;
				addError(new IllegalParameterException(
						"can't use size in unbounded window"));
			}
			if (this.windowAdvance != -1) {
				isValid = false;
				addError(new IllegalParameterException(
						"can't use advance in unbounded window"));
			}
			if (this.partitionedBy != null) {
				isValid = false;
				addError(new IllegalParameterException(
						"can't use partition in unbounded window"));
			}
			if (this.windowSlide != -1) {
				isValid = false;
				addError(new IllegalParameterException(
						"can't use slide in unbounded window"));
			}
			return isValid;
		case PREDICATE:
			isValid = true;
			// Todo check validity 
			// esp. check predicates!
			return isValid;
		default:
			break;
		}
		return true;
	}

}
