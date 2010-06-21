package de.uniol.inf.is.odysseus.scars.objecttracking.prediction.logicaloperator;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListMetadataTypes;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.metadata.PredictionFunctionContainer;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class PredictionAO<M extends IProbability> extends BinaryLogicalOp {

	private static final long serialVersionUID = 1L;
	
	private PredictionFunctionContainer<M> predictionFunctions;
	private String timeAttributeName;
	private String detectedObjectListName;
	
	public PredictionAO() {
		super();
		predictionFunctions = new PredictionFunctionContainer<M>();
	}
	
	public PredictionAO(PredictionAO<M> copy) {
		super(copy);
		predictionFunctions = new PredictionFunctionContainer<M>(copy.getPredictionFunctions());
	}
	

	public void setPredictionFunctions(PredictionFunctionContainer<M> predictionFunctions) {
		this.predictionFunctions = predictionFunctions;
	}
	
	public PredictionFunctionContainer<M> getPredictionFunctions() {
		return predictionFunctions;
	}
	
	public void initNeededAttributeIndices(SDFAttributeList left, SDFAttributeList right, String timeAttr,String listAttr) {
		int timeIndex = 0;
		for(SDFAttribute attr : left) {
			if(attr.getAttributeName().equals(timeAttr)) {
				timeIndex = left.indexOf(attr);
			}
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void subscribeToSource(ILogicalOperator source, int sinkInPort, int sourceOutPort, SDFAttributeList inputSchema) {
		super.subscribeToSource(source, sinkInPort, sourceOutPort, inputSchema);
		if(sinkInPort == RIGHT) {
			SDFAttributeListExtended schema = (SDFAttributeListExtended)getInputSchema(RIGHT);
			Object metadata = schema.getMetadata(SDFAttributeListMetadataTypes.PREDICTION_FUNCTIONS);
			if(metadata instanceof PredictionFunctionContainer<?>) {
				setPredictionFunctions((PredictionFunctionContainer<M>)metadata);
			}
		}
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema(RIGHT);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new PredictionAO<M>(this);
	}
}
