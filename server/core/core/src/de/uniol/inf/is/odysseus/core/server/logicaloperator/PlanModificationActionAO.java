package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

@LogicalOperator(name="PlanModificationAction", maxInputPorts=1, minInputPorts=1, doc="Executes plan modifications based on receiving tuple data", category = { LogicalOperatorCategory.PLAN})
public class PlanModificationActionAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -578154679444642283L;

	private SDFAttribute commandAttribute;
	private SDFAttribute queryIDAttribute;
	
	public PlanModificationActionAO() {
		super();
	}
	
	public PlanModificationActionAO( PlanModificationActionAO copy ) {
		super(copy);
		
		commandAttribute = copy.commandAttribute.clone();
		queryIDAttribute = copy.queryIDAttribute.clone();
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new PlanModificationActionAO(this);
	}

	@Parameter(name="CommandAttribute", type = ResolvedSDFAttributeParameter.class, doc="Attribute to read the plan modification commands")
	public void setCommandAttribute( SDFAttribute attribute ) {
		commandAttribute = attribute;
	}
	
	public SDFAttribute getCommandAttribute() {
		return commandAttribute;
	}

	@Parameter(name="QueryIDAttribute", type = ResolvedSDFAttributeParameter.class, doc="Attribute to read the query id to execute the commands on")
	public void setQueryIDAttribute( SDFAttribute attribute ) {
		queryIDAttribute = attribute;
	}
	
	public SDFAttribute getQueryIDAttribute() {
		return queryIDAttribute;
	}
	
	@Override
	public boolean isValid() {
		clearErrors();
		
		if( !isStringAttribute(commandAttribute)) {
			addError("CommandAttribute must be of type STRING, not " + commandAttribute.getDatatype().getQualName());
		}
		
		if( !isNumericAttribute(queryIDAttribute)) {
			addError("QueryIDAttribute must be of numeric type (e.g., INTEGER), not " + queryIDAttribute.getDatatype().getQualName());
		}
		
		return getErrors().isEmpty();
	}

	private static boolean isStringAttribute(SDFAttribute attribute) {
		return attribute.getDatatype().isString();
	}
	
	private static boolean isNumericAttribute(SDFAttribute attribute) {
		return attribute.getDatatype().isNumeric();
	}
}
