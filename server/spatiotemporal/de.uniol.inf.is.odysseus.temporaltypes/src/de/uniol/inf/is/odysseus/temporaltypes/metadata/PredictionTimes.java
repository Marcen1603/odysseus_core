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
import de.uniol.inf.is.odysseus.temporaltypes.merge.PredictionTimesIntersectionMetadataMergeFunction;

/**
 * The metadata type for a list of prediction times. Bundles the underlying
 * metadata type PredictionTime. Used because in the same stream time interval
 * multiple prediction time intervals can exist.
 * 
 * @author Tobias Brandt
 *
 */
public class PredictionTimes extends AbstractBaseMetaAttribute
		implements IPredictionTimes, Cloneable, Serializable, List<IPredictionTime> {

	private static final long serialVersionUID = -7851387086652619437L;

	private final static String METADATA_NAME = "PredictionTimes";

	private List<IPredictionTime> predictionTimes = new ArrayList<>();
	private TimeUnit timeUnit;

	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[] { IPredictionTimes.class };

	public static final List<SDFMetaSchema> schema = new ArrayList<SDFMetaSchema>(classes.length);
	static {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("PredictionTimes", "PredictionTimes", SDFDatatype.LIST_TUPLE));
		schema.add(SDFSchemaFactory.createNewMetaSchema("PredictionTimes", Tuple.class, attributes,
				IPredictionTimes.class));
	}

	public PredictionTimes() {
	}

	public PredictionTimes(PredictionTimes toCopy) {
		for (IPredictionTime predictionTime : toCopy.predictionTimes) {
			this.predictionTimes.add(predictionTime);
		}
		this.timeUnit = toCopy.getPredictionTimeUnit();
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
		for (IPredictionTime predictionTime : predictionTimes) {
			predictionTime.retrieveValues(values);
		}
	}

	@Override
	public void writeValue(Tuple<?> value) {
		for (IPredictionTime predictionTime : predictionTimes) {
			predictionTime.writeValue(value);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K> K getValue(int subtype, int index) {
		return (K) this;
	}

	@Override
	public List<IPredictionTime> getPredictionTimes() {
		return this.predictionTimes;
	}

	@Override
	public void addPredictionTime(IPredictionTime predictionTime) {
		this.predictionTimes.add(predictionTime);
	}

	@Override
	public void clear() {
		this.predictionTimes = new ArrayList<>();
	}

	@Override
	public boolean includes(PointInTime time) {
		for (IPredictionTime predictionTime : this.predictionTimes) {
			if (predictionTime.includes(time)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * The TimeUnit of the prediction times, which can differ from the TimeUnit of
	 * the stream.
	 */
	public TimeUnit getPredictionTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	@Override
	protected IInlineMetadataMergeFunction<? extends IMetaAttribute> getInlineMergeFunction() {
		return new PredictionTimesIntersectionMetadataMergeFunction();
	}

	@Override
	public IMetaAttribute clone() {
		return new PredictionTimes(this);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		if (this.timeUnit != null) {
			builder.append(this.timeUnit + ": ");
		}

		builder.append("[");

		boolean first = true;
		for (IPredictionTime predictionTime : predictionTimes) {
			if (!first) {
				builder.append(", ");
			}
			builder.append("[" + predictionTime.getPredictionStart() + ", " + predictionTime.getPredictionEnd() + ")");
			first = false;
		}
		builder.append("]");

		return builder.toString();
	}

	@Override
	public boolean add(IPredictionTime e) {
		return predictionTimes.add(e);
	}

	@Override
	public void add(int index, IPredictionTime element) {
		predictionTimes.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends IPredictionTime> c) {
		return predictionTimes.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends IPredictionTime> c) {
		return predictionTimes.addAll(index, c);
	}

	@Override
	public boolean contains(Object o) {
		return predictionTimes.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return predictionTimes.containsAll(c);
	}

	@Override
	public IPredictionTime get(int index) {
		return predictionTimes.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return predictionTimes.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return predictionTimes.isEmpty();
	}

	@Override
	public Iterator<IPredictionTime> iterator() {
		return predictionTimes.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return predictionTimes.lastIndexOf(o);
	}

	@Override
	public ListIterator<IPredictionTime> listIterator() {
		return predictionTimes.listIterator();
	}

	@Override
	public ListIterator<IPredictionTime> listIterator(int index) {
		return predictionTimes.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return predictionTimes.remove(o);
	}

	@Override
	public IPredictionTime remove(int index) {
		return predictionTimes.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return predictionTimes.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return predictionTimes.retainAll(c);
	}

	@Override
	public IPredictionTime set(int index, IPredictionTime element) {
		return predictionTimes.set(index, element);
	}

	@Override
	public int size() {
		return predictionTimes.size();
	}

	@Override
	public List<IPredictionTime> subList(int fromIndex, int toIndex) {
		return predictionTimes.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return predictionTimes.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return predictionTimes.toArray(a);
	}

}
