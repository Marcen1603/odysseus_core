package de.uniol.inf.is.odysseus.server.keyvalue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.AbstractMetadataUpdater;

/**
 * @author Jan Soeren Schwarz
 */
public class KeyValueTimestampAttributeTimeIntervalMFactory extends
AbstractMetadataUpdater<ITimeInterval, KeyValueObject<? extends ITimeInterval>> {


	// Time is given in base format
	final private String startAttrKey;
	final private String endAttrKey;
	final private SimpleDateFormat df;

	// Time is separated to different attributes
	final private String startTimestampYearKey;
	final private String startTimestampMonthKey;
	final private String startTimestampDayKey;
	final private String startTimestampHourKey;
	final private String startTimestampMinuteKey;
	final private String startTimestampSecondKey;
	final private String startTimestampMillisecondKey;
	final private int factor;
	final private long offset;

	final private boolean clearEnd;
	final private TimeZone timezone;

	public KeyValueTimestampAttributeTimeIntervalMFactory(String startAttrKey,
			String endAttrKey, boolean clearEnd, String dateFormat,
			String timezone, Locale locale, int factor, long offset) {
		this.startAttrKey = startAttrKey;
		this.endAttrKey = endAttrKey;

		if (timezone != null) {
			this.timezone = TimeZone.getTimeZone(timezone);
		} else {
			this.timezone = TimeZone.getTimeZone("UTC");
		}

		if (dateFormat != null) {
			if (locale != null) {
				df = new SimpleDateFormat(dateFormat, locale);
			} else {
				df = new SimpleDateFormat(dateFormat);
			}
			df.setTimeZone(this.timezone);
		} else {
			df = null;
		}

		startTimestampYearKey = null;
		startTimestampMonthKey = null;
		startTimestampDayKey = null;
		startTimestampHourKey = null;
		startTimestampMinuteKey = null;
		startTimestampSecondKey = null;
		startTimestampMillisecondKey = null;

		this.factor = factor;
		this.offset = offset;
		this.clearEnd = clearEnd;
	}

	public KeyValueTimestampAttributeTimeIntervalMFactory(
			String startTimestampYear, String startTimestampMonth,
			String startTimestampDay, String startTimestampHour,
			String startTimestampMinute, String startTimestampSecond,
			String startTimestampMillisecond, int factor, boolean clearEnd,
			String timezone) {
		this.startAttrKey = null;
		this.endAttrKey = null;

		this.startTimestampYearKey =
				((startTimestampYear != null && startTimestampYear.length() > 0) ?
						startTimestampYear : null);
		this.startTimestampMonthKey =
				((startTimestampMonth != null && startTimestampMonth.length() > 0) ?
						startTimestampMonth : null);
		this.startTimestampDayKey =
				((startTimestampDay != null && startTimestampDay.length() > 0) ?
						startTimestampDay : null);
		this.startTimestampHourKey =
				((startTimestampHour != null && startTimestampHour.length() > 0) ?
						startTimestampHour : null);
		this.startTimestampMinuteKey =
				((startTimestampMinute != null && startTimestampMinute.length() > 0) ?
						startTimestampMinute : null);
		this.startTimestampSecondKey =
				((startTimestampSecond != null && startTimestampSecond.length() > 0) ?
						startTimestampSecond : null);
		this.startTimestampMillisecondKey =
				((startTimestampMillisecond != null && startTimestampMillisecond.length() > 0) ?
						startTimestampMillisecond : null);

		this.factor = factor;
		this.offset = 0;

		this.clearEnd = clearEnd;

		df = null;
		if (timezone != null) {
			this.timezone = TimeZone.getTimeZone(timezone);
		} else {
			this.timezone = TimeZone.getTimeZone("UTC");
		}
	}

	@Override
	public void updateMetadata(KeyValueObject<? extends ITimeInterval> inElem) {
		if (clearEnd) {
			inElem.getMetadata().setEnd(PointInTime.getInfinityTime());
		}

		if (startTimestampYearKey != null) {

			Calendar cal = Calendar.getInstance(this.timezone);
			int year = inElem.getAttribute(startTimestampYearKey);
			int month = (Integer) (startTimestampMonthKey != null ? inElem
					.getAttribute(startTimestampMonthKey) : 1);
			int day = (Integer) (startTimestampDayKey != null ? inElem
					.getAttribute(startTimestampDayKey) : 1);
			int hour = (Integer) (startTimestampHourKey != null ? inElem
					.getAttribute(startTimestampHourKey) : 0);
			int minute = (Integer) (startTimestampMinuteKey != null ? inElem
					.getAttribute(startTimestampMinuteKey) : 0);
			int second = (Integer) (startTimestampSecondKey != null ? inElem
					.getAttribute(startTimestampSecondKey) : 0);
			cal.set(year, month - 1, day, hour, minute, second);

			long ts = cal.getTimeInMillis();
			if (startTimestampMillisecondKey != null) {
				ts = ts	+ ((Long) inElem.getAttribute(startTimestampMillisecondKey));
			}
			if (factor > 0) {
				ts *= factor;
			}

			ts+=offset;

			PointInTime start = new PointInTime(ts);
			inElem.getMetadata().setStart(start);

		} else {
			if (startAttrKey != null) {
				PointInTime start = extractTimestamp(inElem, startAttrKey);
				inElem.getMetadata().setStart(start);
			}
			if (endAttrKey != null) {
				PointInTime end = extractTimestamp(inElem, endAttrKey);
				inElem.getMetadata().setEnd(end);
			}
		}

	}

	private PointInTime extractTimestamp(KeyValueObject<? extends ITimeInterval> inElem,
			String attrKey) {
		Number timeN;
		if (df != null) {
			String timeString = (String) inElem.getAttribute(attrKey);
			try {
				timeN = df.parse(timeString).getTime();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Date cannot be parsed! "
						+ timeString + " with " + df.toPattern());
			}
		} else {
			try {
				timeN = inElem.getNumberAttribute(attrKey);
			}catch(Exception e) {
				timeN = Long.parseLong(inElem.getAttribute(attrKey));
			}
		}
		if (factor != 0){
			timeN = timeN.longValue() * factor;
		}
		if (offset > 0){
			timeN= timeN.longValue() + offset;
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KeyValueTimestampAttributeTimeIntervalMFactory other = (KeyValueTimestampAttributeTimeIntervalMFactory) obj;
		if (clearEnd != other.clearEnd)
			return false;
		if (df == null) {
			if (other.df != null)
				return false;
		} else if (!df.equals(other.df))
			return false;
		if (timezone == null) {
			if (other.timezone != null)
				return false;
		} else if (!timezone.equals(other.timezone))
			return false;
		if (endAttrKey != other.endAttrKey)
			return false;
		if (factor != other.factor)
			return false;
		if (startAttrKey != other.startAttrKey)
			return false;
		if (startTimestampDayKey != other.startTimestampDayKey)
			return false;
		if (startTimestampHourKey != other.startTimestampHourKey)
			return false;
		if (startTimestampMillisecondKey != other.startTimestampMillisecondKey)
			return false;
		if (startTimestampMinuteKey != other.startTimestampMinuteKey)
			return false;
		if (startTimestampMonthKey != other.startTimestampMonthKey)
			return false;
		if (startTimestampSecondKey != other.startTimestampSecondKey)
			return false;
		if (startTimestampYearKey != other.startTimestampYearKey)
			return false;
		return true;
	}

	public String getStartTimestampYearKey() {
		return startTimestampYearKey;
	}

	public String getStartTimestampMonthKey() {
		return startTimestampMonthKey;
	}

	public String getStartTimestampDayKey() {
		return startTimestampDayKey;
	}

	public String getStartTimestampHourKey() {
		return startTimestampHourKey;
	}

	public String getStartTimestampMinuteKey() {
		return startTimestampMinuteKey;
	}

	public String getStartTimestampSecondKey() {
		return startTimestampSecondKey;
	}

	public String getStartTimestampMillisecondKey() {
		return startTimestampMillisecondKey;
	}

	public int getFactor() {
		return factor;
	}

	public boolean isClearEnd() {
		return clearEnd;
	}

	public TimeZone getTimezone() {
		return timezone;
	}
}
