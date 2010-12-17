package de.uniol.inf.is.odysseus.planmanagement.optimization.plan;

import java.util.Iterator;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.CSVToString;
import de.uniol.inf.is.odysseus.collection.Pair;

public class ScheduleMeta implements CSVToString{
	private long lastSchedule;
	private long lastDiff;
	private long inTimeCount;
	private double rate;
	private long allSchedulings;
	
	final private LinkedList<Pair<Long, Boolean>> history;

	public ScheduleMeta(long lastSchedule) {
		super();
		this.lastSchedule = lastSchedule;
		this.inTimeCount = 0;
		this.allSchedulings = 0;
		this.lastDiff = 0;
		history = new LinkedList<Pair<Long, Boolean>>();
	}

	public ScheduleMeta(ScheduleMeta other) {
		this.lastSchedule = other.lastSchedule;
		this.inTimeCount = other.inTimeCount;
		this.allSchedulings = other.allSchedulings;
		this.lastDiff = other.lastDiff;
		history = new LinkedList<Pair<Long, Boolean>>(other.history);
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

	private long getNow() {
		return System.currentTimeMillis();
	}

	public void drainHistory(long before) {
		synchronized (history) {
			Iterator<Pair<Long, Boolean>> iter = history.iterator();
			while (iter.hasNext()) {
				Pair<Long, Boolean> entry = iter.next();
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

}