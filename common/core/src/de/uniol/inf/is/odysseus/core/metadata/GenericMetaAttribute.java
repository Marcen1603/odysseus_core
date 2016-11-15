package de.uniol.inf.is.odysseus.core.metadata;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

public class GenericMetaAttribute extends AbstractBaseMetaAttribute implements IGenericMetaAttribute{

	private static final long serialVersionUID = 437486308811181701L;

	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[] { IGenericMetaAttribute.class };

	private List<SDFMetaSchema> schema = new ArrayList<>();
	private Tuple<?> content;
	
	public GenericMetaAttribute(){
	}
	
	public GenericMetaAttribute(GenericMetaAttribute genericMetaAttribute) {
		this.schema = genericMetaAttribute.getSchema();
		this.content = genericMetaAttribute.content.clone();
	}

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return classes;
	}

	@Override
	public String getName() {
		return "Generic";
	}

	@Override
	public void clearSchema() {
		this.schema.clear();
	}
	
	@Override
	public void addSchema(SDFMetaSchema schema) {
		this.schema.add(schema);
	}
	
	@Override
	public void addSchema(List<SDFMetaSchema> schema) {
		this.schema.addAll(schema);
	}
	
	@Override
	public void addSchema(SDFSchema schema) {
		SDFMetaSchema newSchema = SDFSchemaFactory.createNewMetaSchema("Generic", Tuple.class, schema.getAttributes(), IGenericMetaAttribute.class);
		this.schema.add(newSchema);
	}
	
	
	@Override
	public List<SDFMetaSchema> getSchema() {
		return schema;
	}

	@Override
	public void setContent(Tuple<?> tuple, List<SDFMetaSchema> schema) {
		schema.clear();
		addSchema(schema);
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
