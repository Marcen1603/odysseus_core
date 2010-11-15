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

	private PredictionExpression associationExpression;
	private String associationExpressionString;
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
		this.associationExpression = new PredictionExpression(this.associationExpressionString);
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

	public PredictionExpression getAssociationExpression() {
		return associationExpression;
	}

	public void setAssociationExpression(PredictionExpression associationExpression) {
		this.associationExpression = associationExpression;
	}

	public String getAssociationExpressionString() {
		return associationExpressionString;
	}

	public void setAssociationExpressionString(String associationExpressionString) {
		this.associationExpressionString = associationExpressionString;
	}

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

}
