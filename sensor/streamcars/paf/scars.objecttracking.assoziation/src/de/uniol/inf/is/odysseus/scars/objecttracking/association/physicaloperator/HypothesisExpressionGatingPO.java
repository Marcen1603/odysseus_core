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

public class HypothesisExpressionGatingPO<M extends IProbability & IConnectionContainer & IObjectTrackingLatency>
		extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

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

		this.setScannedObjectListSIPath(this.schemaHelper
				.getSchemaIndexPath(this.scanObjListPath));
		this.setPredictedObjectListSIPath(this.schemaHelper
				.getSchemaIndexPath(this.predObjListPath));
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		TupleIndexPath scannedTupleIndexPath = this.getScannedObjectListSIPath()
				.toTupleIndexPath(object);
		TupleIndexPath predictedTupleIndexPath = this
				.getPredictedObjectListSIPath().toTupleIndexPath(object);

		ConnectionList newObjConList = new ConnectionList();

		for (TupleInfo scannedTupleInfo : scannedTupleIndexPath) {
			MVRelationalTuple<M> scannedObject = (MVRelationalTuple<M>) scannedTupleInfo.tupleObject;

			for (TupleInfo predictedTupleInfo : predictedTupleIndexPath) {
				MVRelationalTuple<M> predictedObject = (MVRelationalTuple<M>) predictedTupleInfo.tupleObject;

				double currentRating = object
						.getMetadata()
						.getConnectionList()
						.getRatingForElementPair(
								scannedTupleInfo.tupleIndexPath,
								predictedTupleInfo.tupleIndexPath);

				double[][] scannedCov = scannedObject.getMetadata()
						.getCovariance();
				double[][] predictedCov = predictedObject.getMetadata()
						.getCovariance();

				double value = 0;

				if (currentRating != value) {
					newObjConList.add(new Connection(
							scannedTupleInfo.tupleIndexPath,
							predictedTupleInfo.tupleIndexPath, value));
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

	public void setPredictedObjectListSIPath(
			SchemaIndexPath predictedObjectListSIPath) {
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
