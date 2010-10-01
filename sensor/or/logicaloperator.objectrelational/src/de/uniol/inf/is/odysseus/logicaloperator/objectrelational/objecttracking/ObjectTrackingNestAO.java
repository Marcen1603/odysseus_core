package de.uniol.inf.is.odysseus.logicaloperator.objectrelational.objecttracking;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListMetadataTypes;
import de.uniol.inf.is.odysseus.predicate.IPredicate;

/**
 * ObjectTrackingNestAO
 * 
 * @author Jendrik Poloczek
 */
public class ObjectTrackingNestAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	private SDFAttributeListExtended outputSchema;
	private SDFAttributeListExtended nestingAttributes;
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
	public SDFAttributeListExtended getOutputSchema() {
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
		
		for(SDFAttribute nestingAttribute : this.nestingAttributes) {
			this.nestAttribute.addSubattribute(nestingAttribute);
		}
	}
	
	/**
	 * modify input schema, removing attributes to nest, and 
	 * adding new nesting attribute.
	 */	
	@SuppressWarnings("unchecked")
	private SDFAttributeListExtended calcOutputSchema() {
	    SDFAttributeListExtended outputSchema;
	    SDFAttributeListExtended groupingAttributes;
	    SDFAttribute nestAttribute;
	    
	    groupingAttributes = new SDFAttributeListExtended();

	    for(SDFAttribute attribute : this.getInputSchema()) {
	        if(!this.nestingAttributes.contains(attribute)) {
	            groupingAttributes.add(attribute.clone());
	        }
	    }
	    
	    nestAttribute = new SDFAttribute(this.nestAttributeName);	    
	    nestAttribute.setDatatype(SDFDatatypeFactory.getDatatype("Set"));
	    nestAttribute.setSubattributes(this.nestingAttributes);
	    
	    outputSchema = new SDFAttributeListExtended();
	    
	    /*
	     * For the use of SDFAttributeListExtended for various 
	     * ObjectTracking operators prediction functions have to be
	     * specified as metadata in the schema. For evaluating 
	     * purpose prediction functions are empty.
	     */
	    	    
	    outputSchema.setMetadata(
	    	SDFAttributeListMetadataTypes.PREDICTION_FUNCTIONS, 
	    	new HashMap<IPredicate, IPredictionFunction>()
    	);
	   
	    for(SDFAttribute groupingAttribute : groupingAttributes) {
	        outputSchema.add(groupingAttribute);
	    }
	    
	    outputSchema.add(nestAttribute);	    
	    return outputSchema;
	}
	
	public SDFAttributeListExtended getNestingAttributes() {
		return this.nestingAttributes;
	}
	
	public SDFAttributeListExtended getGroupingAttributes() {
	    SDFAttributeListExtended groupingAttributes;
        groupingAttributes = new SDFAttributeListExtended();

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
	
	public void setNestingAttributes(
		SDFAttributeListExtended nestingAttributes
	) {
		this.nestingAttributes = nestingAttributes.clone(); 
	}
}
