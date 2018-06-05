package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.MetaAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;

@LogicalOperator(name="Metadata", maxInputPorts=1, minInputPorts = 1, category={LogicalOperatorCategory.PROCESSING}, doc="Change the current meta data")
public class MetadataAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -1285957744697002431L;

	private IMetaAttribute localMetaAttribute;

	public MetadataAO() {
	}

	public MetadataAO(MetadataAO metadataAO) {
		super(metadataAO);
		this.localMetaAttribute = metadataAO.localMetaAttribute;
	}

	@Parameter(type = MetaAttributeParameter.class, name = "metaAttribute", isList = false, optional = false,possibleValues="getMetadataTypes", doc = "This overwrites the current set meta data. Existing values will not be overwritten.")
	public void setLocalMetaAttribute(IMetaAttribute metaAttribute){
		this.localMetaAttribute = metaAttribute;
	}

	public List<String> getMetadataTypes(){
		return new ArrayList<String>(MetadataRegistry.getNames());
	}

	public IMetaAttribute getLocalMetaAttribute() {
		return localMetaAttribute;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema inputSchema = getInputSchema(0);
		List<SDFMetaSchema> metaSchema = MetadataRegistry.getMetadataSchema(MetadataRegistry.toClassNames(localMetaAttribute.getClasses()));
		SDFSchema outputSchema = SDFSchemaFactory.createNewWithMetaSchema(inputSchema, metaSchema);
		return outputSchema;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new MetadataAO(this);
	}

}
