package de.uniol.inf.is.odysseus.recovery.recoverytime.internal;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractBaseMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.recovery.recoverytime.IRecoveryTime;

/**
 * Meta attribute for recovery time information. <br />
 * <br />
 * Recovery time is defined as the time between the first element after a
 * restart and the first trustworthy element. Recovery time is measured in
 * system time as well as in application time.
 *
 * @author Michael Brand
 *
 */
public class RecoveryTime extends AbstractBaseMetaAttribute implements IRecoveryTime {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 146110535458747739L;

	/**
	 * The interface of this meta attribute.
	 */
	private static final Class<? extends IMetaAttribute> interfaceClass = IRecoveryTime.class;

	/**
	 * The meta schema name to use.
	 */
	private static final String schemaName = "RecoveryTime";

	/**
	 * The schema of the meta attribute.
	 */
	public static final List<SDFMetaSchema> schema = new ArrayList<>(1);

	static {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute(schemaName, "sysTimeStart", SDFDatatype.LONG));
		attributes.add(new SDFAttribute(schemaName, "sysTimeEnd", SDFDatatype.LONG));
		attributes.add(new SDFAttribute(schemaName, "sysTime", SDFDatatype.LONG));
		attributes.add(new SDFAttribute(schemaName, "appTimeStart", SDFDatatype.LONG));
		attributes.add(new SDFAttribute(schemaName, "appTimeEnd", SDFDatatype.LONG));
		attributes.add(new SDFAttribute(schemaName, "appTime", SDFDatatype.LONG));
		schema.add(SDFSchemaFactory.createNewMetaSchema(schemaName, Tuple.class, attributes, interfaceClass));
	}

	/**
	 * The system time at which the first element after a restart has been seen;
	 * -1 if it has not been seen yet.
	 */
	private long startSysTime = -1;

	/**
	 * The system time at which the first trustworthy element after a restart
	 * has been seen; -1 if it has not been seen yet.
	 */
	private long endSysTime = -1;

	/**
	 * The time stamp of the first element after a restart; -1 if it has not
	 * been seen yet.
	 */
	private long startAppTime = -1;

	/**
	 * Tets the time stamp of the first trustworthy element after a restart; -1
	 * if it has not been seen yet.
	 */
	private long endAppTime = -1;

	/**
	 * Empty default constructor.
	 */
	public RecoveryTime() {
	}

	/**
	 * Creates a new recovery time meta attribute as the copy of an existing
	 * one.
	 *
	 * @param other
	 *            The meta attribute to copy.
	 */
	public RecoveryTime(RecoveryTime other) {
		startSysTime = other.startSysTime;
		endSysTime = other.endSysTime;

		startAppTime = other.startAppTime;
		endAppTime = other.endAppTime;
	}

	@Override
	public IMetaAttribute clone() {
		return new RecoveryTime(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (endAppTime ^ (endAppTime >>> 32));
		result = prime * result + (int) (endSysTime ^ (endSysTime >>> 32));
		result = prime * result + (int) (startAppTime ^ (startAppTime >>> 32));
		result = prime * result + (int) (startSysTime ^ (startSysTime >>> 32));
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
		RecoveryTime other = (RecoveryTime) obj;
		if (endAppTime != other.endAppTime)
			return false;
		if (endSysTime != other.endSysTime)
			return false;
		if (startAppTime != other.startAppTime)
			return false;
		if (startSysTime != other.startSysTime)
			return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return new Class[] { interfaceClass };
	}

	@Override
	public String getName() {
		return schemaName;
	}

	@Override
	public String toString() {
		return "RecoveryTime [startSysTime=" + startSysTime + ", endSysTime=" + endSysTime + ", sysTime=" + calcSysTime() + ", startAppTime="
				+ startAppTime + ", endAppTime=" + endAppTime + ", appTime=" + calcAppTime() + "]";
	}

	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		final Tuple<? extends IRecoveryTime> t = new Tuple<>(6, false);
		t.setAttribute(0, startSysTime);
		t.setAttribute(1, endSysTime);
		t.setAttribute(2, calcSysTime());
		t.setAttribute(3, startAppTime);
		t.setAttribute(4, endAppTime);
		t.setAttribute(5, calcAppTime());
		values.add(t);
	}

	/**
	 * Calculates the system time between the first element after a restart and
	 * the first trustworthy element.
	 */
	private long calcSysTime() {
		return endSysTime - startSysTime;
	}

	/**
	 * Calculates the application time between the first element after a restart
	 * and the first trustworthy element.
	 */
	private long calcAppTime() {
		return endAppTime - startAppTime;
	}

	@Override
	public void writeValue(Tuple<?> value) {
		startSysTime = value.getAttribute(0);
		endSysTime = value.getAttribute(1);
		startAppTime = value.getAttribute(3);
		endAppTime = value.getAttribute(4);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K> K getValue(int subtype, int index) {
		switch (index) {
		case 0:
			return (K) (Long) startSysTime;
		case 1:
			return (K) (Long) endSysTime;
		case 2:
			return (K) (Long) calcSysTime();
		case 3:
			return (K) (Long) startAppTime;
		case 4:
			return (K) (Long) endAppTime;
		case 5:
			return (K) (Long) calcAppTime();
		default:
			return null;
		}
	}

	@Override
	protected IInlineMetadataMergeFunction<? extends IMetaAttribute> getInlineMergeFunction() {
		return new RecoveryTimeMergeFunction();
	}

	@Override
	public long getSystemTimeStart() {
		return startSysTime;
	}

	@Override
	public void setSystemTimeStart(long time) {
		startSysTime = time;
	}

	@Override
	public long getSystemTimeEnd() {
		return endSysTime;
	}

	@Override
	public void setSystemTimeEnd(long time) {
		endSysTime = time;
	}

	@Override
	public long getRecoverySystemTime() {
		return calcSysTime();
	}

	@Override
	public long getApplicationTimeStart() {
		return startAppTime;
	}

	@Override
	public void setApplicationTimeStart(long time) {
		startAppTime = time;
	}

	@Override
	public long getApplicationTimeEnd() {
		return endAppTime;
	}

	@Override
	public void setApplicationTimeEnd(long time) {
		endAppTime = time;
	}

	@Override
	public long getRecoveryApplicationTime() {
		return calcAppTime();
	}

}
