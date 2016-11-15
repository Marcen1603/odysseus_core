package de.uniol.inf.is.odysseus.core.metadata;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

public class GenericMetaAttribute extends AbstractBaseMetaAttribute implements IGenericMetaAttribute{

	public static final String NAME = "Generic";

	private static final long serialVersionUID = 437486308811181701L;

	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[] { IGenericMetaAttribute.class };

	public static final List<SDFMetaSchema> schema = new ArrayList<>();
	static{
		List<SDFAttribute> attributes = new ArrayList<>();
		attributes.add(new SDFAttribute("Generic", "content", new SDFDatatype("GEN_TUPLE",SDFDatatype.KindOfDatatype.TUPLE, SDFDatatype.TUPLE)));
		schema.add(SDFSchemaFactory.createNewMetaSchema("Generic", Tuple.class, attributes, IGenericMetaAttribute.class));
	}
	private Tuple<?> content;
	
	public GenericMetaAttribute(){
	}
	
	public GenericMetaAttribute(GenericMetaAttribute genericMetaAttribute) {
		this.content = genericMetaAttribute.content.clone();
	}

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return classes;
	}

	@Override
	public String getName() {
		return NAME;
	}	
	
	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}

	@Override
	public void setContent(Tuple<?> tuple) {
		this.content = tuple.clone();
	}
	
	@Override
	public Tuple<?> getContent() {
		return this.content;
	}
	
	@Override
	public void retrieveValues(List<Tuple<?>> values) {
		values.add(content);
	}

	@Override
	public void writeValue(Tuple<?> value) {
		this.content = value.clone();
	}

	@Override
	public <K> K getValue(int subtype, int index) {
		return content.getAttribute(index);
	}

	@Override
	protected IInlineMetadataMergeFunction<? extends IMetaAttribute> getInlineMergeFunction() {
		return new GenericMetaAttributeMergeFunction();
	}

	@Override
	public IMetaAttribute clone() {
		return new GenericMetaAttribute(this);
	}

}
