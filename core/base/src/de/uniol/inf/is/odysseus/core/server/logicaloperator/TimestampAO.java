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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(minInputPorts = 1, maxInputPorts = 1, name = "Timestamp")
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
		this.startTimestamp = ao.startTimestamp;
		this.endTimestamp = ao.endTimestamp;
		this.isUsingSystemTime = ao.isUsingSystemTime;
		this.isUsingNoTime = ao.isUsingNoTime;
		this.clearEnd = ao.clearEnd;
		this.dateFormat = ao.dateFormat;
		this.timezone = ao.timezone;
		startTimestampYear= ao.startTimestampYear;
		startTimestampMonth= ao.startTimestampMonth;
		startTimestampDay= ao.startTimestampDay;
		startTimestampHour= ao.startTimestampHour;
		startTimestampMinute= ao.startTimestampMinute;
		startTimestampSecond= ao.startTimestampSecond;
		startTimestampMillisecond= ao.startTimestampMillisecond;
		factor= ao.factor;
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
	}
	
	@Parameter(type = BooleanParameter.class, name = "clearEnd", isList = false, optional = true)
	public void setClearEnd(boolean clearEnd) {
		this.clearEnd = clearEnd;
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
	
	public void setIsUsingSystemTime(boolean value) {
		this.isUsingSystemTime = value;
	}
	
	public void setUsingNoTime(boolean b){
		this.isUsingNoTime = b;
	}
	
	public boolean isUsingNoTime(){
		return this.isUsingNoTime;
	}

	public SDFAttribute getStartTimestampYear() {
		return startTimestampYear;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "YEAR", isList = false, optional = true)
	public void setStartTimestampYear(SDFAttribute startTimestampYear) {
		this.startTimestampYear = startTimestampYear;
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
		this.startTimestampMonth = startTimestampMonth;
	}

	public SDFAttribute getStartTimestampDay() {
		return startTimestampDay;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "DAY", isList = false, optional = true)
	public void setStartTimestampDay(SDFAttribute startTimestampDay) {
		this.startTimestampDay = startTimestampDay;
	}

	public SDFAttribute getStartTimestampHour() {
		return startTimestampHour;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "HOUR", isList = false, optional = true)
	public void setStartTimestampHour(SDFAttribute startTimestampHour) {
		this.startTimestampHour = startTimestampHour;
	}

	public SDFAttribute getStartTimestampMinute() {
		return startTimestampMinute;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "MINUTE", isList = false, optional = true)
	public void setStartTimestampMinute(SDFAttribute startTimestampMinute) {
		this.startTimestampMinute = startTimestampMinute;
	}

	public SDFAttribute getStartTimestampSecond() {
		return startTimestampSecond;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "SECOND", isList = false, optional = true)
	public void setStartTimestampSecond(SDFAttribute startTimestampSecond) {
		this.startTimestampSecond = startTimestampSecond;
	}

	public SDFAttribute getStartTimestampMillisecond() {
		return startTimestampMillisecond;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "MILLISECOND", isList = false, optional = true)
	public void setStartTimestampMillisecond(SDFAttribute startTimestampMillisecond) {
		this.startTimestampMillisecond = startTimestampMillisecond;
	}

	public int getFactor() {
		return factor;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "FACTOR", isList = false, optional = true)
	public void setFactor(int factor) {
		this.factor = factor;
	}

	@Override
	public String toString() {
		return  super.toString()+"s :"+startTimestamp+" e:"+endTimestamp+" "+isUsingSystemTime+" "+isUsingNoTime+" "+clearEnd;
	}

	public String getDateFormat() {
		return dateFormat;
	}
	
	@Parameter(type = StringParameter.class, name = "dateFormat", isList = false, optional = true)
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public String getTimezone() {
		return dateFormat;
	}
	
	@Parameter(type = StringParameter.class, name = "timezone", isList = false, optional = true)
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getStandardName() {
		if (isUsingSystemTime){
			return "SystemTime";
		}
		if (isUsingNoTime){
			return "NoTime";
		}
		return "ApplicationTime";
	}
	
}
