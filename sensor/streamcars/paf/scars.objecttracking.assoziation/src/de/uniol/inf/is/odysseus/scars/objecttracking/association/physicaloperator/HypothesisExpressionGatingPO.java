package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IObjectTrackingLatency;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.PredictionExpression;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleHelper;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;

/**
 * @author Volker Janz
 *
 * @param <M>
 */

public class HypothesisExpressionGatingPO<M extends IProbability & IConnectionContainer & IObjectTrackingLatency> extends
		AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private String scanObjListPath;
	private String predObjListPath;
	private PredictionExpression expression;
	private String expressionString;
	private SchemaHelper schemaHelper;
	private String predictedObjectListPath;
	private String scannedObjectListPath;
	private SchemaIndexPath predictedObjectListSIPath;
	private SchemaIndexPath scannedObjectListSIPath;
	private TupleHelper tupleHelper;

	private static final String EXP_ASSOCIATION_CONNECTION_VALUE = "ASSOCIATION_CON_VAL";

	public HypothesisExpressionGatingPO() {
		super();
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		return new HypothesisExpressionGatingPO<M>(this);
	}

	public HypothesisExpressionGatingPO(HypothesisExpressionGatingPO<M> clone) {
		super(clone);
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		this.expression = new PredictionExpression(this.expressionString);
		this.schemaHelper = new SchemaHelper(getOutputSchema());

		this.setScannedObjectListSIPath(this.schemaHelper.getSchemaIndexPath(this.scanObjListPath));
		this.setPredictedObjectListSIPath(this.schemaHelper.getSchemaIndexPath(this.predObjListPath));
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		TupleIndexPath scannedTupleIndexPath = this.getScannedObjectListSIPath().toTupleIndexPath(object);
		TupleIndexPath predictedTupleIndexPath = this.getPredictedObjectListSIPath().toTupleIndexPath(object);

		ConnectionList newObjConList = new ConnectionList();

		for (TupleInfo scannedTupleInfo : scannedTupleIndexPath) {

			for (TupleInfo predictedTupleInfo : predictedTupleIndexPath) {

				try {
					// getRatingForElementPair returns -1 if there is no connection for the given objects
					double currentRating = object.getMetadata().getConnectionList()
							.getRatingForElementPair(scannedTupleInfo.tupleIndexPath, predictedTupleInfo.tupleIndexPath);

					for (String attribute : expression.getAttributeNames(this.schemaHelper.getSchema())) {
						if (attribute.equals(EXP_ASSOCIATION_CONNECTION_VALUE)) {
							if (currentRating != -1) {
								expression.bindVariable(attribute, currentRating);
							} else {
								expression.bindVariable(attribute, 0);
							}
						} else {
							int[] attributePath = expression.getAttributePath(attribute);
							int[] objectPath = this.getVaryingIndex(scannedTupleInfo.tupleIndexPath.toArray(), attributePath);
							if (objectPath == null) {
								objectPath = this.getVaryingIndex(predictedTupleInfo.tupleIndexPath.toArray(), attributePath);
							}
							if (objectPath != null) {
								expression.bindVariable(attribute, this.tupleHelper.getObject(objectPath));
							}
						}
					}
					expression.evaluate();

					double newRating = expression.getTargetDoubleValue();

					if (currentRating != newRating) {
						newObjConList.add(new Connection(scannedTupleInfo.tupleIndexPath, predictedTupleInfo.tupleIndexPath, newRating));
					}
				} catch (Exception exception) {
					exception.printStackTrace();
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

	private int[] getVaryingIndex(int[] carTuplePath, int[] attributeTuplePath) {
		for (int i = 0; i < attributeTuplePath.length; i++) {
			if (attributeTuplePath[i] == -1) {
				attributeTuplePath[i] = carTuplePath[carTuplePath.length - 1];
				return attributeTuplePath;
			}
			if (carTuplePath[i] != attributeTuplePath[i]) {
				return null;
			}
		}
		return null;
	}

	/* SETTER AND GETTER */

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

	public TupleHelper getTupleHelper() {
		return tupleHelper;
	}

	public void setTupleHelper(TupleHelper tupleHelper) {
		this.tupleHelper = tupleHelper;
	}
}
