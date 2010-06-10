package de.uniol.inf.is.odysseus.logicaloperator.objectrelational;

import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * NestAO
 * 
 * v[(A1,...,AN);N](S) where A1...AN are attributes and tuples are nested 
 * when they're equal in attributes except A1...AN. N is the nest attribute
 * and S is the stream.
 * 
 * @author Jendrik Poloczek
 */
public class NestAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	private SDFAttributeList outputSchema = null;
	private SDFAttributeList nestingAttributes = null;
	private SDFAttribute nestAttribute = null;

	public NestAO() {	    
		super();
	}
	
	public NestAO(NestAO ao) {
		super(ao);
		this.outputSchema = ao.getOutputSchema().clone();
		this.nestingAttributes = ao.getNestingAttributes().clone();
		this.nestAttribute = ao.getNestAttribute().clone();
	}
	
	@Override
	public NestAO clone() {
		return new NestAO(this);
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		if(this.outputSchema == null) {
		    SDFAttributeList inputSchema = this.getInputSchema();
			this.outputSchema = calcOutputSchema(inputSchema);
		}
		return outputSchema;
	}

	/**
	 * creating a new SDFAttribute for new nesting attribute 
	 * 
	 * @param nestingAttributeName name of nesting attribute
	 */	
	public void setNestAttributeName(String nestingAttributeName) {
		nestAttribute = new SDFAttribute(nestingAttributeName);
	}
	
	/**
	 * modify input schema, removing attributes to nest, and 
	 * adding new nesting attribute.
	 */	
	private SDFAttributeList calcOutputSchema(SDFAttributeList inputSchema) {
	    SDFAttributeList outputSchema = new SDFAttributeList();
	    return outputSchema;
	}
	
	public SDFAttributeList getNestingAttributes() {
		return nestingAttributes;
	}
	
	public SDFAttribute getNestAttribute() {
		return nestAttribute;
	}
	
	public void setNestingAttributes(SDFAttributeList toNestAttributes) {
		toNestAttributes = toNestAttributes.clone(); 
	}
}
