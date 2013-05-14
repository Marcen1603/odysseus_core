package de.uniol.inf.is.odysseus.p2p_new.logicaloperator;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "JXTARECEIVER", doc = "Received data with JXTA", minInputPorts = 0, maxInputPorts = 0)
public class JxtaReceiverAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1L;

	private String pipeID;
	
	private SDFSchema assignedSchema;

	public JxtaReceiverAO() {
		super();
	}

	public JxtaReceiverAO(JxtaReceiverAO other) {
		super(other);
		
		this.pipeID = other.pipeID;
		this.assignedSchema = other.assignedSchema.clone();
		
		setParameterInfos(other.getParameterInfos());
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.ILogicalOperator
	 * #setPOName (java.lang.String)
	 */
	@Override
	@Parameter(name = "Name", type = StringParameter.class, optional = true)
	public void setName(String name) {
		if( name == null && getName() != null ) {
			super.setName(null);
			
			applyNameToSchema();
		}
		
		if( (getName() == null && name != null) || !getName().equals(name)) {
			super.setName(name);
		
			applyNameToSchema();
		}
	}

	@Parameter(name = "PIPEID", doc = "Jxta Pipe ID to communicate with", type = StringParameter.class, optional = false)
	public void setPipeID(String pipeID) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(pipeID), "PipeID for sender ao must not be null or empty!");
		this.pipeID = pipeID;
		addParameterInfo("PIPEID", "'" + pipeID + "'");
	}

	@Parameter(name="SCHEMA", type = CreateSDFAttributeParameter.class, isList=true,optional=false)
	public void setSchema(List<SDFAttribute> outputSchema) {
		assignedSchema = new SDFSchema(getName() != null ? getName() : "", outputSchema);
		addParameterInfo("SCHEMA", convertSchema(outputSchema));
		
		applyNameToSchema();
	}
	
	public List<SDFAttribute> getSchema() {
		return assignedSchema.getAttributes();
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		return assignedSchema;
	}

	private void applyNameToSchema() {
		if( assignedSchema != null ) {
			List<SDFAttribute> attributes = Lists.newArrayList();
			
			final String nameToApply = getName() != null ? getName() : "";
			for( SDFAttribute attribute : assignedSchema ) {
				attributes.add(new SDFAttribute(nameToApply, attribute.getAttributeName(), attribute));
			}
			
			assignedSchema = new SDFSchema(getName() != null ? getName() : "", attributes);
			addParameterInfo("SCHEMA", convertSchema(attributes));
		}
	}
	
	private static String convertSchema(List<SDFAttribute> outputSchema) {
		if (outputSchema.isEmpty()) {
			return "[]";
		}

		final StringBuilder sb = new StringBuilder();
		sb.append("[");
		final SDFAttribute[] attributes = outputSchema.toArray(new SDFAttribute[0]);
		for (int i = 0; i < attributes.length; i++) {
			final SDFAttribute attribute = attributes[i];
			sb.append("['");
			sb.append(attribute.getAttributeName());
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
	
	
}
