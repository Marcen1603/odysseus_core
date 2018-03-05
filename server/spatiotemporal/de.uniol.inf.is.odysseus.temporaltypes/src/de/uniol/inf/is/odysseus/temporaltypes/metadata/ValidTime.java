package de.uniol.inf.is.odysseus.temporaltypes.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractBaseMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeIntervalInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

final public class ValidTime extends AbstractBaseMetaAttribute implements IValidTime, Cloneable, Serializable {

	private static final long serialVersionUID = -4168542417427389337L;

	private final static String METADATA_NAME = "ValidTime";

	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[] { IValidTime.class };

	public static final List<SDFMetaSchema> schema = new ArrayList<SDFMetaSchema>(classes.length);
	static {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("ValidTime", "start_valid", SDFDatatype.TIMESTAMP));
		attributes.add(new SDFAttribute("ValidTime", "end_valid", SDFDatatype.TIMESTAMP));
		schema.add(SDFSchemaFactory.createNewMetaSchema("ValidTime", Tuple.class, attributes, ITimeInterval.class));
	}

	/*
	 * This class has nearly the same behavior as the normal stream time interval.
	 * Most of the methods can be used from the normal time interval class.
	 */
	protected TimeInterval delegateTimeInterval;

	public ValidTime(PointInTime start, PointInTime end) {
		this.delegateTimeInterval = new TimeInterval(start, end);
	}

	public ValidTime(PointInTime start) {
		this.delegateTimeInterval = new TimeInterval(start);
	}

	public ValidTime() {
		this.delegateTimeInterval = new TimeInterval();
	}
	
	public ValidTime(ValidTime toCopy) {
		this.delegateTimeInterval = new TimeInterval(toCopy.delegateTimeInterval);
	}

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return classes;
	}

	@Override
	public String getName() {
		return METADATA_NAME;
	}

	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}

	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		this.delegateTimeInterval.retrieveValues(values);
	}

	@Override
	public void writeValue(Tuple<?> value) {
		this.delegateTimeInterval.writeValue(value);
	}

	@Override
	public <K> K getValue(int subtype, int index) {
		return this.delegateTimeInterval.getValue(subtype, index);
	}

	@Override
	public PointInTime getValidStart() {
		return this.delegateTimeInterval.getStart();
	}

	@Override
	public PointInTime getValidEnd() {
		return this.delegateTimeInterval.getEnd();
	}

	@Override
	public void setValidStart(PointInTime point) {
		this.delegateTimeInterval.setStart(point);
	}

	@Override
	public void setValidEnd(PointInTime point) {
		this.delegateTimeInterval.setEnd(point);
	}

	@Override
	public void setValidStartAndEnd(PointInTime start, PointInTime end) {
		this.delegateTimeInterval.setStartAndEnd(start, end);
	}

	@Override
	public String toString() {
		return getValidStart().toString() + "|" + getValidEnd().toString();
	}
	
	@Override
	protected IInlineMetadataMergeFunction<? extends IMetaAttribute> getInlineMergeFunction() {
		return new TimeIntervalInlineMetadataMergeFunction();
	}

	@Override
	public ValidTime clone() {
		return new ValidTime(this);
	}

}
