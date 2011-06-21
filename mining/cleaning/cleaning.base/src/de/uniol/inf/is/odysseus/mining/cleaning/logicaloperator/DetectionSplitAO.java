package de.uniol.inf.is.odysseus.mining.cleaning.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class DetectionSplitAO extends BinaryLogicalOp {

	private static final long serialVersionUID = -2193273482190920976L;
	
	private IPredicate<?> predicate;
	
	
	public DetectionSplitAO(IPredicate<?> predicate) {
		this.predicate = predicate;
	}
	
	public DetectionSplitAO(DetectionSplitAO detectionAO) {
		this.predicate = detectionAO.predicate;
	}

	@Override
	public DetectionSplitAO clone() {
		return new DetectionSplitAO(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return super.getInputSchema(RIGHT);		
	}

	
}
