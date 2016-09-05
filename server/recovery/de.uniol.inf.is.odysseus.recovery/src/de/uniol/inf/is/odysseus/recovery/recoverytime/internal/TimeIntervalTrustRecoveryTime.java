package de.uniol.inf.is.odysseus.recovery.recoverytime.internal;

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
import de.uniol.inf.is.odysseus.recovery.recoverytime.IRecoveryTime;
import de.uniol.inf.is.odysseus.recovery.recoverytime.ITimeIntervalTrustRecoveryTime;
import de.uniol.inf.is.odysseus.trust.ITrust;
import de.uniol.inf.is.odysseus.trust.Trust;

/**
 * Combined eta attribute for {@link ITimeInterval}, {@link ITrust} and
 * {@link IRecoveryTime}.
 * 
 * @author Michael Brand
 *
 */
public class TimeIntervalTrustRecoveryTime extends AbstractCombinedMetaAttribute
		implements ITimeIntervalTrustRecoveryTime {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -1423682604067764061L;

	/**
	 * The meta schema name to use.
	 */
	private static final String schemaName = "TimeIntervalTrustRecoveryTime";

	/**
	 * The interfaces of this meta attribute.
	 */
	@SuppressWarnings("unchecked")
	private static final Class<? extends IMetaAttribute>[] interfaceClasses = new Class[] { ITimeInterval.class,
			ITrust.class, IRecoveryTime.class };

	/**
	 * The schema of the combined meta attribute.
	 */
	public static final List<SDFMetaSchema> schema = new ArrayList<>(interfaceClasses.length);

	static {
		schema.addAll(TimeInterval.schema);
		schema.addAll(Trust.schema);
		schema.addAll(RecoveryTime.schema);
	}

	/**
	 * The used time interval instance.
	 */
	private final ITimeInterval ti;

	/**
	 * The used trust instance.
	 */
	private final ITrust trust;

	/**
	 * The used recovery time instance.
	 */
	private final IRecoveryTime rt;

	/**
	 * Default constructor for delegates.
	 */
	public TimeIntervalTrustRecoveryTime() {
		ti = new TimeInterval();
		trust = new Trust();
		rt = new RecoveryTime();
	}

	/**
	 * Copy constructor.
	 */
	public TimeIntervalTrustRecoveryTime(TimeIntervalTrustRecoveryTime other) {
		ti = (ITimeInterval) other.ti.clone();
		trust = (ITrust) other.trust.clone();
		rt = (IRecoveryTime) other.rt.clone();
	}

	@Override
	public ITimeIntervalTrustRecoveryTime clone() {
		return new TimeIntervalTrustRecoveryTime(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rt == null) ? 0 : rt.hashCode());
		result = prime * result + ((ti == null) ? 0 : ti.hashCode());
		result = prime * result + ((trust == null) ? 0 : trust.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeIntervalTrustRecoveryTime other = (TimeIntervalTrustRecoveryTime) obj;
		if (rt == null) {
			if (other.rt != null)
				return false;
		} else if (!rt.equals(other.rt))
			return false;
		if (ti == null) {
			if (other.ti != null)
				return false;
		} else if (!ti.equals(other.ti))
			return false;
		if (trust == null) {
			if (other.trust != null)
				return false;
		} else if (!trust.equals(other.trust))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TimeIntervalTrustRecoveryTime [timeinterval=" + ti + ", trust=" + trust + ", recoverytime=" + rt + "]";
	}

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return interfaceClasses;
	}

	@Override
	public String getName() {
		return schemaName;
	}

	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		ti.retrieveValues(values);
		trust.retrieveValues(values);
		rt.retrieveValues(values);
	}

	@Override
	public void writeValues(List<Tuple<?>> values) {
		ti.writeValue(values.get(0));
		trust.writeValue(values.get(1));
		rt.writeValue(values.get(2));
	}

	@Override
	public <K> K getValue(int subtype, int index) {
		switch (subtype) {
		case 0:
			return ti.getValue(0, index);
		case 1:
			return trust.getValue(0, index);
		case 2:
			return rt.getValue(0, index);
		default:
			return null;
		}
	}

	@Override
	public List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> getInlineMergeFunctions() {
		List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> list = new ArrayList<>();
		list.addAll(ti.getInlineMergeFunctions());
		list.addAll(trust.getInlineMergeFunctions());
		list.addAll(rt.getInlineMergeFunctions());
		return list;
	}

	// Time Interval delegates

	@Override
	public PointInTime getStart() {
		return ti.getStart();
	}

	@Override
	public PointInTime getEnd() {
		return ti.getEnd();
	}

	@Override
	public void setStart(PointInTime point) {
		ti.setStart(point);
	}

	@Override
	public void setEnd(PointInTime point) {
		ti.setEnd(point);
	}

	@Override
	public void setStartAndEnd(PointInTime start, PointInTime end) {
		ti.setStartAndEnd(start, end);
	}

	@Override
	public String toString(PointInTime baseTime) {
		return ti.toString(baseTime);
	}

	@Override
	public int compareTo(ITimeInterval o) {
		return ti.compareTo(o);
	}

	// Trust delegates

	@Override
	public void setTrust(double trustValue) {
		trust.setTrust(trustValue);
	}

	@Override
	public double getTrust() {
		return trust.getTrust();
	}

	// Recovery time delegates

	@Override
	public long getSystemTimeStart() {
		return rt.getSystemTimeStart();
	}

	@Override
	public void setSystemTimeStart(long time) {
		rt.setSystemTimeStart(time);
	}

	@Override
	public long getSystemTimeEnd() {
		return rt.getSystemTimeEnd();
	}

	@Override
	public void setSystemTimeEnd(long time) {
		rt.setSystemTimeEnd(time);
	}

	@Override
	public long getRecoverySystemTime() {
		return rt.getRecoverySystemTime();
	}

	@Override
	public long getApplicationTimeStart() {
		return rt.getApplicationTimeStart();
	}

	@Override
	public void setApplicationTimeStart(long time) {
		rt.setApplicationTimeStart(time);
	}

	@Override
	public long getApplicationTimeEnd() {
		return rt.getApplicationTimeEnd();
	}

	@Override
	public void setApplicationTimeEnd(long time) {
		rt.setApplicationTimeEnd(time);
	}

	@Override
	public long getRecoveryApplicationTime() {
		return rt.getRecoveryApplicationTime();
	}

}
