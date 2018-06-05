package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.MetaAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResourceParameter;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;

@LogicalOperator(name = "MemStoreSource", minInputPorts = 0, maxInputPorts = 0, doc = "This operator provides all elements of the given memory store as stream.", category = {
		LogicalOperatorCategory.SOURCE })
public class MemStoreSourceAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 3193531101511426942L;
	private Resource store;
	private List<SDFAttribute> attributes;
	private SDFSchema schema;
	private String dataHandler = "Tuple";
	private IMetaAttribute metaAttribute;

	
	public MemStoreSourceAO() {
	}

	public MemStoreSourceAO(MemStoreSourceAO memStoreReadAO) {
		this.store = memStoreReadAO.store;
		if (memStoreReadAO.attributes != null) {
			this.attributes = new ArrayList<SDFAttribute>(memStoreReadAO.attributes);
		}
		this.dataHandler = memStoreReadAO.dataHandler;
		this.metaAttribute = memStoreReadAO.metaAttribute;
	}

	public Resource getStore() {
		return store;
	}

	@Parameter(type = ResourceParameter.class, optional = false, doc = "The name of the memory store to read from.")
	public void setStore(Resource store) {
		this.store = store;
	}

	@Parameter(type = CreateSDFAttributeParameter.class, name = "Schema", isList = true, optional = false, doc = "The output schema.")
	public void setAttributes(List<SDFAttribute> attributes) {
		this.attributes = attributes;
	}

	
	@Parameter(type = MetaAttributeParameter.class, name = "metaAttribute", isList = false, optional = false, possibleValues = "getMetadataTypes", doc = "If set, this value overwrites the meta data created from this source.")
	public void setMetaAttribute(IMetaAttribute metaAttribute) {
		this.metaAttribute = metaAttribute;
	}

	public List<String> getMetadataTypes() {
		return new ArrayList<String>(MetadataRegistry.getNames());
	}

	public IMetaAttribute getMetaAttribute() {
		return metaAttribute;
	}
	public List<SDFAttribute> getAttributes() {
		return attributes;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {		
		if (schema == null) {
			
			@SuppressWarnings("rawtypes")
			Class<? extends IStreamObject> type = DataHandlerRegistry.getCreatedType(dataHandler);
			if (type == null) {
				type = Tuple.class;
			}

			schema = SDFSchemaFactory.createNewSchema(getName(), type, attributes);
			
			List<SDFMetaSchema> metaSchema = metaAttribute.getSchema();
			
			schema = SDFSchemaFactory.createNewWithMetaSchema(schema, metaSchema);
		}
		return schema;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new MemStoreSourceAO(this);
	}

}
