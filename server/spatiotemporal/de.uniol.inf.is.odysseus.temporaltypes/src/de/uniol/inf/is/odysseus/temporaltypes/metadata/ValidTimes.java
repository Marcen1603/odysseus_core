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

public class ValidTimes extends AbstractBaseMetaAttribute implements IValidTimes, Cloneable, Serializable {

	private static final long serialVersionUID = -7851387086652619437L;
	
	private final static String METADATA_NAME = "ValidTimes";
	
	List<IValidTime> validTimes= new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[] { IValidTimes.class };

	public static final List<SDFMetaSchema> schema = new ArrayList<SDFMetaSchema>(classes.length);
	static {
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("ValidTimes", "ValidTimes", SDFDatatype.LIST_TUPLE));
		schema.add(SDFSchemaFactory.createNewMetaSchema("ValidTimes", Tuple.class, attributes, IValidTime.class));
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
		return (K) validTimes.get(index);
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
	protected IInlineMetadataMergeFunction<? extends IMetaAttribute> getInlineMergeFunction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMetaAttribute clone() {
		return new ValidTimes(this);
	}

}
