package de.uniol.inf.is.odysseus.systemload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

final public class TimeIntervalSystemLoad extends AbstractMetaAttribute implements ITimeInterval, ISystemLoad {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[]{ 
		ITimeInterval.class, ISystemLoad.class
	};
	
	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}
	
	public static final List<SDFSchema> schema = new ArrayList<SDFSchema>(CLASSES.length);
	static{
		schema.addAll(TimeInterval.schema);
		schema.addAll(SystemLoad.schema);
	}
	
	@Override
	public List<SDFSchema> getSchema() {
		return schema;
	}
	
	final private ITimeInterval timeInterval;
	final private ISystemLoad systemload;
	
	public TimeIntervalSystemLoad() {
		timeInterval = new TimeInterval();
		systemload = new SystemLoad();
	}

	public TimeIntervalSystemLoad( TimeIntervalSystemLoad copy ) {
		timeInterval = copy.timeInterval.clone();
		systemload = copy.systemload.clone();
	}
	
	@Override
	public TimeIntervalSystemLoad clone() {
		return new TimeIntervalSystemLoad(this);
	}
	
	@Override
	public String getName() {
		return "TimeIntervalSystemLoad";
	}
	
	// ------------------------------------------------------------------------------
	// Methods that need to merge different types
	// ------------------------------------------------------------------------------

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		timeInterval.retrieveValues(values);
		systemload.retrieveValues(values);
	}
	
	@Override
	public <K> K getValue(int subtype, int index) {
		switch(subtype){
			case 0:
				return timeInterval.getValue(0, index);
			case 1:
				return systemload.getValue(0, index);
		}
		return null;
	}

	@Override
	public String toString() {
		return "( i= " + timeInterval.toString() + " | sysload= " + super.toString() + " )";
	}

	@Override
	public String getCSVHeader(char delimiter) {
		return timeInterval.getCSVHeader(delimiter) + delimiter + systemload.getCSVHeader(delimiter);
	}

	@Override
	public String csvToString(WriteOptions options) {
		return timeInterval.csvToString(options) + options.getDelimiter() + systemload.csvToString(options);
	}
	
	@Override
	public String toString(PointInTime baseTime) {
		return "( i= " + timeInterval.toString(baseTime) + " | sysload= " + super.toString() + " )";
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
	// Delegates for systemload
	// ------------------------------------------------------------------------------

	public void addSystemLoad(String name) {
		systemload.addSystemLoad(name);
	}

	public void removeSystemLoad(String name) {
		systemload.removeSystemLoad(name);
	}

	public Collection<String> getSystemLoadNames() {
		return systemload.getSystemLoadNames();
	}

	public int getCpuLoad(String name) {
		return systemload.getCpuLoad(name);
	}

	public int getMemLoad(String name) {
		return systemload.getMemLoad(name);
	}

	public int getNetLoad(String name) {
		return systemload.getNetLoad(name);
	}
	
}
