package de.uniol.inf.is.odysseus.temporaltypes.metadata;

import java.io.Serializable;
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
import de.uniol.inf.is.odysseus.temporaltypes.merge.ValidTimesMetadataMergeFunction;

/**
 * The metadata type for a list of valid times. Bundles the underlying metadata
 * type ValidTime. Used because in the same stream time interval multiple valid
 * time intervals can exist.
 * 
 * @author Tobias Brandt
 *
 */
public class ValidTimes extends AbstractBaseMetaAttribute implements IValidTimes, Cloneable, Serializable {

	private static final long serialVersionUID = -7851387086652619437L;

	private final static String METADATA_NAME = "ValidTimes";

	List<IValidTime> validTimes = new ArrayList<>();

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
	protected IInlineMetadataMergeFunction<? extends IMetaAttribute> getInlineMergeFunction() {
		return new ValidTimesMetadataMergeFunction();
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

}
