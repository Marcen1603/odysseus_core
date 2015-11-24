package de.uniol.inf.is.odysseus.interval_latency_trust;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.WriteOptions;
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
import de.uniol.inf.is.odysseus.trust.ITrust;
import de.uniol.inf.is.odysseus.trust.Trust;

public class IntervalLatencyTrust extends AbstractCombinedMetaAttribute implements ITimeInterval, ILatency, ITrust {

	private static final long serialVersionUID = -3129934770814427153L;
	
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[]{ 
		ITimeInterval.class, ILatency.class, ITrust.class
	};
	
	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return classes;
	}
	
	public static final List<SDFMetaSchema> schema = new ArrayList<SDFMetaSchema>(classes.length);
	static{
		schema.addAll(TimeInterval.schema);
		schema.addAll(Latency.schema);
		schema.addAll(Trust.schema);
	}
	
	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}
	
	@Override
	public String getName() {
		return "IntervalLatencyTrust";
	}
	
	private final ITimeInterval timeInterval;
	private final ILatency latency;
	private final ITrust trust;
	
	public IntervalLatencyTrust() {
		timeInterval = new TimeInterval();
		latency = new Latency();
		trust = new Trust();
	}
	
	public IntervalLatencyTrust(IntervalLatencyTrust clone) {
		this.timeInterval = clone.timeInterval.clone();
		this.latency = clone.latency.clone();
		this.trust = clone.trust;
	}
	
	@Override
	public IntervalLatencyTrust clone() {
		return new IntervalLatencyTrust(this);
	}
	
	// ------------------------------------------------------------------------------
	// Methods that need to merge different types
	// ------------------------------------------------------------------------------

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		timeInterval.retrieveValues(values);
		latency.retrieveValues(values);
		trust.retrieveValues(values);
	}
	
	@Override
	public void writeValues(List<Tuple<?>> values) {
		timeInterval.writeValue(values.get(0));
		latency.writeValue(values.get(1));
		trust.writeValue(values.get(2));
	}
	
	@Override
	public List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> getInlineMergeFunctions() {
		List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> list = new ArrayList<>();
		list.addAll(timeInterval.getInlineMergeFunctions());
		list.addAll(latency.getInlineMergeFunctions());
		list.addAll(trust.getInlineMergeFunctions());
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
				return trust.getValue(0, index);
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "( i= " +timeInterval.toString() + " | " + " l="+ this.latency+" | " + " t="+ this.trust+ ")";
	}
	
	@Override
	public String toString(PointInTime baseTime) {
		return "( i= " +timeInterval.toString(baseTime) + " | " + " l="+ this.latency+" | " + " t="+ this.trust+ ")";
	}

	@Override
	public String csvToString(WriteOptions options) {
		return timeInterval.csvToString(options)+options.getDelimiter()+this.latency.csvToString(options)+this.trust.csvToString(options);
	}
	
	@Override
	public String getCSVHeader(char delimiter) {
		return timeInterval.getCSVHeader(delimiter)+delimiter+this.latency.getCSVHeader(delimiter)+this.trust.getCSVHeader(delimiter);
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
	// Delegates for trust
	// ------------------------------------------------------------------------------

	@Override
	public double getTrust() {
		return trust.getTrust();
	}
	
	@Override
	public void setTrust(double trustValue) {
		this.trust.setTrust(trustValue);
	}
}
