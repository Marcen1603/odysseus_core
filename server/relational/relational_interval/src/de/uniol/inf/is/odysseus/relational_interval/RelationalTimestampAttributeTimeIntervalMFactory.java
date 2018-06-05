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
package de.uniol.inf.is.odysseus.relational_interval;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.AbstractMetadataUpdater;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class RelationalTimestampAttributeTimeIntervalMFactory<M extends ITimeInterval>
		extends AbstractMetadataUpdater<M, Tuple<M>> {

	// Time is given in base format
	final private int startAttrPos;
	final private int endAttrPos;

	final private RelationalExpression<M> startExpression;
	final private RelationalExpression<M> endExpression;
	
	final private DateTimeFormatter df;
	final private String df_string;
	final private Locale loc;

	// Time is separated to different attributes
	final private int startTimestampYearPos;
	final private int startTimestampMonthPos;
	final private int startTimestampDayPos;
	final private int startTimestampHourPos;
	final private int startTimestampMinutePos;
	final private int startTimestampSecondPos;
	final private int startTimestampMillisecondPos;
	final private int factor;
	final private long offset;

	final private boolean clearEnd;
	final private ZoneId timezone;

	public RelationalTimestampAttributeTimeIntervalMFactory(int startAttrPos, int endAttrPos, boolean clearEnd,
			String dateFormat, String timezone, Locale locale, int factor, long offset, RelationalExpression<M> startExpression,
			RelationalExpression<M> endExpression) {
		this.startAttrPos = startAttrPos;
		this.endAttrPos = endAttrPos;
		this.startExpression = startExpression;
		this.endExpression = endExpression;

		if (timezone != null) {
			this.timezone = ZoneId.of(timezone);
		} else {
			this.timezone = ZoneId.of("UTC");
		}

		df_string = dateFormat;
		loc = locale;
		if (dateFormat != null) {
			if (locale != null) {
				df = DateTimeFormatter.ofPattern(dateFormat, locale);
			} else {
				df = DateTimeFormatter.ofPattern(dateFormat);
			}
		} else {
			df = null;
		}

		startTimestampYearPos = -1;
		startTimestampMonthPos = -1;
		startTimestampDayPos = -1;
		startTimestampHourPos = -1;
		startTimestampMinutePos = -1;
		startTimestampSecondPos = -1;
		startTimestampMillisecondPos = -1;
		this.factor = factor;
		this.offset = offset;
		this.clearEnd = clearEnd;
	}

	public RelationalTimestampAttributeTimeIntervalMFactory(int startTimestampYear, int startTimestampMonth,
			int startTimestampDay, int startTimestampHour, int startTimestampMinute, int startTimestampSecond,
			int startTimestampMillisecond, int factor, boolean clearEnd, String timezone) {
		this.startAttrPos = -1;
		this.endAttrPos = -1;
		
		this.startExpression = null;
		this.endExpression = null;

		this.startTimestampYearPos = startTimestampYear;
		this.startTimestampMonthPos = startTimestampMonth;
		this.startTimestampDayPos = startTimestampDay;
		this.startTimestampHourPos = startTimestampHour;
		this.startTimestampMinutePos = startTimestampMinute;
		this.startTimestampSecondPos = startTimestampSecond;
		this.startTimestampMillisecondPos = startTimestampMillisecond;
		this.factor = factor;
		this.offset = 0;

		this.clearEnd = clearEnd;

		df = null;
		df_string = null;
		loc = null;
		if (timezone != null) {
			this.timezone = ZoneId.of(timezone);
		} else {
			this.timezone = ZoneId.of("UTC");
		}
	}

	@Override
	public void updateMetadata(Tuple<M> inElem) {
		if (clearEnd) {
			inElem.getMetadata().setEnd(PointInTime.getInfinityTime());
		}

		if (startTimestampYearPos >= 0) {

			// LocalDateTime cal = Calendar.getInstance(this.timezone);
			int year = inElem.getAttribute(startTimestampYearPos);
			int month = (Integer) (startTimestampMonthPos > 0 ? inElem.getAttribute(startTimestampMonthPos) : 1);
			int day = (Integer) (startTimestampDayPos > 0 ? inElem.getAttribute(startTimestampDayPos) : 1);
			int hour = (Integer) (startTimestampHourPos > 0 ? inElem.getAttribute(startTimestampHourPos) : 0);
			int minute = (Integer) (startTimestampMinutePos > 0 ? inElem.getAttribute(startTimestampMinutePos) : 0);
			int second = (Integer) (startTimestampSecondPos > 0 ? inElem.getAttribute(startTimestampSecondPos) : 0);
			LocalDateTime ldt = LocalDateTime.of(year, month, day, hour, minute, second);
			ZonedDateTime zdt = ldt.atZone(timezone);
			long ts = zdt.toInstant().toEpochMilli();
			// Round to millis because they cannot be set with cal.set
			ts = (ts / 1000) * 1000;

			if (startTimestampMillisecondPos > 0) {
				ts = ts + ((Long) inElem.getAttribute(startTimestampMillisecondPos));
			}
			if (factor > 0) {
				ts *= factor;
			}

			ts += offset;

			PointInTime start = new PointInTime(ts);
			inElem.getMetadata().setStart(start);

		} else {
			if (startAttrPos >= 0) {
				PointInTime start = extractTimestamp(inElem, startAttrPos);
				inElem.getMetadata().setStart(start);
			}
			if (endAttrPos >= 0) {
				PointInTime end = extractTimestamp(inElem, endAttrPos);
				inElem.getMetadata().setEnd(end);
			}
			if (startExpression != null) {
				try {
					Object expr = startExpression.evaluate( inElem,null, null);
					if (expr != null) {
						inElem.getMetadata().setStart(new PointInTime(((Number)expr).longValue()));
					}
				} catch (Exception e) {
					// Warn handling as in map
				}
			}
			if (endExpression != null) {
				try {
					Object expr = endExpression.evaluate( inElem,null, null);
					if (expr != null) {
						inElem.getMetadata().setEnd(new PointInTime(((Number)expr).longValue()));
					}
				} catch (Exception e) {
					// Warn handling as in map
				}
			}

		}

	}

	private PointInTime extractTimestamp(Tuple<? extends ITimeInterval> inElem, int attrPos) {
		Number timeN;
		if (df != null) {
			String timeString = (String) inElem.getAttribute(attrPos);
			try {
				LocalDateTime ldt = LocalDateTime.parse(timeString, df);
				ZonedDateTime zdt = ldt.atZone(timezone);

				timeN = zdt.toInstant().toEpochMilli();

			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Date cannot be parsed! " + timeString + " with " + df);
			}
		} else {
			timeN = (Number) inElem.getAttribute(attrPos);
		}
		if (factor != 0) {
			timeN = timeN.longValue() * factor;
		}
		if (offset > 0) {
			timeN = timeN.longValue() + offset;
		}

		PointInTime time = null;
		if (timeN == null || timeN.longValue() == -1) {
			time = PointInTime.getInfinityTime();
		} else {
			time = new PointInTime(timeN);
		}
		return time;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (clearEnd ? 1231 : 1237);
		result = prime * result + ((df == null) ? 0 : df.hashCode());
		result = prime * result + ((df_string == null) ? 0 : df_string.hashCode());
		result = prime * result + endAttrPos;
		result = prime * result + ((endExpression == null) ? 0 : endExpression.hashCode());
		result = prime * result + factor;
		result = prime * result + ((loc == null) ? 0 : loc.hashCode());
		result = prime * result + (int) (offset ^ (offset >>> 32));
		result = prime * result + startAttrPos;
		result = prime * result + ((startExpression == null) ? 0 : startExpression.hashCode());
		result = prime * result + startTimestampDayPos;
		result = prime * result + startTimestampHourPos;
		result = prime * result + startTimestampMillisecondPos;
		result = prime * result + startTimestampMinutePos;
		result = prime * result + startTimestampMonthPos;
		result = prime * result + startTimestampSecondPos;
		result = prime * result + startTimestampYearPos;
		result = prime * result + ((timezone == null) ? 0 : timezone.hashCode());
		return result;
	}

	


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RelationalTimestampAttributeTimeIntervalMFactory other = (RelationalTimestampAttributeTimeIntervalMFactory) obj;
		if (clearEnd != other.clearEnd)
			return false;
		if (df == null) {
			if (other.df != null)
				return false;
		} else if (!df.equals(other.df))
			return false;
		if (df_string == null) {
			if (other.df_string != null)
				return false;
		} else if (!df_string.equals(other.df_string))
			return false;
		if (endAttrPos != other.endAttrPos)
			return false;
		if (endExpression == null) {
			if (other.endExpression != null)
				return false;
		} else if (!endExpression.equals(other.endExpression))
			return false;
		if (factor != other.factor)
			return false;
		if (loc == null) {
			if (other.loc != null)
				return false;
		} else if (!loc.equals(other.loc))
			return false;
		if (offset != other.offset)
			return false;
		if (startAttrPos != other.startAttrPos)
			return false;
		if (startExpression == null) {
			if (other.startExpression != null)
				return false;
		} else if (!startExpression.equals(other.startExpression))
			return false;
		if (startTimestampDayPos != other.startTimestampDayPos)
			return false;
		if (startTimestampHourPos != other.startTimestampHourPos)
			return false;
		if (startTimestampMillisecondPos != other.startTimestampMillisecondPos)
			return false;
		if (startTimestampMinutePos != other.startTimestampMinutePos)
			return false;
		if (startTimestampMonthPos != other.startTimestampMonthPos)
			return false;
		if (startTimestampSecondPos != other.startTimestampSecondPos)
			return false;
		if (startTimestampYearPos != other.startTimestampYearPos)
			return false;
		if (timezone == null) {
			if (other.timezone != null)
				return false;
		} else if (!timezone.equals(other.timezone))
			return false;
		return true;
	}

	public static void main(String[] args) throws ParseException {
		String test = "2012-02-22T16:50:34.2669408+00:00";
		String form = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSzzz";
		DateTimeFormatter df = DateTimeFormatter.ofPattern(form);

		LocalDateTime ldt = LocalDateTime.parse(test, df);
		ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));

		long timeN = zdt.toInstant().toEpochMilli();
		System.err.println(timeN);

	}

	public int getStartTimestampYearPos() {
		return startTimestampYearPos;
	}

	public int getStartTimestampMonthPos() {
		return startTimestampMonthPos;
	}

	public int getStartTimestampDayPos() {
		return startTimestampDayPos;
	}

	public int getStartTimestampHourPos() {
		return startTimestampHourPos;
	}

	public int getStartTimestampMinutePos() {
		return startTimestampMinutePos;
	}

	public int getStartTimestampSecondPos() {
		return startTimestampSecondPos;
	}

	public int getStartTimestampMillisecondPos() {
		return startTimestampMillisecondPos;
	}

	public int getFactor() {
		return factor;
	}

	public boolean isClearEnd() {
		return clearEnd;
	}

	public ZoneId getTimezone() {
		return timezone;
	}

	public int getStartAttrPos() {
		return startAttrPos;
	}

	public int getEndAttrPos() {
		return endAttrPos;
	}

	public String getDateFormat() {
		return df_string;
	}

	public Locale getLocale() {
		return loc;
	}

	public long getOffset() {
		return offset;
	}

}