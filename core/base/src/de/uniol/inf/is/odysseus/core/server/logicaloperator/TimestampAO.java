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

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(minInputPorts = 1, maxInputPorts = 1, name = "Timestamp", doc="This Operator can be used to update the timestamp information in the meta data part. Be careful because this may lead undefined semantics", category={LogicalOperatorCategory.BASE})
public class TimestampAO extends UnaryLogicalOp {
	private static final long serialVersionUID = -467482177921504749L;

	// To be used if timestamps are given in one attribute
	private SDFAttribute startTimestamp;
	private SDFAttribute endTimestamp;
	// an optional String representation of the date format
	private String dateFormat;
	/** The timezone to use */
	private String timezone;
	// To be used if timestamps are seperated
	private SDFAttribute startTimestampYear;
	private SDFAttribute startTimestampMonth;
	private SDFAttribute startTimestampDay;
	private SDFAttribute startTimestampHour;
	private SDFAttribute startTimestampMinute;
	private SDFAttribute startTimestampSecond;
	private SDFAttribute startTimestampMillisecond;
	private int factor;

	private boolean isUsingSystemTime;
	private boolean isUsingNoTime;

	private boolean clearEnd;

	public TimestampAO(TimestampAO ao) {
		super(ao);
		setStartTimestamp(ao.startTimestamp);
		setEndTimestamp(ao.endTimestamp);
		setClearEnd(ao.clearEnd);
		setDateFormat(ao.dateFormat);
		setTimezone(ao.timezone);
		setStartTimestampYear(ao.startTimestampYear);
		setStartTimestampMonth(ao.startTimestampMonth);
		setStartTimestampDay(ao.startTimestampDay);
		setStartTimestampHour(ao.startTimestampHour);
		setStartTimestampMinute(ao.startTimestampMinute);
		setStartTimestampSecond(ao.startTimestampSecond);
		setStartTimestampMillisecond(ao.startTimestampMillisecond);
		setFactor(ao.factor);
		this.isUsingSystemTime = ao.isUsingSystemTime;
		this.isUsingNoTime = ao.isUsingNoTime;
	}

	public TimestampAO() {
		startTimestamp = null;
		endTimestamp = null;
		isUsingSystemTime = true;
		isUsingNoTime = false;
		clearEnd = false;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TimestampAO(this);
	}

	public SDFAttribute getStartTimestamp() {
		return startTimestamp;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "START", isList = false, optional = true)
	public void setStartTimestamp(SDFAttribute startTimestamp) {
		this.startTimestamp = startTimestamp;
		addParameterInfoIfNeeded("START", startTimestamp);
		if (this.startTimestamp == null) {
			this.isUsingSystemTime = true;
		} else {
			this.isUsingSystemTime = false;
		}
	}

	public SDFAttribute getEndTimestamp() {
		return endTimestamp;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "END", isList = false, optional = true)
	public void setEndTimestamp(SDFAttribute endTimestamp) {
		this.endTimestamp = endTimestamp;
		addParameterInfoIfNeeded("END", endTimestamp);
	}

	@Parameter(type = BooleanParameter.class, name = "clearEnd", isList = false, optional = true)
	public void setClearEnd(boolean clearEnd) {
		this.clearEnd = clearEnd;
		addParameterInfo("CLEAREND", "'" + Boolean.valueOf(clearEnd) + "'");
	}

	public boolean isClearEnd() {
		return clearEnd;
	}

	public boolean hasStartTimestamp() {
		return this.startTimestamp != null;
	}

	public boolean hasEndTimestamp() {
		return this.endTimestamp != null;
	}

	public boolean isUsingSystemTime() {
		return this.isUsingSystemTime;
	}

	@Parameter(type = BooleanParameter.class, name = "SystemTime", isList = false, optional = true)
	public void setIsUsingSystemTime(boolean value) {
		this.isUsingSystemTime = value;
	}

	public void setUsingNoTime(boolean b) {
		this.isUsingNoTime = b;
	}

	public boolean isUsingNoTime() {
		return this.isUsingNoTime;
	}

