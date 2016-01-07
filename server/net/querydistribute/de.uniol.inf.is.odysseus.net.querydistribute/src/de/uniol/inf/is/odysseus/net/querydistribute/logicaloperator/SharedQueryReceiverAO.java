package de.uniol.inf.is.odysseus.net.querydistribute.logicaloperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.MetaAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;

@LogicalOperator(name = "SHAREDQUERYRECEIVER", doc = "Receiver of data from other node sharing the same query", minInputPorts = 0, maxInputPorts = 0, category = { LogicalOperatorCategory.SOURCE }, hidden = true)
public class SharedQueryReceiverAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1L;

	private String connectionID;
	private String odysseusNodeID;
	private List<SDFAttribute> attributes;
	private String schemaName;
	private String baseTimeunit;
	private IMetaAttribute localMetaAttribute;

	private SDFSchema outputSchema;

	public SharedQueryReceiverAO() {
		super();
	}

	public SharedQueryReceiverAO(SharedQueryReceiverAO other) {
		super(other);

		this.connectionID = other.connectionID;
		this.odysseusNodeID = other.odysseusNodeID;
		if (other.attributes != null) {
			this.attributes = Lists.newArrayList(other.attributes);
		}
		this.schemaName = other.schemaName;
		this.baseTimeunit = other.baseTimeunit;
		this.localMetaAttribute = other.localMetaAttribute;
		this.outputSchema = other.outputSchema.clone();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SharedQueryReceiverAO(this);
	}

	@Parameter(name = "CONNECTIONID", doc = "Connection UUID to identify connections between nodes", type = StringParameter.class, optional = false)
	public void setConnectionID(String uuidString) {
		connectionID = uuidString;
	}

	public String getConnectionID() {
		return connectionID;
	}

	@Parameter(name = "ODYSSEUSNODEID", doc = "OdysseusNodeID to identify the node to connect to", type = StringParameter.class, optional = false)
	public void setOdysseusNodeID(String odysseusNodeIDString) {
		odysseusNodeID = odysseusNodeIDString;
	}

	public String getOdysseusNodeID() {
		return odysseusNodeID;
	}

	@Parameter(name = "ATTRIBUTES", type = CreateSDFAttributeParameter.class, isList = true, optional = false)
	public void setAttributes(List<SDFAttribute> attributes) {
		this.attributes = attributes;
	}

	public List<SDFAttribute> getAttributes() {
		return attributes;
	}

	@Parameter(name = "SCHEMANAME", type = StringParameter.class, optional = false)
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	
	public String getSchemaName() {
		return schemaName;
	}

	@Parameter(name = "BASETIMEUNIT", type = StringParameter.class, optional = true)
	public void setBaseTimeunit(String baseTimeunit) {
		this.baseTimeunit = baseTimeunit;
	}

	public String getBaseTimeunit() {
		return baseTimeunit;
	}
	
	@Parameter(type = MetaAttributeParameter.class, name = "METAATTRIBUTE", isList = false, optional = false,possibleValues="getMetadataTypes", doc = "If set, this value overwrites the meta data created from this source.")
	public void setLocalMetaAttribute(IMetaAttribute metaAttribute){
		this.localMetaAttribute = metaAttribute;
	}
	
	public IMetaAttribute getLocalMetaAttribute() {
		return localMetaAttribute;
	}


	@Override
	public void initialize() {
		outputSchema = SDFSchemaFactory.createNewTupleSchema(schemaName, attributes);
		if( !Strings.isNullOrEmpty(baseTimeunit)) {
			TimeUnit timeunit = TimeUnit.valueOf(baseTimeunit);
			
			Map<String, SDFConstraint> constraints = Maps.newHashMap();
			constraints.put(SDFConstraint.BASE_TIME_UNIT, new SDFConstraint(SDFConstraint.BASE_TIME_UNIT, timeunit));
			outputSchema = SDFSchemaFactory.createNewWithContraints(constraints, outputSchema);
		}
		
		if( localMetaAttribute != null ) {
			outputSchema.setMetaSchema(localMetaAttribute.getSchema());
		}
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		return outputSchema;
	}

	// method called for parameter METAATTRIBUTE
	public List<String> getMetadataTypes(){
		return new ArrayList<String>(MetadataRegistry.getNames());
	}
	
	@Override
	public boolean isSinkOperator() {
		return false;
	}
}
