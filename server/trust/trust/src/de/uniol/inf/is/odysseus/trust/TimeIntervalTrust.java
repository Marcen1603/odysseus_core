package de.uniol.inf.is.odysseus.trust;

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

public class TimeIntervalTrust extends AbstractCombinedMetaAttribute implements ITimeIntervalTrust {

	private static final long serialVersionUID = -2590477661191250202L;

	@SuppressWarnings("unchecked")
	public transient static final Class<? extends IMetaAttribute>[] CLASSES = new Class[] { ITimeInterval.class,
			ITrust.class };

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return CLASSES;
	}

	public static final List<SDFMetaSchema> schema = new ArrayList<SDFMetaSchema>(CLASSES.length);
	static {
		schema.addAll(TimeInterval.schema);
		schema.addAll(Trust.schema);
	}

	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}

	final private ITimeInterval timeInterval;
	final private ITrust trust;

	public TimeIntervalTrust() {
		timeInterval = new TimeInterval();
		trust = new Trust();
	}

	public TimeIntervalTrust(TimeIntervalTrust other) {
		timeInterval = (ITimeInterval) other.timeInterval.clone();
		trust = (ITrust) other.trust.clone();
	}

	@Override
	public TimeIntervalTrust clone() {
		return new TimeIntervalTrust(this);
	}

	@Override
	public String getName() {
		return "TimeIntervalTrust";
	}

	// ------------------------------------------------------------------------------
	// Methods that need to merge different types
	// ------------------------------------------------------------------------------

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		timeInterval.retrieveValues(values);
		trust.retrieveValues(values);
	}

	@Override
	public void writeValues(List<Tuple<?>> values) {
		timeInterval.writeValue(values.get(0));
		trust.writeValue(values.get(1));
	}

	@Override
	public List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> getInlineMergeFunctions() {
		List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> list = new ArrayList<>();
		list.addAll(timeInterval.getInlineMergeFunctions());
		list.addAll(trust.getInlineMergeFunctions());
		return list;
	}

	@Override
	public <K> K getValue(int subtype, int index) {
		switch (subtype) {
		case 0:
			return timeInterval.getValue(0, index);
		case 1:
			return trust.getValue(0, index);
		}
		return null;
	}

	@Override
	public String toString(PointInTime baseTime) {
		return "( i = " + timeInterval.toString(baseTime) + " | " + trust.toString() + ")";
	}

	@Override
	public String toString() {
		return "( i = " + super.toString() + " | " + trust.toString() + ")";
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
	// Delegates for Trust
	// ------------------------------------------------------------------------------

	@Override
	public void setTrust(double trustValue) {
		trust.setTrust(trustValue);
	}

	@Override
	public double getTrust() {
		return trust.getTrust();
	}

}
