package de.uniol.inf.is.odysseus.datarate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

final public class TimeIntervalLatencyDatarate extends AbstractCombinedMetaAttribute
		implements ITimeInterval, ILatency, IDatarate {

	private static final long serialVersionUID = 3739945183008260647L;

	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] CLASSES = new Class[] {
			ITimeInterval.class, ILatency.class, IDatarate.class };

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}

	public static final List<SDFMetaSchema> schema = new ArrayList<SDFMetaSchema>(
			CLASSES.length);
	static {
		schema.addAll(TimeInterval.schema);
		schema.addAll(Latency.schema);
		schema.addAll(Datarate.schema);
	}

	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}

	final private ITimeInterval timeInterval;
	final private ILatency latency;
	final private IDatarate datarate;

	public TimeIntervalLatencyDatarate() {
		timeInterval = new TimeInterval();
		latency = new Latency();
		datarate = new Datarate();
	}

	public TimeIntervalLatencyDatarate(TimeIntervalLatencyDatarate other) {
		timeInterval = (ITimeInterval) other.timeInterval.clone();
		latency = (ILatency) other.latency.clone();
		datarate = (IDatarate) other.datarate.clone();
	}

	@Override
	public TimeIntervalLatencyDatarate clone() {
		return new TimeIntervalLatencyDatarate(this);
	}

	@Override
	public String getName() {
		return "TimeIntervalLatencyDatarate";
	}

	// ------------------------------------------------------------------------------
	// Methods that need to merge different types
	// ------------------------------------------------------------------------------

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		timeInterval.retrieveValues(values);
		latency.retrieveValues(values);
		datarate.retrieveValues(values);
	}

	@Override
	public void writeValues(List<Tuple<?>> values) {
		timeInterval.writeValue(values.get(0));
		latency.writeValue(values.get(1));
		datarate.writeValue(values.get(2));
	}

	@Override
	public List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> getInlineMergeFunctions() {
		List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> list = new ArrayList<>();
		list.addAll(timeInterval.getInlineMergeFunctions());
		list.addAll(latency.getInlineMergeFunctions());
		list.addAll(datarate.getInlineMergeFunctions());
		return list;
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
		}
		return null;
	}

	@Override
	public String toString() {
		return "i = " + timeInterval.toString() + " | l = "
				+ latency.toString() + " | d = " + datarate.toString() + "";
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
	public void setDatarate(String key, double datarate) {
		this.datarate.setDatarate(key, datarate);
	}

	@Override
	public double getDatarate(String key) {
		return datarate.getDatarate(key);
	}

	@Override
	public Map<String, Double> getDatarates() {
		return datarate.getDatarates();
	}

	@Override
	public void setDatarates(Map<String, Double> datarates) {
		datarate.setDatarates(datarates);
	}

}
