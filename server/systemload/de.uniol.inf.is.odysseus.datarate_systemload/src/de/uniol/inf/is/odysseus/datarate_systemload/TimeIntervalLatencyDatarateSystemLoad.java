package de.uniol.inf.is.odysseus.datarate_systemload;

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
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.datarate.Datarate;
import de.uniol.inf.is.odysseus.datarate.IDatarate;
import de.uniol.inf.is.odysseus.latency.Latency;
import de.uniol.inf.is.odysseus.systemload.ISystemLoad;
import de.uniol.inf.is.odysseus.systemload.SystemLoad;

final public class TimeIntervalLatencyDatarateSystemLoad extends
		AbstractMetaAttribute implements ITimeInterval, ILatency, IDatarate,
		ISystemLoad {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[] {
			ISystemLoad.class, IDatarate.class, ILatency.class,
			ITimeInterval.class };

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}

	public static final List<SDFSchema> schema = new ArrayList<SDFSchema>(
			CLASSES.length);
	static {
		schema.addAll(TimeInterval.schema);
		schema.addAll(Latency.schema);
		schema.addAll(Datarate.schema);
		schema.addAll(SystemLoad.schema);
	}

	@Override
	public List<SDFSchema> getSchema() {
		return schema;
	}

	final private ITimeInterval timeInterval;
	final private ILatency latency;
	final private IDatarate datarate;
	final private ISystemLoad systemload;

	public TimeIntervalLatencyDatarateSystemLoad() {
		timeInterval = new TimeInterval();
		latency = new Latency();
		datarate = new Datarate();
		systemload = new SystemLoad();
	}

	public TimeIntervalLatencyDatarateSystemLoad(
			TimeIntervalLatencyDatarateSystemLoad other) {
		timeInterval = other.timeInterval.clone();
		latency = other.latency.clone();
		datarate = other.datarate.clone();
		systemload = other.systemload.clone();
	}

	@Override
	public TimeIntervalLatencyDatarateSystemLoad clone() {
		return new TimeIntervalLatencyDatarateSystemLoad(this);
	}

	@Override
	public String getName() {
		return "TimeIntervalLatencyDatarateSystemLoad";
	}

	// ------------------------------------------------------------------------------
	// Methods that need to merge different types
	// ------------------------------------------------------------------------------
	
	@Override
	public void fillValueList(List<Tuple<?>> values) {
		timeInterval.fillValueList(values);
		latency.fillValueList(values);
		datarate.fillValueList(values);
		systemload.fillValueList(values);
	}
	
	@Override
	public <K> K getValue(int subtype, int index) {
		switch(subtype){
			case 0:
				return timeInterval.getValue(0, index);
			case 1:
				return latency.getValue(0, index);
			case 2:
				return datarate.getValue(0, index);
			case 3:
				return systemload.getValue(0, index);
		}
		return null;
	}

	@Override
	public String getCSVHeader(char delimiter) {
		return timeInterval.getCSVHeader(delimiter) + delimiter
				+ latency.getCSVHeader(delimiter) + delimiter
				+ datarate.getCSVHeader(delimiter) + delimiter
				+ systemload.getCSVHeader(delimiter);
	}

	@Override
	public String csvToString(WriteOptions options) {
		return timeInterval.csvToString(options) + options.getDelimiter()
				+ latency.csvToString(options) + options.getDelimiter()
				+ datarate.csvToString(options) + options.getDelimiter()
				+ systemload.csvToString(options);
	}

	@Override
	public String toString() {
		return "( i = " + timeInterval.toString() + " | l = "
				+ latency.toString() + " | datarate = " + datarate.toString()
				+ " | sysload = )" + systemload + toString();
	}

	@Override
	public String toString(PointInTime baseTime) {
		return "( i = " + timeInterval.toString(baseTime) + " | l = "
				+ latency.toString() + " | datarate = " + datarate.toString()
				+ " | sysload = )" + systemload + toString();
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
	// Delegates for latency
	// ------------------------------------------------------------------------------

	
	@Override
	public final long getLatency() {
		return latency.getLatency();
	}
	
	@Override
	public long getMaxLatency() {
		return latency.getMaxLatency();
	}

	@Override
	public final long getLatencyEnd() {
		return latency.getLatencyEnd();
	}

	@Override
	public final long getLatencyStart() {
		return latency.getLatencyStart();
	}
	
	@Override
	public long getMaxLatencyStart() {
		return latency.getMaxLatencyStart();
	}

	@Override
	public final void setLatencyEnd(long timestamp) {
		latency.setLatencyEnd(timestamp);
	}

	@Override
	public final void setMinLatencyStart(long timestamp) {
		latency.setMinLatencyStart(timestamp);
	}
	
	@Override
	public void setMaxLatencyStart(long timestamp) {
		latency.setMaxLatencyStart(timestamp);
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
