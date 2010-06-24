package de.uniol.inf.is.odysseus.scars.objecttracking.prediction.logicaloperator;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListMetadataTypes;
import de.uniol.inf.is.odysseus.scars.objecttracking.OrAttributeResolver;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.metadata.PredictionFunctionContainer;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

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
	}
	
	public void initListPath(SDFAttributeList inputSchema, String[] listAttributeNamePath) {
		pathToList = OrAttributeResolver.resolveIndices(inputSchema, listAttributeNamePath);
	}
	
	public int[] getPathToList() {
		return pathToList;
	}
	
	public void setDefaultPredictionFunction(SDFExpression[] defaultPredictionFunction) {
		predictionFunctions.setDefaultPredictionFunction(defaultPredictionFunction);
	}
	
	public void setPredictionFunction(SDFExpression[] expressions, IPredicate<MVRelationalTuple<M>> predicate) {
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
