package de.uniol.inf.is.odysseus.p2p_new.logicaloperator;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFMetaSchemaParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;

@LogicalOperator(name = "JXTARECEIVER", doc = "Received data with JXTA", minInputPorts = 0, maxInputPorts = 0, category = {LogicalOperatorCategory.SOURCE}, hidden = true)
public class JxtaReceiverAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1L;

	private String pipeID;
	private String peerID;
	
	private SDFSchema assignedSchema;
	
	private String schemaName;
	private String basetimeUnit;
	private SourceAdvertisement importedSrcAdvertisement;
	
	private List<SDFMetaSchema> metaschemata;
	
	public JxtaReceiverAO() {
		super();
	}

	public JxtaReceiverAO(JxtaReceiverAO other) {
		super(other);
		
		this.pipeID = other.pipeID;
		this.peerID = other.peerID;
		this.assignedSchema = other.assignedSchema.clone();
		this.metaschemata = other.metaschemata;
		this.importedSrcAdvertisement = other.importedSrcAdvertisement;
		this.basetimeUnit = other.basetimeUnit;
		this.schemaName = other.schemaName;
		this.importedSrcAdvertisement = other.importedSrcAdvertisement;
		
		setParameterInfos(other.getParameterInfos());
		appendToAssignedSchema(basetimeUnit);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new JxtaReceiverAO(this);
	}

	public String getPipeID() {
		return this.pipeID;
	}

	@Override
	public boolean isValid() {
		return !Strings.isNullOrEmpty(this.pipeID);
	}
	
	public Optional<SourceAdvertisement> getImportedSourceAdvertisement() {
		return Optional.fromNullable(importedSrcAdvertisement);
	}
	
	public void setImportedSourceAdvertisement(SourceAdvertisement sourceAdvertisement) {
		this.importedSrcAdvertisement = sourceAdvertisement;
	}
	
	@Override
	@Parameter(name = "Name", type = StringParameter.class, optional = true)
	public void setName(String name) {
		if( name == null && getName() != null ) {
			super.setName(null);
			
			removeParameterInfo("NAME");
		} else if( (getName() == null && name != null) || !getName().equals(name)) {
			super.setName(name);
		
			addParameterInfo("NAME", "'" + name + "'");
		}
	}

	@Parameter(name = "PIPEID", doc = "Jxta Pipe ID to communicate with", type = StringParameter.class, optional = false)
	public void setPipeID(String pipeID) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(pipeID), "PipeID for sender ao must not be null or empty!");
		this.pipeID = pipeID;
		addParameterInfo("PIPEID", "'" + pipeID + "'");
	}
	
	@Parameter(name = "PEERID", doc = "Jxta Peer ID to communicate with", type = StringParameter.class, optional = false)
	public void setPeerID( String peerID ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(peerID), "PeerID for sender ao must not be null or empty!");
		
		this.peerID = peerID;
		addParameterInfo("PEERID", "'" + peerID + "'");
	}
	
	public String getPeerID() {
		return peerID;
	}

	@Parameter(name="SCHEMA", type = CreateSDFAttributeParameter.class, isList=true,optional=false)
	public void setSchema(List<SDFAttribute> outputSchema) {
		if( !Strings.isNullOrEmpty(schemaName)) {
			assignedSchema = SDFSchemaFactory.createNewTupleSchema(schemaName, outputSchema);
		} else {
			assignedSchema = SDFSchemaFactory.createNewTupleSchema("", outputSchema);
		}
		
		if(assignedSchema.getMetaschema()!=null) {
			setMetaschemata(assignedSchema.getMetaschema());
		}
		else {
			if(this.metaschemata!=null) {
				assignedSchema.setMetaSchema(metaschemata);
			}
		}
		
		appendToAssignedSchema(basetimeUnit);
		
		addParameterInfo("SCHEMA", schemaToString(assignedSchema.getAttributes()));
	}
	
	
	public List<SDFAttribute> getSchema() {
		return assignedSchema.getAttributes();
	}
	
	@Parameter(name="BASETIMEUNIT", type = StringParameter.class, optional=true)
	public void setBaseTimeunit( String baseTimeUnit ) {
		this.basetimeUnit = baseTimeUnit;
		
		appendToAssignedSchema(baseTimeUnit);
	}
	
	

	public List<SDFMetaSchema> getMetaschemata() {
		return metaschemata;
	}

	@Parameter(name="METASCHEMATA", type = CreateSDFMetaSchemaParameter.class, isList=true, optional=false)
	public void setMetaschemata(List<SDFMetaSchema> metaschemata) {
		this.metaschemata = metaschemata;
		addParameterInfo("METASCHEMATA",metaSchemataToString(metaschemata));
		if(this.getOutputSchema()!=null) {
			this.assignedSchema.setMetaSchema(metaschemata);
		}
		
	}

	

	private void appendToAssignedSchema(String baseTimeUnit) {
		if( assignedSchema != null && !Strings.isNullOrEmpty(baseTimeUnit)) {
			TimeUnit baseTimeunit = TimeUnit.valueOf(baseTimeUnit);
			
			Map<String, SDFConstraint> constraints = Maps.newHashMap();
			constraints.put(SDFConstraint.BASE_TIME_UNIT, new SDFConstraint(SDFConstraint.BASE_TIME_UNIT, baseTimeunit));
			assignedSchema = SDFSchemaFactory.createNewWithContraints(constraints, assignedSchema);
			
			if(metaschemata!=null) {
				assignedSchema.setMetaSchema(metaschemata);
			}
			
			/*
			// Workaround
			List<SDFMetaSchema> metaSchemaList = Lists.newArrayList();
			metaSchemaList.addAll(TimeInterval.schema);
			assignedSchema = SDFSchemaFactory.createNewWithMetaSchema(assignedSchema, metaSchemaList );
			*/
			
			addParameterInfo("SCHEMA", schemaToString(assignedSchema.getAttributes()));
		}
	}
	
	public String getBaseTimeunit() {
		return basetimeUnit;
	}
	
	@Parameter(name = "SCHEMANAME",type = StringParameter.class, optional=false)
	public void setSchemaName( String schemaName ) {
		this.schemaName = schemaName;
		
		// TODO: This must be done inside initialize
		if( assignedSchema != null ) {
			assignedSchema = SDFSchemaFactory.createNewTupleSchema(schemaName, assignedSchema.getAttributes());
			appendToAssignedSchema(basetimeUnit);
			if(metaschemata!=null) {
				assignedSchema.setMetaSchema(metaschemata);
			}
		}
	}
	
	
	@Override
	public void initialize() {
	}
	
	public String getSchemaName() {
		return schemaName;
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		return assignedSchema;
	}

	private static String schemaToString(List<SDFAttribute> outputSchema) {
		if (outputSchema.isEmpty()) {
			return "[]";
		}

		final StringBuilder sb = new StringBuilder();
		sb.append("[");
		final SDFAttribute[] attributes = outputSchema.toArray(new SDFAttribute[0]);
		for (int i = 0; i < attributes.length; i++) {
			final SDFAttribute attribute = attributes[i];
			sb.append("[");
			if( !Strings.isNullOrEmpty(attribute.getSourceName())) {
				sb.append("'").append(attribute.getSourceName()).append("',");
			}
			sb.append("'").append(attribute.getAttributeName());
			sb.append("', '");
			sb.append(attribute.getDatatype().getURI());
			sb.append("']");
			if (i < attributes.length - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	private static String metaSchemataToString(List<SDFMetaSchema> metaSchemata) {
		if(metaSchemata.isEmpty()) {
			return "[]";
		}
		
		
		
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		Iterator<SDFMetaSchema> iter = metaSchemata.iterator();
		while(iter.hasNext()) {
			sb.append(singeMetaSchemaToString(iter.next()));
			if(iter.hasNext()) {
				sb.append(',');
			}
		}
		
		
		sb.append("]");
		
		return sb.toString();
		
	}

	public SDFSchema getAssignedSchema() {
		return assignedSchema;
	}

	public void setAssignedSchema(SDFSchema assignedSchema) {
		this.assignedSchema = assignedSchema;
		if(this.assignedSchema.getMetaschema()!=null) {
			setMetaschemata(this.assignedSchema.getMetaschema());
		}
	}
	
	@Override
	public boolean isSinkOperator() {
		return false;
	}
	
	
	private static String singeMetaSchemaToString(SDFMetaSchema metaschema) {
		String uri = metaschema.getURI();
		String metaelement = metaschema.getMetaAttribute().getName();
		String metaElementDataType = metaschema.getType().getName();
		
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("'");
		sb.append(uri);
		sb.append("','");
		sb.append(metaelement);
		sb.append("','");
		sb.append(metaElementDataType);
		sb.append("',[");
		
		Iterator<SDFAttribute> iter = metaschema.iterator();
		while(iter.hasNext()) {
			SDFAttribute nextAttribute = iter.next();
			sb.append(getPQLForAttribute(nextAttribute));
			if(iter.hasNext()) {
				sb.append(",");
			}
		}
		
		sb.append("]]");
		
		
		return sb.toString();
	}
	
	private static String getPQLForAttribute(SDFAttribute attribute) {
		String attributeFullName = attribute.getAttributeName();
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		if (!Strings.isNullOrEmpty(attribute.getSourceName())) {
			sb.append("'").append(attribute.getSourceName()).append("',");
		}
		
		sb.append("'").append(attributeFullName).append("','").append(attribute.getDatatype().getURI()).append("'");
		if (attribute.getUnit() != null || attribute.getDtConstraints().size() > 0) {
			sb.append(",[");
			if (attribute.getUnit() != null) {
				sb.append("['Unit','").append(attribute.getUnit()).append("']");
				if (attribute.getDtConstraints().size() > 0) {
					sb.append(",");
				}
			}
			if (attribute.getDtConstraints().size() > 0) {
				Iterator<SDFConstraint> iter = attribute.getDtConstraints().iterator();
				for (int i = 0; i < attribute.getDtConstraints().size(); i++) {
					SDFConstraint cs = iter.next();
					sb.append("['").append(cs.getURIWithoutQualName()).append("','").append(cs.getValue()).append("']");
					if (i < attribute.getDtConstraints().size() - 1) {
						sb.append(",");
					}
				}
			}
			sb.append("]");
		}

		sb.append("]");
		return sb.toString();
	}
}
