package de.uniol.inf.is.odysseus.mining.cleaning.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.logicaloperator.OutputSchemaSettable;
import de.uniol.inf.is.odysseus.mining.cleaning.model.IDetection;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class DetectionSplitAO<T> extends BinaryLogicalOp implements OutputSchemaSettable {

	private static final long serialVersionUID = -2193273482190920976L;
	private SDFAttributeList outputschema;		
	private List<IDetection<T>> detections = new ArrayList<IDetection<T>>();

	public DetectionSplitAO(){
		
	}
	
	public DetectionSplitAO(DetectionSplitAO<T> detectionAO) {
		super(detectionAO);
	}

	@Override
	public DetectionSplitAO<T> clone() {
		return new DetectionSplitAO<T>(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return this.outputschema;
	}

	public void addDetection(IDetection<T> detection) {
		this.detections.add(detection);		
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
	
	public List<IDetection<T>> getDetections(){
		return this.detections;
	}
	
	
}
