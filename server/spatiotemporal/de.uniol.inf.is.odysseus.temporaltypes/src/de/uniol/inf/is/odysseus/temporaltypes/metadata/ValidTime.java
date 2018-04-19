package de.uniol.inf.is.odysseus.temporaltypes.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

/**
 * Implementation of the ValidTime metadata. This is used for temporal
 * attributes which are predicted to a certain point in time or a time interval.
 * 
 * @author Tobias Brandt
 *
 */
final public class ValidTime extends AbstractBaseMetaAttribute
		implements IValidTime, Cloneable, Serializable, List<PointInTime> {

	private static final long serialVersionUID = -4168542417427389337L;

	private final static String METADATA_NAME = "ValidTime";

	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[] { IValidTime.class };

	public static final List<SDFMetaSchema> schema = new ArrayList<SDFMetaSchema>(classes.length);
	static {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("ValidTime", "start_valid", SDFDatatype.TIMESTAMP));
		attributes.add(new SDFAttribute("ValidTime", "end_valid", SDFDatatype.TIMESTAMP));
		schema.add(SDFSchemaFactory.createNewMetaSchema(METADATA_NAME, Tuple.class, attributes, IValidTime.class));
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
	public boolean includes(PointInTime p) {
		return this.delegateTimeInterval.includes(p);
	}

	/**
	 * Union of the left and right interval, if they are overlapping or adjacent
	 */
	public static TimeInterval union(ITimeInterval left, ITimeInterval right) {
		if (TimeInterval.overlaps(left, right) || TimeInterval.areAdjacent(left, right)) {
			return new TimeInterval(PointInTime.min(left.getStart(), right.getStart()),
					PointInTime.max(left.getEnd(), right.getEnd()));
		}
		return null;
	}

	@Override
	public String toString() {
		return "[" + getValidStart().toString() + ", " + getValidEnd().toString() + ")";
	}

	@Override
	protected IInlineMetadataMergeFunction<? extends IMetaAttribute> getInlineMergeFunction() {
		return new TimeIntervalInlineMetadataMergeFunction();
	}

	@Override
	public ValidTime clone() {
		return new ValidTime(this);
	}

	@Override
	public boolean add(PointInTime e) {
		return false;
	}

	@Override
	public void add(int index, PointInTime element) {
	}

	@Override
	public boolean addAll(Collection<? extends PointInTime> c) {
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends PointInTime> c) {
		return false;
	}

	@Override
	public void clear() {
	}

	@Override
	public boolean contains(Object o) {
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return false;
	}

	@Override
	public PointInTime get(int index) {
		if (index == 0) {
			return this.getValidStart();
		} else {
			return this.getValidEnd();
		}
	}

	@Override
	public int indexOf(Object o) {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public Iterator<PointInTime> iterator() {
		return null;
	}

	@Override
	public int lastIndexOf(Object o) {
		return 0;
	}

	@Override
	public ListIterator<PointInTime> listIterator() {
		return null;
	}

	@Override
	public ListIterator<PointInTime> listIterator(int index) {
		return null;
	}

	@Override
	public boolean remove(Object o) {
		return false;
	}

	@Override
	public PointInTime remove(int index) {
		return null;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}

	@Override
	public PointInTime set(int index, PointInTime element) {
		return null;
	}

	@Override
	public int size() {
		return 2;
	}

	@Override
	public List<PointInTime> subList(int fromIndex, int toIndex) {
		return null;
	}

	@Override
	public Object[] toArray() {
		PointInTime[] times = new PointInTime[2];
		times[0] = this.getValidStart();
		times[1] = this.getValidEnd();
		return times;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		return (T[]) this.toArray();
	}

}
