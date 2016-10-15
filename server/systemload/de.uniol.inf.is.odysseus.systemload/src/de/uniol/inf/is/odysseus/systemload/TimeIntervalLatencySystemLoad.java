package de.uniol.inf.is.odysseus.systemload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractCombinedMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.latency.Latency;

final public class TimeIntervalLatencySystemLoad extends AbstractCombinedMetaAttribute implements ITimeInterval, ILatency, ISystemLoad {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[] { ITimeInterval.class, ILatency.class, ISystemLoad.class };

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}

	public static final List<SDFMetaSchema> schema = new ArrayList<SDFMetaSchema>(CLASSES.length);
	static {
		schema.addAll(TimeInterval.schema);
		schema.addAll(Latency.schema);
		schema.addAll(SystemLoad.schema);
	}

	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}

	final private ITimeInterval timeInterval;
	final private ILatency latency;
	final private ISystemLoad systemload;

	public TimeIntervalLatencySystemLoad() {
		timeInterval = new TimeInterval();
		latency = new Latency();
		systemload = new SystemLoad();
	}

	public TimeIntervalLatencySystemLoad(TimeIntervalLatencySystemLoad copy) {
		timeInterval = (ITimeInterval) copy.timeInterval.clone();
		latency = (ILatency) copy.latency.clone();
		systemload = (ISystemLoad) copy.systemload.clone();
	}

	@Override
	public TimeIntervalLatencySystemLoad clone() {
		return new TimeIntervalLatencySystemLoad(this);
	}

	@Override
	public String getName() {
		return "TimeIntervalLatencySystemLoad";
	}

	// ------------------------------------------------------------------------------
	// Methods that need to merge different types
	// ------------------------------------------------------------------------------

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		timeInterval.retrieveValues(values);
		latency.retrieveValues(values);
		systemload.retrieveValues(values);
	}

	@Override
	public void writeValues(List<Tuple<?>> values) {
		timeInterval.writeValue(values.get(0));
		latency.writeValue(values.get(1));
		systemload.writeValue(values.get(2));
	}

	@Override
	public List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> getInlineMergeFunctions() {
		List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> list = new ArrayList<>();
		list.addAll(timeInterval.getInlineMergeFunctions());
		list.addAll(latency.getInlineMergeFunctions());
		list.addAll(systemload.getInlineMergeFunctions());
		return list;
	}

	@Override
	public <K> K getValue(int subtype, int index) {
		switch (subtype) {
		case 0:
			return timeInterval.getValue(0, index);
		case 1:
			return latency.getValue(0, index);
		case 2:
			return systemload.getValue(0, index);
		default:
			return null;
		}
	}

	@Override
	public String toString() {
		return "i=" + timeInterval.toString() + "| +l=" + latency.toString() + " | sysload= " + systemload.toString();
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
	// Delegates for systemload
	// ------------------------------------------------------------------------------

	@Override
	public void addSystemLoad(String name) {
		systemload.addSystemLoad(name);
	}

	@Override
	public void removeSystemLoad(String name) {
		systemload.removeSystemLoad(name);
	}

	@Override
	public Collection<String> getSystemLoadNames() {
		return systemload.getSystemLoadNames();
	}

	@Override
	public int getCpuLoad(String name) {
		return systemload.getCpuLoad(name);
	}

	@Override
	public int getMemLoad(String name) {
		return systemload.getMemLoad(name);
	}

	@Override
	public int getNetLoad(String name) {
		return systemload.getNetLoad(name);
	}

	@Override
	public void insert(ISystemLoad other) {
		systemload.insert(other);
	}

	@Override
	public SystemLoadEntry getSystemLoad(String name) {
		return systemload.getSystemLoad(name);
	}
}
