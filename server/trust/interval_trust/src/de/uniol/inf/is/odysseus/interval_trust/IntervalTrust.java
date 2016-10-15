package de.uniol.inf.is.odysseus.interval_trust;

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
import de.uniol.inf.is.odysseus.trust.ITimeIntervalTrust;
import de.uniol.inf.is.odysseus.trust.ITrust;
import de.uniol.inf.is.odysseus.trust.Trust;

public class IntervalTrust extends AbstractCombinedMetaAttribute implements ITimeIntervalTrust {

	private static final long serialVersionUID = 1599620389994530920L;

	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[] { ITimeInterval.class, ITrust.class };

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return classes;
	}

	public static final List<SDFMetaSchema> schema = new ArrayList<>(classes.length);

	static {
		schema.addAll(TimeInterval.schema);
		schema.addAll(Trust.schema);
	}

	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}

	@Override
	public String getName() {
		return "IntervalTrust";
	}

	private final ITrust trust;
	private final ITimeInterval timeInterval;

	public IntervalTrust() {
		this.trust = new Trust();
		this.timeInterval = new TimeInterval();
	}

	public IntervalTrust(IntervalTrust other) {
		this.trust = (ITrust) other.trust.clone();
		this.timeInterval = (ITimeInterval) other.timeInterval.clone();
	}

	@Override
	public IntervalTrust clone() {
		return new IntervalTrust(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof IntervalTrust)) {
			return false;
		}
		IntervalTrust other = (IntervalTrust) obj;
		return this.trust.equals(other.trust) && this.timeInterval.equals(other.timeInterval);
	}

	// ------------------------------------------------------------------------------
	// Methods that need to merge different types
	// ------------------------------------------------------------------------------

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		this.timeInterval.retrieveValues(values);
		this.trust.retrieveValues(values);
	}

	@Override
	public void writeValues(List<Tuple<?>> values) {
		this.timeInterval.writeValue(values.get(0));
		this.trust.writeValue(values.get(1));
	}

	@Override
	public List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> getInlineMergeFunctions() {
		List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> list = new ArrayList<>();
		list.addAll(this.timeInterval.getInlineMergeFunctions());
		list.addAll(this.trust.getInlineMergeFunctions());
		return list;
	}

	@Override
	public <K> K getValue(int subtype, int index) {
		switch (subtype) {
		case 0:
			return this.timeInterval.getValue(0, index);
		case 1:
			return this.trust.getValue(0, index);
		default:
			return null;
		}
	}

	@Override
	public String toString() {
		return "( i= " + this.timeInterval.toString() + " | " + " l=" + this.trust + ")";
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
