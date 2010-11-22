package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IObjectTrackingLatency;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.PredictionExpression;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleHelper;

/**
 * @author Volker Janz
 *
 * @param <M>
 */

public class HypothesisExpressionEvaluationPO<M extends IProbability & IConnectionContainer & IObjectTrackingLatency> extends
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

	public HypothesisExpressionEvaluationPO() {
		super();
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		return new HypothesisExpressionEvaluationPO<M>(this);
	}

	public HypothesisExpressionEvaluationPO(HypothesisExpressionEvaluationPO<M> clone) {
		super(clone);
		this.scanObjListPath = clone.scanObjListPath;
		this.predObjListPath = clone.predObjListPath;
		this.expression = clone.expression;
		this.expressionString = clone.expressionString;
		this.schemaHelper = clone.schemaHelper;
		this.predictedObjectListPath = clone.predictedObjectListPath;
		this.scannedObjectListPath = clone.scannedObjectListPath;
		this.predictedObjectListSIPath = clone.predictedObjectListSIPath;
		this.scannedObjectListSIPath = clone.scannedObjectListSIPath;
		this.tupleHelper = clone.tupleHelper;
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
		this.tupleHelper = new TupleHelper(object);

		ConnectionList newObjConList = new ConnectionList();

		for (IConnection con : object.getMetadata().getConnectionList()) {
			// double currentRating = con.getRating();
			try {
				for (String attribute : expression.getAttributeNames(this.schemaHelper.getSchema())) {
					if (attribute.equals(EXP_ASSOCIATION_CONNECTION_VALUE)) {
						expression.bindVariable(attribute, con.getRating());
					} else {
						int[] attributePath = expression.getAttributePath(attribute);
						int[] objectPath = this.getVaryingIndex(con.getLeftPath().toArray(), attributePath);
						if (objectPath == null) {
							objectPath = this.getVaryingIndex(con.getRightPath().toArray(), attributePath);
						}
						if (objectPath != null) {
							expression.bindVariable(attribute, this.tupleHelper.getObject(objectPath));
						}
					}
				}
				expression.evaluate();
				con.setRating(expression.getTargetDoubleValue());
			} catch (Exception exception) {
				exception.printStackTrace();
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
