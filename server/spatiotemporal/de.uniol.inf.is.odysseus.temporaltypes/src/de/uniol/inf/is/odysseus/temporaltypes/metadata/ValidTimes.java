package de.uniol.inf.is.odysseus.temporaltypes.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractBaseMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.temporaltypes.merge.ValidTimesIntersectionMetadataMergeFunction;

/**
 * The metadata type for a list of valid times. Bundles the underlying metadata
 * type ValidTime. Used because in the same stream time interval multiple valid
 * time intervals can exist.
 * 
 * @author Tobias Brandt
 *
 */
public class ValidTimes extends AbstractBaseMetaAttribute
		implements IValidTimes, Cloneable, Serializable, List<IValidTime> {

	private static final long serialVersionUID = -7851387086652619437L;

	private final static String METADATA_NAME = "ValidTimes";

	private List<IValidTime> validTimes = new ArrayList<>();
	private TimeUnit timeUnit;

	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[] { IValidTimes.class };

	public static final List<SDFMetaSchema> schema = new ArrayList<SDFMetaSchema>(classes.length);
	static {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("ValidTimes", "ValidTimes", SDFDatatype.LIST_TUPLE));
		schema.add(SDFSchemaFactory.createNewMetaSchema("ValidTimes", Tuple.class, attributes, IValidTimes.class));
	}

	public ValidTimes() {
	}

	public ValidTimes(ValidTimes toCopy) {
		for (IValidTime validTime : toCopy.validTimes) {
			this.validTimes.add(validTime);
		}
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
		for (IValidTime validTime : validTimes) {
			validTime.retrieveValues(values);
		}
	}

	@Override
	public void writeValue(Tuple<?> value) {
		for (IValidTime validTime : validTimes) {
			validTime.writeValue(value);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K> K getValue(int subtype, int index) {
		return (K) this;
	}

	@Override
	public List<IValidTime> getValidTimes() {
		return this.validTimes;
	}

	@Override
	public void addValidTime(IValidTime validTime) {
		this.validTimes.add(validTime);
	}

	@Override
	public void clear() {
		this.validTimes = new ArrayList<>();
	}

	@Override
	public boolean includes(PointInTime time) {
		for (IValidTime validTime : this.validTimes) {
			if (validTime.includes(time)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * The TimeUnit of the valid times, which can differ from the TimeUnit of the
	 * stream.
	 */
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	@Override
	protected IInlineMetadataMergeFunction<? extends IMetaAttribute> getInlineMergeFunction() {
		return new ValidTimesIntersectionMetadataMergeFunction();
	}

	@Override
	public IMetaAttribute clone() {
		return new ValidTimes(this);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");

		boolean first = true;
		for (IValidTime validTime : validTimes) {
			if (!first) {
				builder.append(", ");
			}
			builder.append("[" + validTime.getValidStart() + ", " + validTime.getValidEnd() + ")");
			first = false;
		}
		builder.append("]");

		return builder.toString();
	}

	@Override
	public boolean add(IValidTime e) {
		return validTimes.add(e);
	}

	@Override
	public void add(int index, IValidTime element) {
		validTimes.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends IValidTime> c) {
		return validTimes.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends IValidTime> c) {
		return validTimes.addAll(index, c);
	}

	@Override
	public boolean contains(Object o) {
		return validTimes.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return validTimes.containsAll(c);
	}

	@Override
	public IValidTime get(int index) {
		return validTimes.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return validTimes.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return validTimes.isEmpty();
	}

	@Override
	public Iterator<IValidTime> iterator() {
		return validTimes.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return validTimes.lastIndexOf(o);
	}

	@Override
	public ListIterator<IValidTime> listIterator() {
		return validTimes.listIterator();
	}

	@Override
	public ListIterator<IValidTime> listIterator(int index) {
		return validTimes.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return validTimes.remove(o);
	}

	@Override
	public IValidTime remove(int index) {
		return validTimes.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return validTimes.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return validTimes.retainAll(c);
	}

	@Override
	public IValidTime set(int index, IValidTime element) {
		return validTimes.set(index, element);
	}

	@Override
	public int size() {
		return validTimes.size();
	}

	@Override
	public List<IValidTime> subList(int fromIndex, int toIndex) {
		return validTimes.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return validTimes.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return validTimes.toArray(a);
	}

}
