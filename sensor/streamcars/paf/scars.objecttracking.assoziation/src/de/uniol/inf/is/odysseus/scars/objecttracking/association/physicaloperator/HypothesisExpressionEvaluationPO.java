package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IObjectTrackingLatency;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.PredictionExpression;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;

/**
 * @author Volker Janz
 *
 * @param <M>
 */

public class HypothesisExpressionEvaluationPO<M extends IProbability & IConnectionContainer & IObjectTrackingLatency> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private String scanObjListPath;
	private String predObjListPath;
	private PredictionExpression expression;
	private String expressionString;
	private SchemaHelper schemaHelper;
	private SchemaIndexPath predictedObjectListPath;
	private SchemaIndexPath scannedObjectListPath;

	public HypothesisExpressionEvaluationPO() {
		super();
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		return new HypothesisExpressionEvaluationPO<M>(this);
	}

	public HypothesisExpressionEvaluationPO(HypothesisExpressionEvaluationPO<M> clone) {
		super(clone);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		this.expression = new PredictionExpression(this.expressionString);
		this.schemaHelper = new SchemaHelper(getOutputSchema());

		this.setScannedObjectListPath(this.schemaHelper
				.getSchemaIndexPath(this.scanObjListPath));
		this.setPredictedObjectListPath(this.schemaHelper
				.getSchemaIndexPath(this.predObjListPath));
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		transfer(object);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		this.sendPunctuation(timestamp);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	/* SETTER AND GETTER */

	public SchemaHelper getSchemaHelper() {
		return schemaHelper;
	}

	public void setSchemaHelper(SchemaHelper schemaHelper) {
		this.schemaHelper = schemaHelper;
	}

	public SchemaIndexPath getPredictedObjectListPath() {
		return predictedObjectListPath;
	}

	public void setPredictedObjectListPath(SchemaIndexPath predictedObjectListPath) {
		this.predictedObjectListPath = predictedObjectListPath;
	}

	public SchemaIndexPath getScannedObjectListPath() {
		return scannedObjectListPath;
	}

	public void setScannedObjectListPath(SchemaIndexPath scannedObjectListPath) {
		this.scannedObjectListPath = scannedObjectListPath;
	}

	public String getScanObjListPath() {
		return scanObjListPath;
	}

	public void setScanObjListPath(String scanObjListPath) {
		this.scanObjListPath = scanObjListPath;
	}

	public String getPredObjListPath() {
		return predObjListPath;
	}

	public void setPredObjListPath(String predObjListPath) {
		this.predObjListPath = predObjListPath;
	}

	public PredictionExpression getExpression() {
		return expression;
	}

	public void setExpression(PredictionExpression expression) {
		this.expression = expression;
	}

	public String getExpressionString() {
		return expressionString;
	}

	public void setExpressionString(String expressionString) {
		this.expressionString = expressionString;
	}


}
