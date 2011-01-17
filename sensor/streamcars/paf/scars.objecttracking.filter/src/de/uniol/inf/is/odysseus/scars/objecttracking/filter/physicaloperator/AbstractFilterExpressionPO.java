package de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IObjectTrackingLatency;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IStreamCarsExpression;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsExpression;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public abstract class AbstractFilterExpressionPO<M extends IProbability & IObjectTrackingLatency & IConnectionContainer> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private String predictedObjectListPath;
	private String scannedObjectListPath;
	
	private SchemaIndexPath predictedObjectListSIPath;
	private SchemaIndexPath scannedObjectListSIPath;
	
	private SDFAttributeList inputSchema;
	
	private SchemaHelper schemaHelper;
	
	private String expressionString;
	protected IStreamCarsExpression expression;

	private String[] restrictedVariables;
	
	public void setExpression(IStreamCarsExpression expression) {
		this.expression = expression;
	}

	public AbstractFilterExpressionPO() {
		super();
	}

	public AbstractFilterExpressionPO(AbstractFilterExpressionPO<M> copy) {
		super(copy);
		this.setExpressionString(copy.getExpressionString());
		this.setPredictedObjectListPath(copy.getPredictedObjectListPath());
		this.setPredictedObjectListSIPath(copy.getPredictedObjectListSIPath().clone());
		this.setScannedObjectListPath(copy.getScannedObjectListPath());
		this.setScannedObjectListSIPath(copy.getScannedObjectListSIPath().clone());
		// TODO clone?
		this.setExpression(copy.getExpression());
		this.setInputSchema(copy.getInputSchema().clone());
	}
	
	protected void process_open() throws OpenFailedException {
		if(expressionString != null) {
			this.expressionString = this.expressionString.replace("'", "");
			expression = new StreamCarsExpression(expressionString);
			expression.init(getOutputSchema());
		}

		
		this.schemaHelper = new SchemaHelper(getOutputSchema());

		this.setScannedObjectListSIPath(this.schemaHelper.getSchemaIndexPath(this.getScannedObjectListPath()));
		this.setPredictedObjectListSIPath(this.schemaHelper.getSchemaIndexPath(this.getPredictedObjectListPath()));
		
		this.setInputSchema(this.getSubscribedToSource(0).getTarget().getOutputSchema());
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		object.getMetadata().setObjectTrackingLatencyStart();
		object = computeAll(object);
		// transfer to broker
		object.getMetadata().setObjectTrackingLatencyEnd();
		transfer(object);
	}

	public abstract MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object);

	@Override
	public abstract AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone();

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	// Getter & Setter
	
	public IStreamCarsExpression getExpression() {
		return expression;
	}
	
	public void setExpressionString(String expressionString) {
		this.expressionString = expressionString;
	}
	
	public String getExpressionString() {
		return expressionString;
	}

	public void setPredictedObjectListPath(String predictedObjectListPath) {
		this.predictedObjectListPath = predictedObjectListPath;
	}

	public String getPredictedObjectListPath() {
		return predictedObjectListPath;
	}

	public void setScannedObjectListPath(String scannedObjectListPath) {
		this.scannedObjectListPath = scannedObjectListPath;
	}

	public String getScannedObjectListPath() {
		return scannedObjectListPath;
	}

	public void setPredictedObjectListSIPath(SchemaIndexPath predictedObjectListSIPath) {
		this.predictedObjectListSIPath = predictedObjectListSIPath;
	}

	public SchemaIndexPath getPredictedObjectListSIPath() {
		return predictedObjectListSIPath;
	}

	public void setScannedObjectListSIPath(SchemaIndexPath scannedObjectListSIPath) {
		this.scannedObjectListSIPath = scannedObjectListSIPath;
	}

	public SchemaIndexPath getScannedObjectListSIPath() {
		return scannedObjectListSIPath;
	}

	/**
	 * @param inputSchema the inputSchema to set
	 */
	public void setInputSchema(SDFAttributeList inputSchema) {
		this.inputSchema = inputSchema;
	}

	/**
	 * @return the inputSchema
	 */
	public SDFAttributeList getInputSchema() {
		return inputSchema;
	}
}
