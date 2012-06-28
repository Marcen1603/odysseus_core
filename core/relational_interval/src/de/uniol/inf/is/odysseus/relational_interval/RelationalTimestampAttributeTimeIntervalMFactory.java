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
package de.uniol.inf.is.odysseus.relational_interval;

import java.util.Calendar;
import java.util.GregorianCalendar;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.AbstractMetadataUpdater;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class RelationalTimestampAttributeTimeIntervalMFactory extends
		AbstractMetadataUpdater<ITimeInterval, Tuple<? extends ITimeInterval>> {

	// Time is given in base format
	final private int startAttrPos;
	final private int endAttrPos;

	// Time is separated to different attributes
	final private int startTimestampYearPos;
	final private int startTimestampMonthPos;
	final private int startTimestampDayPos;
	final private int startTimestampHourPos;
	final private int startTimestampMinutePos;
	final private int startTimestampSecondPos;
	final private int startTimestampMillisecondPos;
	final private int factor;

	final private boolean clearEnd;

	public RelationalTimestampAttributeTimeIntervalMFactory(int startAttrPos,
			int endAttrPos, boolean clearEnd) {
		this.startAttrPos = startAttrPos;
		this.endAttrPos = endAttrPos;

		startTimestampYearPos = -1;
		startTimestampMonthPos = -1;
		startTimestampDayPos = -1;
		startTimestampHourPos = -1;
		startTimestampMinutePos = -1;
		startTimestampSecondPos = -1;
		startTimestampMillisecondPos = -1;
		factor = 0;

		this.clearEnd = clearEnd;
	}

	public RelationalTimestampAttributeTimeIntervalMFactory(
			int startTimestampYear, int startTimestampMonth,
			int startTimestampDay, int startTimestampHour,
			int startTimestampMinute, int startTimestampSecond,
			int startTimestampMillisecond, int factor, boolean clearEnd) {
		this.startAttrPos = -1;
		this.endAttrPos = -1;

		this.startTimestampYearPos = startTimestampYear;
		this.startTimestampMonthPos = startTimestampMonth;
		this.startTimestampDayPos = startTimestampDay;
		this.startTimestampHourPos = startTimestampHour;
		this.startTimestampMinutePos = startTimestampMinute;
		this.startTimestampSecondPos = startTimestampSecond;
		this.startTimestampMillisecondPos = startTimestampMillisecond;
		this.factor = factor;

		this.clearEnd = clearEnd;

	}

	public RelationalTimestampAttributeTimeIntervalMFactory(int startAttrPos,
			boolean clearEnd) {
		this(startAttrPos, -1, clearEnd);
	}

	@Override
	public void updateMetadata(Tuple<? extends ITimeInterval> inElem) {
		if (clearEnd) {
			inElem.getMetadata().setEnd(PointInTime.getInfinityTime());
		}

		if (startTimestampYearPos > 0) {

			Calendar cal = new GregorianCalendar();
			int year = inElem.getAttribute(startTimestampYearPos);
			int month = inElem.getAttribute(startTimestampMonthPos);
			int day = (Integer) (startTimestampDayPos > 0 ? inElem
					.getAttribute(startTimestampDayPos) : 1);
			int hour = (Integer) (startTimestampHourPos > 0 ? inElem
					.getAttribute(startTimestampHourPos) : 0);
			int minute = (Integer) (startTimestampMinutePos > 0 ? inElem
					.getAttribute(startTimestampMinutePos) : 0);
			int second = (Integer) (startTimestampSecondPos > 0 ? inElem
					.getAttribute(startTimestampSecondPos) : 0);
			cal.set(year, month, day, hour, minute, second);
			long ts = cal.getTimeInMillis();
			if (startTimestampMillisecondPos > 0) {
				ts = ts
						+ ((Long) inElem
								.getAttribute(startTimestampMillisecondPos));
			}
			if (factor > 0) {
				ts *= factor;
			}

			PointInTime start = new PointInTime(ts);
			inElem.getMetadata().setStart(start);

		} else {
			PointInTime start = extractTimestamp(inElem, startAttrPos);

			inElem.getMetadata().setStart(start);
			if (endAttrPos > 0) {
				PointInTime end = extractTimestamp(inElem, endAttrPos);
				inElem.getMetadata().setEnd(end);
			}
		}

	}

	private static PointInTime extractTimestamp(
			Tuple<? extends ITimeInterval> inElem, int attrPos) {
		Number timeN = (Number) inElem.getAttribute(attrPos);
		PointInTime time = null;
		if (timeN.longValue() == -1) {
			time = PointInTime.getInfinityTime();
		} else {
			time = new PointInTime(timeN);
		}
		return time;
	}

}