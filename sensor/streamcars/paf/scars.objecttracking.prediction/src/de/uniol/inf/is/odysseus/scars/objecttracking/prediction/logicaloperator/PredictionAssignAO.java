package de.uniol.inf.is.odysseus.scars.objecttracking.prediction.logicaloperator;

import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListMetadataTypes;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.PredictionExpression;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf.metadata.PredictionFunctionContainer;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
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
		this.pathToList = new int[predictionAO.pathToList.length];
		System.arraycopy(predictionAO.pathToList, 0, this.pathToList, 0, predictionAO.pathToList.length);
	}
	
	public void initListPath(SDFAttributeList inputSchema, String absoluteListNamePath) {
		SchemaHelper helper = new SchemaHelper(inputSchema);
		pathToList = helper.getSchemaIndexPath(absoluteListNamePath).toArray();
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
