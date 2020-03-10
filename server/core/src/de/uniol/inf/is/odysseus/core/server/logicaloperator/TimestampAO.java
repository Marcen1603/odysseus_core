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

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.mep.MEP;

@LogicalOperator(minInputPorts = 1, maxInputPorts = 1, name = "Timestamp", doc = "This Operator can be used to update the timestamp information in the meta data part. Be careful because this may lead undefined semantics", category = {
		LogicalOperatorCategory.BASE })
public class TimestampAO extends UnaryLogicalOp {
	private static final long serialVersionUID = -467482177921504749L;

	static List<String> locales = null;

	// To be used if timestamps are given in one attribute
	// private SDFAttribute startTimestamp;
	// private SDFAttribute endTimestamp;
	// expressions to calculate timestamps
	private SDFExpression startExpression;
	private SDFExpression endExpression;

	// an optional String representation of the date format
	private String dateFormat;
	private Locale locale;
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
	private long offset;

	private boolean isUsingSystemTime;
	private boolean isUsingNoTime;

	private boolean clearEnd;

	public TimestampAO(TimestampAO ao) {
		super(ao);		
		// setStartTimestamp(ao.startTimestamp);
		// setEndTimestamp(ao.endTimestamp);
		setClearEnd(ao.clearEnd);
		setDateFormat(ao.dateFormat);
		this.locale = ao.locale;
		setTimezone(ao.timezone);
		setStartTimestampYear(ao.startTimestampYear);
		setStartTimestampMonth(ao.startTimestampMonth);
		setStartTimestampDay(ao.startTimestampDay);
		setStartTimestampHour(ao.startTimestampHour);
		setStartTimestampMinute(ao.startTimestampMinute);
		setStartTimestampSecond(ao.startTimestampSecond);
		setStartTimestampMillisecond(ao.startTimestampMillisecond);
		setFactor(ao.factor);
		setOffset(ao.offset);
		this.isUsingSystemTime = ao.isUsingSystemTime;
		this.isUsingNoTime = ao.isUsingNoTime;
		this.startExpression = ao.startExpression != null ? ao.startExpression.clone() : null;
		this.endExpression = ao.endExpression != null ? ao.endExpression.clone() : null;
	}

	public TimestampAO() {
		// startTimestamp = null;
		// endTimestamp = null;
		startExpression = null;
		endExpression = null;
		isUsingSystemTime = true;
		isUsingNoTime = false;
		clearEnd = false;
		// per default punctuations should be suppressed, can be release by parameter
		setSuppressPunctuations(true);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TimestampAO(this);
	}


	@Parameter(type = NamedExpressionParameter.class, name = "START", aliasname = "StartExpression", optional = true, doc = "Expression to calculate the start time. Could be an attribute, too.")
	public void setStartExpression2(NamedExpression expression) {
		this.startExpression = expression.expression;
		this.isUsingSystemTime = false;
	}

	public SDFExpression getStartExpression() {
		return startExpression;
	}

	@Parameter(type = NamedExpressionParameter.class, name = "END", aliasname = "EndExpression", optional = true, doc = "Expression to calculate the start time. Could be an attribute, too.")
	public void setEndExpression2(NamedExpression expression) {
		this.endExpression = expression.expression;
		this.isUsingSystemTime = false;
	}

	public SDFExpression getEndExpression() {
		return endExpression;
	}

	@Parameter(type = BooleanParameter.class, name = "clearEnd", isList = false, optional = true, doc = "If set to true, the end timestamp will be set to infinity")
	public void setClearEnd(boolean clearEnd) {
		this.clearEnd = clearEnd;
		addParameterInfo("CLEAREND", "'" + Boolean.valueOf(clearEnd) + "'");
	}

	public boolean isClearEnd() {
		return clearEnd;
	}

	public boolean hasStartTimestampExpression() {
		return this.startExpression != null;
	}

	public boolean hasEndTimestampExpression() {
		return this.endExpression != null;
	}

	public boolean isUsingSystemTime() {
		return this.isUsingSystemTime;
	}

	@Parameter(type = BooleanParameter.class, name = "SystemTime", isList = false, optional = true, doc = "If set to true, system time instead of application time will be used")
	public void setIsUsingSystemTime(boolean value) {
		this.isUsingSystemTime = value;
	}

