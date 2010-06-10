package de.uniol.inf.is.odysseus.logicaloperator.objectrelational;

import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * UnnestAO 
 * 
 * u[A](S) where A is attribute name of nest to unnest and S is 
 * equal to the stream. 
 * 
 * @author Jendrik Poloczek
 */
public class UnnestAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	private SDFAttributeList outputSchema = null;
	private SDFAttribute nestAttribute = null;

	public UnnestAO() {
		super();
	}
	
	public UnnestAO(UnnestAO ao) {
		super(ao);
		this.outputSchema = ao.getOutputSchema();
		this.nestAttribute = ao.getNestAttribute();
	}
	
	@Override
	public UnnestAO clone() {
		return new UnnestAO(this);
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		if(outputSchema == null) 
			calcOutputSchema(this.getInputSchema());
		return outputSchema;
	}

	public void setNestAttribute(SDFAttribute nestingAttribute) {
		nestingAttribute = nestingAttribute.clone();
	}
	
	/**
	 * modify input schema, adding attributes of nesting, and 
	 * remove nesting attribute.
	 */	
	
	private SDFAttributeList calcOutputSchema(SDFAttributeList inputSchema) {
		outputSchema = inputSchema.clone();
		return outputSchema;
	}
	
	public SDFAttribute getNestAttribute() {
		return nestAttribute;
	}
}
