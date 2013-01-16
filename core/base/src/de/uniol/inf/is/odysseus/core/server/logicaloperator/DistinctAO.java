package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * 
 * @author Marco Grawunder
 *
 */
@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="DISTINCT", doc="This operator removes duplicates.")
public class DistinctAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -1992998023364461468L;

	private SDFSchema attributes;
	
	public DistinctAO(DistinctAO distinctAO) {
		super(distinctAO);
		this.attributes = distinctAO.attributes;
	}
	
	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTR", isList = true, optional=true)
	public void setAttr(List<SDFAttribute> outputSchema) {
		this.attributes = new SDFSchema("", outputSchema);
	}

	public SDFSchema getAttributes() {
		return attributes;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new DistinctAO(this);
	}

}
