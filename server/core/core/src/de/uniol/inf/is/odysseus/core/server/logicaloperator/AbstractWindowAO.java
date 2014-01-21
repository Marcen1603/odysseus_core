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
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatypeConstraint;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

abstract public class AbstractWindowAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 349442832133715634L;

	private WindowType windowType = null;

	private TimeValueItem windowSize = null;
	private TimeValueItem windowAdvance = null;
	private TimeUnit baseTimeUnit = null;

	// For predicate based windows
	private IPredicate<?> startCondition;
	private IPredicate<?> endCondition;
	private boolean sameStarttime;

	private List<SDFAttribute> partitionedBy;

	private TimeValueItem windowSlide = null;

	@Deprecated
	private TimeUnit timeUnit = null;

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
		this.baseTimeUnit = windowPO.baseTimeUnit;
	}

	public AbstractWindowAO() {
		super();
	}

	public TimeValueItem getWindowSize() {
		return windowSize;
	}

	@Parameter(type = TimeParameter.class, name = "SIZE", optional = true)
	public void setWindowSize(TimeValueItem windowSize) {
		this.windowSize = windowSize;
	}

	public void setWindowSizeString(TimeValueItem size) {
		this.windowSize = size;
	}

	public WindowType getWindowType() {
		return windowType;
	}

	public void setWindowType(WindowType windowType) {
		this.windowType = windowType;
	}

	public TimeUnit getBaseTimeUnit() {
		if (baseTimeUnit == null) {
			// Find input schema attribute with type start timestamp
			// It provides the correct base unit 
			// if not given use MILLISECONDS as default
			Collection<SDFAttribute> attrs = getInputSchema()
					.getSDFDatatypeAttributes(SDFDatatype.START_TIMESTAMP);
			if (attrs.isEmpty()) {
				attrs = getInputSchema().getSDFDatatypeAttributes(
						SDFDatatype.START_TIMESTAMP_STRING);
			}
			if (attrs.size() > 0) {
				SDFAttribute attr = attrs.iterator().next();
				SDFDatatypeConstraint constr = attr.getDtConstraint(SDFDatatypeConstraint.BASE_TIME_UNIT);
				if (constr != null){
					baseTimeUnit = (TimeUnit) constr.getValue();
				}
			} else {
				baseTimeUnit = TimeUnit.MILLISECONDS;
			}

		}
		return baseTimeUnit;
	}

	public void setBaseTimeUnit(TimeUnit baseTimeUnit) {
		this.baseTimeUnit = baseTimeUnit;
	}

	public List<String> getWindowTypes() {
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

	@Parameter(name = "PARTITION", type = ResolvedSDFAttributeParameter.class, optional = true, isList = true)
	public void setPartitionBy(List<SDFAttribute> partitionedBy) {
		this.partitionedBy = partitionedBy;
	}

	public TimeValueItem getWindowAdvance() {
		return windowAdvance;
	}

	public TimeValueItem getWindowSlide() {
		return windowSlide;
	}

	public void setWindowSlide(TimeValueItem slide) {
		this.windowSlide = slide;
	}

	public void setWindowAdvance(TimeValueItem windowAdvance) {
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

	@Deprecated
	@Parameter(type = StringParameter.class, name = "Unit", optional = true, possibleValues = "__JAVA_TIMEUNITS", deprecated = true)
	public void setUnit(String unit) {
		this.timeUnit = TimeUnit.valueOf(unit);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
	 * #providesPredicates()
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
			if (this.windowSlide != null && this.windowAdvance != null) {
				addError(new IllegalParameterException(
						"can't use slide and advance at the same time"));
				return false;
			}
			return checkTimeUnit();
		case TUPLE:
			if (this.windowSlide != null && this.windowAdvance != null) {
				addError(new IllegalParameterException(
						"can't use slide and advance at the same time"));
				return false;
			}
			return checkTimeUnit();
		case UNBOUNDED:
			boolean isValid = true;
			if (this.windowSize != null) {
				isValid = false;
				addError(new IllegalParameterException(
						"can't use size in unbounded window"));
			}
			if (this.windowAdvance != null) {
				isValid = false;
				addError(new IllegalParameterException(
						"can't use advance in unbounded window"));
			}
			if (this.partitionedBy != null) {
				isValid = false;
				addError(new IllegalParameterException(
						"can't use partition in unbounded window"));
			}
			if (this.windowSlide != null) {
				isValid = false;
				addError(new IllegalParameterException(
						"can't use slide in unbounded window"));
			}
			isValid = checkTimeUnit();
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

	private boolean checkTimeUnit() {
		// TODO: remove this, when deprecated "timeUnit" value is removed!
		if (timeUnit != null) {
			if (windowSize != null) {
				if (windowSize.getUnit() != null
						&& !timeUnit.equals(windowSize.getUnit())) {
					addError(new IllegalParameterException(
							"window size unit must be equals to unit (remark: unit is deprecated)!"));
					return false;
				}
			}
			if (windowAdvance != null) {
				if (windowAdvance.getUnit() != null
						&& !timeUnit.equals(windowAdvance.getUnit())) {
					addError(new IllegalParameterException(
							"window advance unit must be equals to unit (remark: unit is deprecated)!"));
					return false;
				}
			}
			if (windowSlide != null) {
				if (windowSlide.getUnit() != null
						&& !timeUnit.equals(windowSlide.getUnit())) {
					addError(new IllegalParameterException(
							"window slide unit must be equals to unit (remark: unit is deprecated)!"));
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void initialize() {
		// overwrite values, if only unit parameter is used (old version)
		// TODO: remove this, when deprecated "timeUnit" value is removed!
		TimeUnit useTime = TimeUnit.MILLISECONDS;
		if (this.timeUnit != null) {
			useTime = timeUnit;
		}
		if (this.windowSize != null && this.windowSize.getUnit() == null) {
			TimeValueItem newTimeUnit = new TimeValueItem(
					this.windowSize.getTime(), useTime);
			this.windowSize = newTimeUnit;
		}
		if (this.windowAdvance != null && this.windowAdvance.getUnit() == null) {
			TimeValueItem newTimeUnit = new TimeValueItem(
					this.windowAdvance.getTime(), useTime);
			this.windowAdvance = newTimeUnit;
		}
		if (this.windowSlide != null && this.windowSlide.getUnit() == null) {
			TimeValueItem newTimeUnit = new TimeValueItem(
					this.windowSlide.getTime(), useTime);
			this.windowSlide = newTimeUnit;
		}

		super.initialize();
	}

}
