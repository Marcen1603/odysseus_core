package de.uniol.inf.is.odysseus.interval_latency_trust;

import java.util.ArrayList;
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

	public static final List<SDFMetaSchema> schema = new ArrayList<>(classes.length);
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
		this.timeInterval = new TimeInterval();
		this.latency = new Latency();
		this.trust = new Trust();
	}

	public IntervalLatencyTrust(IntervalLatencyTrust clone) {
		this.timeInterval = (ITimeInterval) clone.timeInterval.clone();
		this.latency = (ILatency) clone.latency.clone();
		this.trust = clone.trust;
	}

	@Override
	public IntervalLatencyTrust clone() {
		return new IntervalLatencyTrust(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof IntervalLatencyTrust)) {
			return false;
		}
		IntervalLatencyTrust other = (IntervalLatencyTrust) obj;
		return this.trust.equals(other.trust) && this.timeInterval.equals(other.timeInterval) && this.latency.equals(other.latency);
	}

	// ------------------------------------------------------------------------------
	// Methods that need to merge different types
	// ------------------------------------------------------------------------------

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		this.timeInterval.retrieveValues(values);
		this.latency.retrieveValues(values);
		this.trust.retrieveValues(values);
	}

	@Override
	public void writeValues(List<Tuple<?>> values) {
		this.timeInterval.writeValue(values.get(0));
		this.latency.writeValue(values.get(1));
		this.trust.writeValue(values.get(2));
	}

	@Override
	public List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> getInlineMergeFunctions() {
		List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> list = new ArrayList<>();
		list.addAll(this.timeInterval.getInlineMergeFunctions());
		list.addAll(this.latency.getInlineMergeFunctions());
		list.addAll(this.trust.getInlineMergeFunctions());
		return list;
	}


	@Override
	public <K> K getValue(int subtype, int index) {
		switch(subtype){
			case 0:
				return this.timeInterval.getValue(0, index);
			case 1:
				return this.latency.getValue(0, index);
			case 2:
				return this.trust.getValue(0, index);
			default:
				return null;
		}
	}

	@Override
	public String toString() {
		return "( i= " +this.timeInterval.toString() + " | " + " l="+ this.latency+" | " + " t="+ this.trust+ ")";
	}

	// ------------------------------------------------------------------------------
	// Delegates for timeInterval
	// ------------------------------------------------------------------------------

	@Override
	public PointInTime getStart() {
		return this.timeInterval.getStart();
	}

	@Override
	public PointInTime getEnd() {
		return this.timeInterval.getEnd();
	}

	@Override
	public void setStart(PointInTime point) {
		this.timeInterval.setStart(point);
	}

	@Override
	public void setEnd(PointInTime point) {
		this.timeInterval.setEnd(point);
	}

	@Override
	public void setStartAndEnd(PointInTime start, PointInTime end) {
		this.timeInterval.setStartAndEnd(start, end);
	}

	@Override
	public int compareTo(ITimeInterval o) {
		return this.timeInterval.compareTo(o);
	}

	// ------------------------------------------------------------------------------
	// Delegates for latency
	// ------------------------------------------------------------------------------


	@Override
	public final long getLatency() {
		return this.latency.getLatency();
	}

	@Override
	public long getMaxLatency() {
		return this.latency.getMaxLatency();
	}

	@Override
	public final long getLatencyEnd() {
		return this.latency.getLatencyEnd();
	}

	@Override
	public final long getLatencyStart() {
		return this.latency.getLatencyStart();
	}

	@Override
	public long getMaxLatencyStart() {
		return this.latency.getMaxLatencyStart();
	}

	@Override
	public final void setLatencyEnd(long timestamp) {
		this.latency.setLatencyEnd(timestamp);
	}

	@Override
	public final void setMinLatencyStart(long timestamp) {
		this.latency.setMinLatencyStart(timestamp);
	}

	@Override
	public void setMaxLatencyStart(long timestamp) {
		this.latency.setMaxLatencyStart(timestamp);
	}

	// ------------------------------------------------------------------------------
	// Delegates for trust
	// ------------------------------------------------------------------------------

	@Override
	public double getTrust() {
		return this.trust.getTrust();
	}

	@Override
	public void setTrust(double trustValue) {
		this.trust.setTrust(trustValue);
	}
}