	public SDFAttribute getStartTimestampYear() {
		return startTimestampYear;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "YEAR", isList = false, optional = true)
	public void setStartTimestampYear(SDFAttribute startTimestampYear) {
		this.startTimestampYear = startTimestampYear;
		addParameterInfoIfNeeded("YEAR", startTimestampYear);
		if (this.startTimestampYear == null) {
			this.isUsingSystemTime = true;
		} else {
			this.isUsingSystemTime = false;
		}
	}

	public SDFAttribute getStartTimestampMonth() {
		return startTimestampMonth;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "MONTH", isList = false, optional = true)
	public void setStartTimestampMonth(SDFAttribute startTimestampMonth) {
		addParameterInfoIfNeeded("MONTH", startTimestampMonth);
		this.startTimestampMonth = startTimestampMonth;
	}

	public SDFAttribute getStartTimestampDay() {
		return startTimestampDay;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "DAY", isList = false, optional = true)
	public void setStartTimestampDay(SDFAttribute startTimestampDay) {
		addParameterInfoIfNeeded("DAY", startTimestampDay);
		this.startTimestampDay = startTimestampDay;
	}

	public SDFAttribute getStartTimestampHour() {
		return startTimestampHour;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "HOUR", isList = false, optional = true)
	public void setStartTimestampHour(SDFAttribute startTimestampHour) {
		addParameterInfoIfNeeded("HOUR", startTimestampHour);
		this.startTimestampHour = startTimestampHour;
	}

	public SDFAttribute getStartTimestampMinute() {
		return startTimestampMinute;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "MINUTE", isList = false, optional = true)
	public void setStartTimestampMinute(SDFAttribute startTimestampMinute) {
		addParameterInfoIfNeeded("MINUTE", startTimestampMinute);
		this.startTimestampMinute = startTimestampMinute;
	}

	public SDFAttribute getStartTimestampSecond() {
		return startTimestampSecond;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "SECOND", isList = false, optional = true)
	public void setStartTimestampSecond(SDFAttribute startTimestampSecond) {
		addParameterInfoIfNeeded("SECOND", startTimestampSecond);
		this.startTimestampSecond = startTimestampSecond;
	}

	public SDFAttribute getStartTimestampMillisecond() {
		return startTimestampMillisecond;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "MILLISECOND", isList = false, optional = true)
	public void setStartTimestampMillisecond(SDFAttribute startTimestampMillisecond) {
		addParameterInfoIfNeeded("MILLISECOND", startTimestampMillisecond);
		this.startTimestampMillisecond = startTimestampMillisecond;
	}

	public int getFactor() {
		return factor;
	}

	@Parameter(type = IntegerParameter.class, name = "FACTOR", isList = false, optional = true)
	public void setFactor(int factor) {
		addParameterInfo("FACTOR", Integer.valueOf(factor));
		this.factor = factor;
	}

	@Override
	public String toString() {
		return super.toString() + "s :" + startTimestamp + " e:" + endTimestamp + " " + isUsingSystemTime + " " + isUsingNoTime + " " + clearEnd;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	@Parameter(type = StringParameter.class, name = "dateFormat", isList = false, optional = true)
	public void setDateFormat(String dateFormat) {
		addParameterInfo("DATEFORMAT", dateFormat);
		this.dateFormat = dateFormat;
	}

	public String getTimezone() {
		return dateFormat;
	}

	@Parameter(type = StringParameter.class, name = "timezone", isList = false, optional = true)
	public void setTimezone(String timezone) {
		addParameterInfo("TIMEZONE", timezone);
		this.timezone = timezone;
	}

	public String getStandardName() {
		if (isUsingSystemTime) {
			return "SystemTime";
		}
		if (isUsingNoTime) {
			return "NoTime";
		}
		return "ApplicationTime";
	}

	private void addParameterInfoIfNeeded(String key, SDFAttribute attribute) {
		if (attribute != null) {
			addParameterInfo(key, "'" + attribute.getAttributeName() + "'");
		} else {
			addParameterInfo(key, null);
		}
	}

}
