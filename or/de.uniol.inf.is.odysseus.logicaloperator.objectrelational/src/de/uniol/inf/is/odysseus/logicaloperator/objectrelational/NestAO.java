package de.uniol.inf.is.odysseus.logicaloperator.objectrelational;

import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Jendrik Poloczek
 */

public class NestAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	private SDFAttributeList outputSchema = null;
	private SDFAttributeList toNestAttributes = null;
	private SDFAttribute nestingAttribute = null;

	public NestAO() {
		super();
	}
	
	public NestAO(NestAO ao) {
		super(ao);
		this.outputSchema = ao.getOutputSchema();
		this.toNestAttributes = ao.getToNestAttributes();
		this.nestingAttribute = ao.getNestingAttribute();
	}
	
	@Override
	public NestAO clone() {
		return new NestAO(this);
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		if(outputSchema == null) 
			calcOutputSchema();
		return outputSchema;
	}

	/**
	 * creating a new SDFAttribute for new nesting attribute 
	 * 
	 * @param nestingAttributeName name of nesting attribute
	 */	
	public void setNestingAttributeName(String nestingAttributeName) {
		nestingAttribute = new SDFAttribute(nestingAttributeName);
	}
	
	/**
	 * modify input schema, removing attributes to nest, and 
	 * adding new nesting attribute.
	 */	
	private void calcOutputSchema() {
		SDFAttributeList inputSchema = getInputSchema();
		outputSchema = inputSchema.clone();
		// outputSchema = SDFAttributeList.difference(inputSchema, toNestAttributes);
		// outputSchema.addAttribute(nestingAttribute);
	}
	
	public SDFAttributeList getToNestAttributes() {
		return toNestAttributes;
	}
	
	public SDFAttribute getNestingAttribute() {
		return nestingAttribute;
	}
	
	public void setToNestAttributes(SDFAttributeList toNestAttributes) {
		toNestAttributes = toNestAttributes.clone(); 
	}
	

}
