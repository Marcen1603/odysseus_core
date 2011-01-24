package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.CovarianceExpressionHelper;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.CovarianceHelper;
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
 * <p>
 * Physical operator for the <i>expression based</i> rating of each possible
 * pair of predicted and scanned objects. The expressions is set within the
 * query. To rate only existing connections HypothesisExpressionEvaluationPO (
 * {@link HypothesisExpressionEvaluationPO}) should be used.
 * </p>
 * 
 * @author Volker Janz
 */

public class HypothesisExpressionGatingPO<M extends IProbability & IConnectionContainer & IObjectTrackingLatency> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private StreamCarsExpression expression;
	private SchemaHelper schemaHelper;
	private String predictedObjectListPath;
	private String scannedObjectListPath;
	private String expressionString;
	private SchemaIndexPath predictedObjectListSIPath;
	private SchemaIndexPath scannedObjectListSIPath;

	private CovarianceHelper covarianceHelper;
	private CovarianceExpressionHelper covHelper;

	private static final String EXP_ASSOCIATION_CONNECTION_VALUE = "ASSOCIATION_CON_VAL";
	private static final String METADATA_COV = "COVARIANCE";

	public HypothesisExpressionGatingPO() {
		super();
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		return new HypothesisExpressionGatingPO<M>(this);
	}

	public HypothesisExpressionGatingPO(HypothesisExpressionGatingPO<M> clone) {
		super(clone);
		this.expression = clone.expression;
		this.schemaHelper = clone.schemaHelper;
		this.predictedObjectListPath = clone.predictedObjectListPath;
		this.scannedObjectListPath = clone.scannedObjectListPath;
		this.predictedObjectListSIPath = clone.predictedObjectListSIPath;
		this.scannedObjectListSIPath = clone.scannedObjectListSIPath;
		this.covarianceHelper = clone.covarianceHelper;
		this.covHelper = clone.covHelper;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		this.expressionString = this.expressionString.replace("'", "");
		this.expression = new StreamCarsExpression(this.expressionString);
		this.expression.init(this.getOutputSchema());
		this.schemaHelper = new SchemaHelper(getOutputSchema());

		this.schemaHelper = new SchemaHelper(getOutputSchema());
		this.setScannedObjectListSIPath(this.schemaHelper.getSchemaIndexPath(this.scannedObjectListPath));
		this.setPredictedObjectListSIPath(this.schemaHelper.getSchemaIndexPath(this.predictedObjectListPath));

		// this.scannedObjectListSIPath.getLastSchemaIndex().getAttribute().getSubattributes();
		this.covarianceHelper = new CovarianceHelper(this.expression, this.getOutputSchema());
		covHelper = new CovarianceExpressionHelper();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		TupleIndexPath scannedTupleIndexPath = this.getScannedObjectListSIPath().toTupleIndexPath(object);
		TupleIndexPath predictedTupleIndexPath = this.getPredictedObjectListSIPath().toTupleIndexPath(object);
		TupleHelper tupleHelper = new TupleHelper(object);
		ConnectionList newObjConList = new ConnectionList();

		TupleIndexPath absScanTupleIndexPath;
		TupleIndexPath absPredTupleIndexPath;

		int scanCount = ((MVRelationalTuple<M>) scannedTupleIndexPath.getTupleObject()).getAttributeCount();
		int predCount = ((MVRelationalTuple<M>) predictedTupleIndexPath.getTupleObject()).getAttributeCount();

		for (int i = 0; i < scanCount; i++) {

			for (int j = 0; j < predCount; j++) {

				// getRatingForElementPair returns -1 if there is no
				// connection for the given objects
				absScanTupleIndexPath = scannedTupleIndexPath.appendClone(i);
				absPredTupleIndexPath = predictedTupleIndexPath.appendClone(j);
				double currentRating = object.getMetadata().getConnectionList().getRatingForElementPair(absScanTupleIndexPath, absPredTupleIndexPath);

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
							var.replaceVaryingIndex(i);
							var.bindTupleValue(object);
						} else if (var.isInList(predictedTupleIndexPath)) {
							var.replaceVaryingIndex(j);
							var.bindTupleValue(object);
						}
					} else if (var.hasMetadataInfo()) { // Metadaten Variable
						if (var.isInList(scannedTupleIndexPath)) {
							var.replaceVaryingIndex(i);
							Object val = tupleHelper.getObject(var.getPath());
							String metaDataInfo = var.getMetadataInfo();
							if (metaDataInfo.equals(METADATA_COV)) {
								var.bind(covHelper.getCovarianceForExpressionVariables(this.expression, ((MVRelationalTuple<M>) val).getMetadata()));
//								var.bind(this.covarianceHelper.getCovarianceForAttributes(((MVRelationalTuple<M>) val).getMetadata().getCovariance()));
							}
						} else if (var.isInList(predictedTupleIndexPath)) {
							var.replaceVaryingIndex(j);
							Object val = tupleHelper.getObject(var.getPath());
							String metaDataInfo = var.getMetadataInfo();
							if (metaDataInfo.equals(METADATA_COV)) {
								var.bind(covHelper.getCovarianceForExpressionVariables(this.expression, ((MVRelationalTuple<M>) val).getMetadata()));
//								var.bind(this.covarianceHelper.getCovarianceForAttributes(((MVRelationalTuple<M>) val).getMetadata().getCovariance()));
							}
						}
					}
				}
				expression.evaluate();

				double newRating = expression.getDoubleValue();

				if (newRating > 0) {
					if (currentRating == -1) {
						newObjConList.add(new Connection(absScanTupleIndexPath, absPredTupleIndexPath, newRating));
					} else if (newRating != currentRating) {
						newObjConList.add(new Connection(absScanTupleIndexPath, absPredTupleIndexPath, newRating));
					}
				}

			}
		}

		object.getMetadata().setConnectionList(newObjConList);
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

	public String getExpressionString() {
		return expressionString;
	}

	public void setExpressionString(String expressionString) {
		this.expressionString = expressionString;
	}

	public CovarianceHelper getCovarianceHelper() {
		return covarianceHelper;
	}

	public void setCovarianceHelper(CovarianceHelper covarianceHelper) {
		this.covarianceHelper = covarianceHelper;
	}
}
