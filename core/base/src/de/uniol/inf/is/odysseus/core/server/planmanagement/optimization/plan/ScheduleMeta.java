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
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.plan;

import java.util.Iterator;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.ICSVToString;
import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;

public class ScheduleMeta implements ICSVToString{
	private long lastSchedule;
	private long lastDiff;
	private long inTimeCount;
	private double rate;
	private long allSchedulings;
	
	final private LinkedList<IPair<Long, Boolean>> history;

	public ScheduleMeta(long lastSchedule) {
		super();
		this.lastSchedule = lastSchedule;
		this.inTimeCount = 0;
		this.allSchedulings = 0;
		this.lastDiff = 0;
		history = new LinkedList<IPair<Long, Boolean>>();
	}

	public ScheduleMeta(ScheduleMeta other) {
		this.lastSchedule = other.lastSchedule;
		this.inTimeCount = other.inTimeCount;
		this.allSchedulings = other.allSchedulings;
		this.lastDiff = other.lastDiff;
		history = new LinkedList<IPair<Long, Boolean>>(other.history);
	}

	public void csvPrint(StringBuffer toPrint) {
		toPrint.append(lastDiff);
		toPrint.append(";").append(inTimeCount);
		toPrint.append(";").append(allSchedulings);
		if (allSchedulings > 0) {
			toPrint.append(";").append(
					Math.round((inTimeCount * 1.0 / allSchedulings) * 100));
		} else {
			toPrint.append(";0");
		}
		toPrint.append(";").append(history.size());
	}
	
	@Override
	public String csvToString() {
		StringBuffer ret = new StringBuffer();
		csvPrint(ret);
		return ret.toString();
	}
	
	@Override
	public String getCSVHeader() {
		return "lastDiff;inTimeCount;allSchedulings;factor;historySize";
	}

	private static long getNow() {
		return System.currentTimeMillis();
	}

	public void drainHistory(long before) {
		synchronized (history) {
			Iterator<IPair<Long, Boolean>> iter = history.iterator();
			while (iter.hasNext()) {
				IPair<Long, Boolean> entry = iter.next();
				if (entry.getE1().longValue() < getNow() - before) {
					iter.remove();
					if (entry.getE2()) {
						inTimeCount--;
					}
					allSchedulings--;
				} else {
					break; // Timestamps are sorted
				}
			}
			calcRate();
		}
	}

	public long scheduleDone(long minTime) {

		synchronized (history) {
			long now = getNow();
			long timeSinceLastSchedule = now - lastSchedule; 
			boolean inTime = minTime > timeSinceLastSchedule;
			if (inTime) {
				inTimeCount++;
			}
			this.lastSchedule = now;
			allSchedulings++;
			history.add(new Pair<Long, Boolean>(now, inTime));
			lastDiff = timeSinceLastSchedule;
			calcRate();
			return timeSinceLastSchedule;
		}
	}

	public double calcPotentialRate(long minTimePeriod) {
		double potentialInTimeSchedulings = ((getNow() - lastSchedule) < minTimePeriod ? (inTimeCount + 1)
				: inTimeCount) * 1.0;
		return allSchedulings > 0 ? potentialInTimeSchedulings * 1.0
				/ (allSchedulings + 1 * 1.0) // increase potential overall
												// count!
		: 0;
	}

	public void calcRate() {
		rate = allSchedulings > 0 ? inTimeCount * 1.0 / (allSchedulings * 1.0)
				: 0;
	}
	
	public double getRate(){
		return rate;
	}
	
	public long getLastDiff() {
		return lastDiff;
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		long now = System.currentTimeMillis();
		ret.append("last=" + (now - lastSchedule));
		ret.append(" " + inTimeCount);
		ret.append("/").append(allSchedulings);
		if (allSchedulings > 0) {
			ret.append("=").append(
					Math.round((inTimeCount * 1.0 / allSchedulings) * 100));
		} else {
			ret.append("=0");
		}
		ret.append(" h#").append(history.size());
		return ret.toString();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.ICSVToString#csvToString(boolean)
	 */
	@Override
	public String csvToString(boolean withMetada) {
		return this.csvToString();
	}

}