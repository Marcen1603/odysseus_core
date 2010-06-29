package de.uniol.inf.is.odysseus.logicaloperator.objectrelational.objecttracking;

import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

/**
 * @author Jendrik Poloczek
 */
public class ObjectTrackingNestAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	private SDFAttributeList outputSchema;
	private SDFAttributeList nestingAttributes;
	private SDFAttribute nestAttribute;
	private String nestAttributeName;

	public ObjectTrackingNestAO() {
	    super();
	}
	
	public ObjectTrackingNestAO(ObjectTrackingNestAO ao) {
		super(ao);
		this.outputSchema = ao.getOutputSchema().clone();
		this.nestingAttributes = ao.getNestingAttributes().clone();
		this.nestAttribute = ao.getNestAttribute().clone();
	}
	
	@Override
	public ObjectTrackingNestAO clone() {
		return new ObjectTrackingNestAO(this);
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		if(this.outputSchema == null) {
			this.outputSchema = calcOutputSchema();
		}
		return outputSchema;
	}

	/**
	 * creating a new SDFAttribute for new nesting attribute 
	 * 
	 * @param nestingAttributeName name of nesting attribute
	 */	
	public void setNestAttributeName(String nestAttributeName) {
		this.nestAttributeName = nestAttributeName;			
		this.nestAttribute = 
			new SDFAttribute(nestAttributeName);
	}
	
	/**
	 * modify input schema, removing attributes to nest, and 
	 * adding new nesting attribute.
	 */	
	private SDFAttributeList calcOutputSchema() {
	    SDFAttributeList outputSchema;
	    SDFAttributeList groupingAttributes;
	    SDFAttribute nestAttribute;
	    
	    groupingAttributes = new SDFAttributeList();

	    for(SDFAttribute attribute : this.getInputSchema()) {
	        if(!this.nestingAttributes.contains(attribute)) {
	            groupingAttributes.add(attribute.clone());
	        }
	    }
	    
	    nestAttribute = new SDFAttribute(this.nestAttributeName);	    
	    nestAttribute.setDatatype(SDFDatatypeFactory.getDatatype("Set"));
	    nestAttribute.setSubattributes(this.nestingAttributes);
	    
	    outputSchema = new SDFAttributeList();
	   
	    for(SDFAttribute groupingAttribute : groupingAttributes) {
	        outputSchema.add(groupingAttribute);
	    }
	    
	    outputSchema.add(nestAttribute);	    
	    return outputSchema;
	}
	
	public SDFAttributeList getNestingAttributes() {
		return nestingAttributes;
	}
	
	public SDFAttributeList getGroupingAttributes() {
	    SDFAttributeList groupingAttributes;
        groupingAttributes = new SDFAttributeList();

        for(SDFAttribute attribute : this.getInputSchema()) {
            if(!this.nestingAttributes.contains(attribute)) {
                groupingAttributes.add(attribute.clone());
            }
        }	    
        
        return groupingAttributes;
	}
	
	public SDFAttribute getNestAttribute() {
		return nestAttribute;
	}
	
	public void setNestingAttributes(SDFAttributeList nestingAttributes) {
		this.nestingAttributes = nestingAttributes.clone(); 
	}
}
