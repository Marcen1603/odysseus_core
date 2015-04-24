package de.uniol.inf.is.odysseus.datarate;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

final public class TimeIntervalDatarate extends AbstractMetaAttribute implements ITimeInterval, IDatarate {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	public transient static final Class<? extends IMetaAttribute>[] CLASSES = new Class[] {ITimeInterval.class, IDatarate.class};
	
	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}

	public static final List<SDFSchema> schema = new ArrayList<SDFSchema>(CLASSES.length);
	static{
		schema.addAll(TimeInterval.schema);
		schema.addAll(Datarate.schema);
	}
	
	@Override
	public List<SDFSchema> getSchema() {
		return schema;
	}
	
	final private ITimeInterval timeInterval;
	final private IDatarate datarate;
	
	public TimeIntervalDatarate() {
		timeInterval = new TimeInterval();
		datarate = new Datarate();
	}
	
	public TimeIntervalDatarate(TimeIntervalDatarate other) {
		timeInterval = other.timeInterval.clone();
		datarate = other.datarate.clone();
	}

	@Override
	public TimeIntervalDatarate clone() {
		return new TimeIntervalDatarate(this);
	}

	@Override
	public String getName() {
		return "TimeIntervalDatarate";
	}
	
	// ------------------------------------------------------------------------------
	// Methods that need to merge different types
	// ------------------------------------------------------------------------------
	
	@Override
	public void fillValueList(List<Tuple<?>> values) {
		timeInterval.fillValueList(values);
		datarate.fillValueList(values);
	}
	
	@Override
	public <K> K getValue(int subtype, int index) {
		switch(subtype){
			case 0:
				return timeInterval.getValue(0, index);
			case 1:
				return datarate.getValue(0, index);
		}
		return null;
	}
	
	@Override
	public String toString(PointInTime baseTime) {
		return "( i = " + timeInterval.toString(baseTime) + " | "+ datarate.toString() + ")";
	}
	
	@Override
	public String toString() {
		return "( i = " + super.toString() + " | " + datarate.toString() + ")";
	}
	
	@Override
	public String getCSVHeader(char delimiter) {
		return timeInterval.getCSVHeader(delimiter) + delimiter + datarate.getCSVHeader(delimiter);
	}
	
	@Override
	public String csvToString(WriteOptions options) {
		return timeInterval.csvToString(options) + options.getDelimiter() + datarate.csvToString(options);
	}
	
	// ------------------------------------------------------------------------------
	// Delegates for timeInterval
	// ------------------------------------------------------------------------------
	
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
	public int compareTo(ITimeInterval o) {
		return timeInterval.compareTo(o);
	}

	// ------------------------------------------------------------------------------
	// Delegates for Datarate
	// ------------------------------------------------------------------------------
	
	@Override
	public void setDatarate(double datarate) {
		this.datarate.setDatarate(datarate);
	}

	@Override
	public double getDatarate() {
		return datarate.getDatarate();
	}
	

}
