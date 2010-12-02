package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.CovarianceHelper;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IObjectTrackingLatency;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IStreamCarsExpressionVariable;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsExpression;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsExpressionVariable;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleHelper;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;

/**
 * @author Volker Janz
 *
 * @param <M>
 */

public class HypothesisExpressionEvaluationPO<M extends IProbability & IConnectionContainer & IObjectTrackingLatency> extends
		AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private StreamCarsExpression expression;
	private String expressionString;
	private SchemaHelper schemaHelper;
	private String predictedObjectListPath;
	private String scannedObjectListPath;
	private SchemaIndexPath predictedObjectListSIPath;
	private SchemaIndexPath scannedObjectListSIPath;

	private CovarianceHelper covarianceHelper;

	private static final String EXP_ASSOCIATION_CONNECTION_VALUE = "ASSOCIATION_CON_VAL";
	private static final String METADATA_COV = "COVARIANCE";

	public HypothesisExpressionEvaluationPO() {
		super();
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		return new HypothesisExpressionEvaluationPO<M>(this);
	}

	public HypothesisExpressionEvaluationPO(HypothesisExpressionEvaluationPO<M> clone) {
		super(clone);
		this.expression = clone.expression;
		this.expressionString = clone.expressionString;
		this.schemaHelper = clone.schemaHelper;
		this.predictedObjectListPath = clone.predictedObjectListPath;
		this.scannedObjectListPath = clone.scannedObjectListPath;
		this.predictedObjectListSIPath = clone.predictedObjectListSIPath;
		this.scannedObjectListSIPath = clone.scannedObjectListSIPath;
		this.covarianceHelper = clone.covarianceHelper;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		this.expressionString = this.expressionString.replace("'", "");
		this.expression = new StreamCarsExpression(this.expressionString);
		this.expression.init(this.getOutputSchema());
		this.schemaHelper = new SchemaHelper(getOutputSchema());

		this.setScannedObjectListSIPath(this.schemaHelper.getSchemaIndexPath(this.scannedObjectListPath));
		this.setPredictedObjectListSIPath(this.schemaHelper.getSchemaIndexPath(this.predictedObjectListPath));

		this.covarianceHelper = new CovarianceHelper(this.expression, this.getOutputSchema());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		TupleIndexPath scannedTupleIndexPath = this.getScannedObjectListSIPath().toTupleIndexPath(object);
		TupleIndexPath predictedTupleIndexPath = this.getPredictedObjectListSIPath().toTupleIndexPath(object);

		TupleHelper tupleHelper = new TupleHelper(object);

		for (IConnection con : object.getMetadata().getConnectionList()) {

			double currentRating = con.getRating();

			for (IStreamCarsExpressionVariable ivar : expression.getVariables()) {
				StreamCarsExpressionVariable var = (StreamCarsExpressionVariable) ivar;

				if (!var.isSchemaVariable() && var.getName().equals(EXP_ASSOCIATION_CONNECTION_VALUE)) { // "Custom" Variable
					if (currentRating != -1) {
						var.bind(currentRating);
					} else {
						var.bind(0);
					}
				} else if (var.isSchemaVariable() && !var.hasMetadataInfo()) { // "Normale" Variable
					if (var.isInList(scannedTupleIndexPath)) {
						var.replaceVaryingIndex(con.getLeftPath().getLastTupleIndex().toInt());
						var.bindTupleValue(object);
					} else if (var.isInList(predictedTupleIndexPath)) {
						var.replaceVaryingIndex(con.getRightPath().getLastTupleIndex().toInt());
						var.bindTupleValue(object);
					}
				} else if (var.hasMetadataInfo()) { // Metadaten Variable
					if (var.isInList(scannedTupleIndexPath.toArray())) {
						var.replaceVaryingIndex(con.getLeftPath().getLastTupleIndex().toInt());
						Object val = tupleHelper.getObject(var.getPath());
						String metaDataInfo = var.getMetadataInfo();
						if (metaDataInfo.equals(METADATA_COV)) {
							var.bind(this.covarianceHelper.getCovarianceForAttributes(((MVRelationalTuple<M>) val).getMetadata().getCovariance()));
						}
					} else if (var.isInList(predictedTupleIndexPath.toArray())) {
						var.replaceVaryingIndex(con.getRightPath().getLastTupleIndex().toInt());
						Object val = tupleHelper.getObject(var.getPath());
						String metaDataInfo = var.getMetadataInfo();
						if (metaDataInfo.equals(METADATA_COV)) {
							var.bind(this.covarianceHelper.getCovarianceForAttributes(((MVRelationalTuple<M>) val).getMetadata().getCovariance()));
						}
					}
				}
			}
			expression.evaluate();

			con.setRating(expression.getDoubleValue());
		}

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

	public StreamCarsExpression getExpression() {
		return expression;
	}

	public void setExpression(StreamCarsExpression expression) {
		this.expression = expression;
	}

	public String getExpressionString() {
		return expressionString;
	}

	public void setExpressionString(String expressionString) {
		this.expressionString = expressionString;
	}

	public SchemaHelper getSchemaHelper() {
		return schemaHelper;
	}

	public void setSchemaHelper(SchemaHelper schemaHelper) {
		this.schemaHelper = schemaHelper;
	}

	public String getPredictedObjectListPath() {
		return predictedObjectListPath;
	}

	public void setPredictedObjectListPath(String predictedObjectListPath) {
		this.predictedObjectListPath = predictedObjectListPath;
	}

	public String getScannedObjectListPath() {
		return scannedObjectListPath;
	}

	public void setScannedObjectListPath(String scannedObjectListPath) {
		this.scannedObjectListPath = scannedObjectListPath;
	}

	public SchemaIndexPath getPredictedObjectListSIPath() {
		return predictedObjectListSIPath;
	}

	public void setPredictedObjectListSIPath(SchemaIndexPath predictedObjectListSIPath) {
		this.predictedObjectListSIPath = predictedObjectListSIPath;
	}

	public SchemaIndexPath getScannedObjectListSIPath() {
		return scannedObjectListSIPath;
	}

	public void setScannedObjectListSIPath(SchemaIndexPath scannedObjectListSIPath) {
		this.scannedObjectListSIPath = scannedObjectListSIPath;
	}
}
