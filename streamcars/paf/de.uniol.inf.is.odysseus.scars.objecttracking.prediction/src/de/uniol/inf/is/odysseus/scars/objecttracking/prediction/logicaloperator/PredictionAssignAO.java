package de.uniol.inf.is.odysseus.scars.objecttracking.prediction.logicaloperator;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListMetadataTypes;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.PredictionExpression;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.metadata.PredictionFunctionContainer;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class PredictionAssignAO<M extends IProbability> extends UnaryLogicalOp {
	
	private static final long serialVersionUID = 1L;
	
	private PredictionFunctionContainer<M> predictionFunctions;
	private int[] pathToList;

	
	public PredictionAssignAO() {
		super();
		predictionFunctions = new PredictionFunctionContainer<M>();
	}
	
	public PredictionAssignAO(PredictionAssignAO<M> predictionAO) {
		super(predictionAO);
		predictionFunctions = new PredictionFunctionContainer<M>(predictionAO.getPredictionFunctions());
		pathToList = predictionAO.getPathToList();
	}
	
	public void initListPath(SDFAttributeList inputSchema, String absoluteListNamePath) {
		pathToList = OrAttributeResolver.getAttributePath(inputSchema, absoluteListNamePath);
	}
	
	public int[] getPathToList() {
		return pathToList;
	}
	
	public void setDefaultPredictionFunction(PredictionExpression[] defaultPredictionFunction) {
		predictionFunctions.setDefaultPredictionFunction(defaultPredictionFunction);
	}
	
	public void setPredictionFunction(PredictionExpression[] expressions, IPredicate<MVRelationalTuple<M>> predicate) {
		predictionFunctions.setPredictionFunction(expressions, predicate);
	}
	
	public PredictionFunctionContainer<M> getPredictionFunctions() {
		return predictionFunctions;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new PredictionAssignAO<M>(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		SDFAttributeListExtended outputSchema = new SDFAttributeListExtended(this.getInputSchema());
		outputSchema.setMetadata(SDFAttributeListMetadataTypes.PREDICTION_FUNCTIONS, predictionFunctions);
		return outputSchema;
	}

}
