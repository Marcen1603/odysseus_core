package de.uniol.inf.is.odysseus.mining.cleaning.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.logicaloperator.OutputSchemaSettable;
import de.uniol.inf.is.odysseus.mining.cleaning.model.IDetection;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class DetectionSplitAO extends BinaryLogicalOp implements OutputSchemaSettable {

	private static final long serialVersionUID = -2193273482190920976L;
	private SDFAttributeList outputschema;		

	public DetectionSplitAO(){
		
	}
	
	public DetectionSplitAO(IPredicate<?> predicate) {
		super.setPredicate(predicate);
	}
	
	public DetectionSplitAO(DetectionSplitAO detectionAO) {
		super(detectionAO);
	}

	@Override
	public DetectionSplitAO clone() {
		return new DetectionSplitAO(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return this.outputschema;
	}

	public void addDetection(IDetection detection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		this.outputschema = outputSchema;
		
	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema, int port) {
		if(port>0){
			throw new UnsupportedOperationException("no ports greater than 0 supported");
		}
		setOutputSchema(outputSchema);
		
	}
	
	
}
