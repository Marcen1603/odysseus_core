package de.uniol.inf.is.odysseus.logicaloperator.objectrelational;

import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Jendrik Poloczek
 */

public class UnnestAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	private SDFAttributeList outputSchema = null;
	private SDFAttribute nestingAttribute = null;

	public UnnestAO() {
		super();
	}
	
	public UnnestAO(UnnestAO ao) {
		super(ao);
		this.outputSchema = ao.getOutputSchema();
		this.nestingAttribute = ao.getNestingAttribute();
	}
	
	@Override
	public UnnestAO clone() {
		return new UnnestAO(this);
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		if(outputSchema == null) 
			calcOutputSchema();
		return outputSchema;
	}

	public void setNestingAttribute(SDFAttribute nestingAttribute) {
		nestingAttribute = nestingAttribute.clone();
	}
	
	/**
	 * modify input schema, adding attributes of nesting, and 
	 * remove nesting attribute.
	 */	
	
	private void calcOutputSchema() {
		SDFAttributeList inputSchema = getInputSchema();
		outputSchema = inputSchema.clone();
		// outputSchema = SDFAttributeList.difference(inputSchema, toNestAttributes);
		// outputSchema.addAttribute(nestingAttribute);
	}
	
	public SDFAttribute getNestingAttribute() {
		return nestingAttribute;
	}
}