	public boolean getIsUsingSystemTime() {
		return this.isUsingSystemTime;
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

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "YEAR", isList = false, optional = true, doc = "The name of the attribute for the year part of the start timestamp for application time")
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

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "MONTH", isList = false, optional = true, doc = "The name of the attribute for the month part of the start timestamp for application time")
	public void setStartTimestampMonth(SDFAttribute startTimestampMonth) {
		addParameterInfoIfNeeded("MONTH", startTimestampMonth);
		this.startTimestampMonth = startTimestampMonth;
	}

	public SDFAttribute getStartTimestampDay() {
		return startTimestampDay;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "DAY", isList = false, optional = true, doc = "The name of the attribute for the day part of the start timestamp for application time")
	public void setStartTimestampDay(SDFAttribute startTimestampDay) {
		addParameterInfoIfNeeded("DAY", startTimestampDay);
		this.startTimestampDay = startTimestampDay;
	}

	public SDFAttribute getStartTimestampHour() {
		return startTimestampHour;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "HOUR", isList = false, optional = true, doc = "The name of the attribute for the hour part of the start timestamp for application time")
	public void setStartTimestampHour(SDFAttribute startTimestampHour) {
		addParameterInfoIfNeeded("HOUR", startTimestampHour);
		this.startTimestampHour = startTimestampHour;
	}

	public SDFAttribute getStartTimestampMinute() {
		return startTimestampMinute;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "MINUTE", isList = false, optional = true, doc = "The name of the attribute for the minute part of the start timestamp for application time")
	public void setStartTimestampMinute(SDFAttribute startTimestampMinute) {
		addParameterInfoIfNeeded("MINUTE", startTimestampMinute);
		this.startTimestampMinute = startTimestampMinute;
	}

	public SDFAttribute getStartTimestampSecond() {
		return startTimestampSecond;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "SECOND", isList = false, optional = true, doc = "The name of the attribute for the second part of the start timestamp for application time")
	public void setStartTimestampSecond(SDFAttribute startTimestampSecond) {
		addParameterInfoIfNeeded("SECOND", startTimestampSecond);
		this.startTimestampSecond = startTimestampSecond;
	}

	public SDFAttribute getStartTimestampMillisecond() {
		return startTimestampMillisecond;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "MILLISECOND", isList = false, optional = true, doc = "The name of the attribute for the millisecond part of the start timestamp for application time")
	public void setStartTimestampMillisecond(SDFAttribute startTimestampMillisecond) {
		addParameterInfoIfNeeded("MILLISECOND", startTimestampMillisecond);
		this.startTimestampMillisecond = startTimestampMillisecond;
	}

	public int getFactor() {
		return factor;
	}

	@Parameter(type = IntegerParameter.class, name = "FACTOR", isList = false, optional = true, doc = "A multiplication factor for a single attributed timestamp to calc milliseconds (e.g. if input is seconds, use 1000 here)")
	public void setFactor(int factor) {
		addParameterInfo("FACTOR", Integer.valueOf(factor));
		this.factor = factor;
	}

	public long getOffset() {
		return offset;
	}

	@Parameter(type = LongParameter.class, name = "OFFSET", isList = false, optional = true, doc = "An offset in milliseconds that will be added to the timestmap")
	public void setOffset(long offset) {
		addParameterInfo("OFFSET", offset);
		this.offset = offset;
	}

	@Override
	public String toString() {
		return super.toString() + "s :" + this.startExpression + " e:" + endExpression + " " + isUsingSystemTime + " "
				+ isUsingNoTime + " " + clearEnd;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	@Parameter(type = StringParameter.class, name = "dateFormat", isList = false, optional = true, doc = "If using a string for date information, use this format to parse the date (in Java syntax).")
	public void setDateFormat(String dateFormat) {
		if (!Strings.isNullOrEmpty(dateFormat)) {
			addParameterInfo("DATEFORMAT", "'" + dateFormat + "'");
		} else {
			removeParameterInfo("DATEFORMAT");
		}

		this.dateFormat = dateFormat;
	}

	@Parameter(type = StringParameter.class, name = "locale", optional = true, doc = "Interprete the date string with this locale")
	public void setLocaleStr(String localeStr) {
		this.locale = Locale.forLanguageTag(localeStr);
	}

	public String getLocaleStr() {
		return this.locale != null ? this.locale.toString() : null;
	}

	public List<String> getLocales() {
		if (locales == null) {
			for (Locale l : Locale.getAvailableLocales()) {
				locales = new LinkedList<>();
				locales.add(l.getDisplayName());
			}
		}
		return locales;
	}

	public Locale getLocale() {
		return locale;
	}

	public String getTimezone() {
		return timezone;
	}

	@Parameter(type = StringParameter.class, name = "timezone", isList = false, optional = true, doc = "The timezone in Java syntax.")
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

	public void setStartTimestamp(SDFAttribute attr) {
		setStartExpression2(namedExpressionFromAttribute(attr));
	}

	public void setEndTimestamp(SDFAttribute attr) {
		setEndExpression2(namedExpressionFromAttribute(attr));
	}

	private NamedExpression namedExpressionFromAttribute(SDFAttribute attr) {
		return new NamedExpression("",new SDFExpression("", attr.getAttributeName(), null, MEP.getInstance(),
				AggregateFunctionBuilderRegistry.getAggregatePattern()));
	}


}
