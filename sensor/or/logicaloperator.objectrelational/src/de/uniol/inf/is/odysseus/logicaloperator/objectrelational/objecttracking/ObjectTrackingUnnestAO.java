package de.uniol.inf.is.odysseus.logicaloperator.objectrelational.objecttracking;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListMetadataTypes;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * ObjectTrackingUnnestAO
 * 
 * @author Jendrik Poloczek
 */
public class ObjectTrackingUnnestAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	private SDFAttributeListExtended outputSchema;
	private SDFAttribute nestAttribute;

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
	public SDFAttributeListExtended getOutputSchema() {
		if(outputSchema == null) 
			this.outputSchema = calcOutputSchema(this.getInputSchema());
		return outputSchema;
	}

	public void setNestAttribute(SDFAttribute nestAttribute) {
		this.nestAttribute = nestAttribute.clone();		
	}
	
	/**
	 * modify input schema, adding attributes of nesting, and 
	 * remove nesting attribute.
	 */	
	
	private SDFAttributeListExtended calcOutputSchema(
		SDFAttributeList sdfAttributeList
	) {
	    SDFAttributeListExtended outputSchema;
	    SDFAttributeList subAttributes; 	   
	    
	    outputSchema = new SDFAttributeListExtended();
	    subAttributes = this.nestAttribute.getSubattributes();
	    	    
	    for(SDFAttribute attribute : sdfAttributeList) {
	        if(!attribute.equals(this.nestAttribute)) {
	            outputSchema.add(attribute);
	        }
	    }
	    
		outputSchema.addAll(subAttributes);
		
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
		
		return outputSchema;
	}
	
	public SDFAttribute getNestAttribute() {
		return this.nestAttribute;
	}
}
