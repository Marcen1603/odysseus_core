package de.uniol.inf.is.odysseus.logicaloperator.objectrelational.objecttracking;

import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * UnnestAO 
 * 
 * @author Jendrik Poloczek
 */
public class ObjectTrackingUnnestAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	private SDFAttributeList outputSchema = null;
	private SDFAttribute nestAttribute = null;

	public ObjectTrackingUnnestAO() {
		super();
	}
	
	public ObjectTrackingUnnestAO(ObjectTrackingUnnestAO ao) {
		super(ao);
		this.outputSchema = ao.getOutputSchema();
		this.nestAttribute = ao.getNestAttribute();
	}
	
	@Override
	public ObjectTrackingUnnestAO clone() {
		return new ObjectTrackingUnnestAO(this);
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		if(outputSchema == null) 
			calcOutputSchema(this.getInputSchema());
		return outputSchema;
	}

	public void setNestAttribute(SDFAttribute nestAttribute) {
		this.nestAttribute = nestAttribute.clone();
	}
	
	/**
	 * modify input schema, adding attributes of nesting, and 
	 * remove nesting attribute.
	 */	
	
	private SDFAttributeList calcOutputSchema(SDFAttributeList inputSchema) {
	    SDFAttributeList outputSchema;
	    SDFAttributeList subAttributes; 	   
	    
	    outputSchema = new SDFAttributeList();
	    subAttributes = this.nestAttribute.getSubattributes();
	    
	    for(SDFAttribute attribute : inputSchema) {
	        if(!attribute.equals(this.nestAttribute)) {
	            outputSchema.add(attribute);
	        }
	    }
	    
		outputSchema.addAll(subAttributes);
		System.out.println(outputSchema);
		return outputSchema;
	}
	
	public SDFAttribute getNestAttribute() {
		return nestAttribute;
	}
}
