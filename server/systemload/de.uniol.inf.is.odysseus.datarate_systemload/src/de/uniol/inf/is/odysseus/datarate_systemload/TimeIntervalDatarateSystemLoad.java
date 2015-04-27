package de.uniol.inf.is.odysseus.datarate_systemload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractCombinedMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.datarate.Datarate;
import de.uniol.inf.is.odysseus.datarate.IDatarate;
import de.uniol.inf.is.odysseus.systemload.ISystemLoad;
import de.uniol.inf.is.odysseus.systemload.SystemLoad;

final public class TimeIntervalDatarateSystemLoad extends AbstractCombinedMetaAttribute 
		implements ITimeInterval, IDatarate, ISystemLoad  {

	private static final long serialVersionUID = -3865342510097346722L;
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[] {
			ITimeInterval.class, IDatarate.class, ISystemLoad.class};

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}
	
	public static final List<SDFSchema> schema = new ArrayList<SDFSchema>(CLASSES.length);
	static{
		schema.addAll(TimeInterval.schema);
		schema.addAll(Datarate.schema);
		schema.addAll(SystemLoad.schema);
	}
	
	@Override
	public List<SDFSchema> getSchema() {
		return schema;
	}

	final private ITimeInterval timeInterval;
	final private IDatarate datarate;
	final private ISystemLoad systemload;

	public TimeIntervalDatarateSystemLoad() {
		timeInterval = new TimeInterval();
		datarate = new Datarate();
		systemload = new SystemLoad();
	}

	public TimeIntervalDatarateSystemLoad(TimeIntervalDatarateSystemLoad other) {
		timeInterval = other.timeInterval.clone();
		datarate = other.datarate.clone();
		systemload = other.systemload.clone();
	}

	@Override
	public TimeIntervalDatarateSystemLoad clone() {
		return new TimeIntervalDatarateSystemLoad(this);
	}

	// ------------------------------------------------------------------------------
	// Methods that need to merge different types
	// ------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "( i = "+ timeInterval.toString() +"| datarate = "
				+ datarate.toString() + "| sysload = " + systemload.toString() + " )";
	}

	@Override
	public String toString(PointInTime baseTime) {
		return "( i = "+ timeInterval.toString(baseTime) +"| datarate = "
				+ datarate.toString() + "| sysload = " + systemload.toString() + " )";
	}
	
	@Override
	public String getCSVHeader(char delimiter) {
		return timeInterval.getCSVHeader(delimiter) + delimiter
				+ datarate.getCSVHeader(delimiter) + delimiter
				+ systemload.getCSVHeader(delimiter);
	}

	@Override
	public String csvToString(WriteOptions options) {
		return timeInterval.csvToString(options) + options.getDelimiter()
				+ datarate.csvToString(options) + options.getDelimiter()
				+ systemload.csvToString(options);
	}

	@Override
	public String getName() {
		return "TimeIntervalDatarateSystemLoad";
	}

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		timeInterval.retrieveValues(values);
		datarate.retrieveValues(values);
		systemload.retrieveValues(values);
	}

	@Override
	public void writeValues(List<Tuple<?>> values) {
		timeInterval.writeValue(values.get(0));
		datarate.writeValue(values.get(1));
		systemload.writeValue(values.get(2));
	}
	
	@Override
	public <K> K getValue(int subtype, int index) {
		switch (subtype) {
		case 0:
			return timeInterval.getValue(0, index);
		case 1:
			return datarate.getValue(0, index);
		case 2:
			return systemload.getValue(0, index);
		}
		return null;
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
	
	// ------------------------------------------------------------------------------
	// Delegates for Systemload
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
