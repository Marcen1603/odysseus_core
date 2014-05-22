package de.uniol.inf.is.odysseus.systemload;

import java.text.NumberFormat;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

public class TimeIntervalSystemLoad extends SystemLoad implements ITimeInterval, ITimeIntervalSystemLoad {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[]{ 
		ITimeInterval.class, ISystemLoad.class
	};
	
	private TimeInterval timeInterval = new TimeInterval();
	
	public TimeIntervalSystemLoad() {
		super();
	}

	public TimeIntervalSystemLoad( TimeIntervalSystemLoad copy ) {
		super(copy);
		
		timeInterval = copy.timeInterval.clone();
	}
	
	@Override
	public int compareTo(ITimeInterval o) {
		return timeInterval.compareTo(o);
	}

	@Override
	public PointInTime getStart() {
		return timeInterval.getStart();
	}

	@Override
	public PointInTime getEnd() {
		return timeInterval.getEnd();
	}

	@Override
	public void setStart(PointInTime point) {
		timeInterval.setStart(point);
	}

	@Override
	public void setEnd(PointInTime point) {
		timeInterval.setEnd(point);
	}

	@Override
	public void setStartAndEnd(PointInTime start, PointInTime end) {
		timeInterval.setStartAndEnd(start, end);
	}

	@Override
	public String toString() {
		return "( i= " + timeInterval.toString() + " | sysload= " + super.toString() + " )";
	}

	@Override
	public String getCSVHeader(char delimiter) {
		return timeInterval.getCSVHeader(delimiter) + delimiter + super.getCSVHeader(delimiter);
	}

	@Override
	public String csvToString(char delimiter, Character textSeperator, NumberFormat floatingFormatter, NumberFormat numberFormatter, boolean withMetadata) {
		return timeInterval.csvToString(delimiter, textSeperator, floatingFormatter, numberFormatter, withMetadata) + delimiter + super.csvToString(delimiter, textSeperator, floatingFormatter, numberFormatter, withMetadata);
	}
	
	@Override
	public ITimeIntervalSystemLoad clone() {
		return new TimeIntervalSystemLoad(this);
	}
	
	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}
	
	@Override
	public String getName() {
		return "TimeIntervalSystemLoad";
	}

	@Override
	public String toString(PointInTime baseTime) {
		return timeInterval.toString(baseTime);
	}
}
