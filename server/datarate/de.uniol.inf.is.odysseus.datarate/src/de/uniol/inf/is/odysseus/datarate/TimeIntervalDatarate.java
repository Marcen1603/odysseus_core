package de.uniol.inf.is.odysseus.datarate;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

public class TimeIntervalDatarate extends TimeInterval implements ITimeInterval, ITimeIntervalDatarate {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	public transient static final Class<? extends IMetaAttribute>[] CLASSES = new Class[] { IDatarate.class, ITimeInterval.class };

	private Datarate datarate;
	
	public TimeIntervalDatarate() {
		datarate = new Datarate();
	}
	
	public TimeIntervalDatarate(TimeIntervalDatarate other) {
		super(other);
		datarate = new Datarate(other.datarate);
	}

	@Override
	public void setDatarate(double datarate) {
		this.datarate.setDatarate(datarate);
	}

	@Override
	public double getDatarate() {
		return datarate.getDatarate();
	}
	
	@Override
	public TimeIntervalDatarate clone() {
		return new TimeIntervalDatarate(this);
	}
	
	@Override
	public String toString(PointInTime baseTime) {
		return "( i = " + super.toString() + " | "+ datarate.toString() + ")";
	}
	
	@Override
	public String toString() {
		return "( i = " + super.toString() + " | " + datarate.toString() + ")";
	}
	
	@Override
	public String getCSVHeader(char delimiter) {
		return super.getCSVHeader(delimiter) + delimiter + datarate.getCSVHeader(delimiter);
	}
	
	@Override
	public String csvToString(WriteOptions options) {
		return super.csvToString(options) + options.getDelimiter() + datarate.csvToString(options);
	}
	
	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}

	@Override
	public String getName() {
		return "TimeIntervalDatarate";
	}
}
