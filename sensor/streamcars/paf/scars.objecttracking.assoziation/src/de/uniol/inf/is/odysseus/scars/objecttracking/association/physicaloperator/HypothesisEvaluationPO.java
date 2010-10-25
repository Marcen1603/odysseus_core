package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.scars.objecttracking.association.algorithms.IAssociationAlgorithm;
import de.uniol.inf.is.odysseus.scars.objecttracking.association.algorithms.MultiDistanceAssociation;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleHelper;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;
import de.uniol.inf.is.odysseus.scars.util.TupleIterator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * Physical Operator for the rating of connections within the association
 * process.
 *
 * new = left; old = right
 *
 * @author Volker Janz
 *
 * @param <M>
 */
public class HypothesisEvaluationPO<M extends IProbability & IConnectionContainer>
		extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	// if debugMode is true the connections will be send to the console
	private boolean debugMode = false;

	private String oldObjListPath;
	private String newObjListPath;
	private HashMap<String, String> algorithmParameter;
	private SchemaHelper schemaHelper;
	private SchemaIndexPath predictedObjectListPath;
	private SchemaIndexPath scannedObjectListPath;

	private TupleHelper tupleHelper;

	private IAssociationAlgorithm associationAlgorithm;

	private boolean gatingMode;

	private static final String GATING_ID = "gatingMode";
	private static final String GATING_ON = "true";
	private static final String GATING_OFF = "false";

	public HypothesisEvaluationPO() {
		super();
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		return new HypothesisEvaluationPO<M>(this);
	}

	@SuppressWarnings("unchecked")
	public HypothesisEvaluationPO(HypothesisEvaluationPO<M> clone) {
		super(clone);

		this.oldObjListPath = clone.getOldObjListPath();
		this.newObjListPath = clone.getNewObjListPath();
		this.algorithmParameter = (HashMap<String, String>) clone
				.getAlgorithmParameter().clone();
		this.schemaHelper = clone.schemaHelper;
		this.associationAlgorithm = clone.associationAlgorithm;
		this.gatingMode = clone.gatingMode;
	}

	protected double[] getMeasurementValues(MVRelationalTuple<M> tuple,
			TupleIndexPath tupleIndexPath) {
		ArrayList<Double> values = new ArrayList<Double>();

		for (TupleInfo info : new TupleIterator(tuple, tupleIndexPath)) {
			if (SDFDatatypes.isMeasurementValue(info.attribute.getDatatype())) {
				values.add(new Double(info.tupleObject.toString()));
			}
		}

		double[] result = new double[values.size()];
		for (int i = 0; i < values.size(); i++) {
			result[i] = values.get(i);
		}

		return result;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		this.schemaHelper = new SchemaHelper(getOutputSchema());

		this.setScannedObjectListPath(this.schemaHelper
				.getSchemaIndexPath(this.newObjListPath));
		this.setPredictedObjectListPath(this.schemaHelper
				.getSchemaIndexPath(this.oldObjListPath));

		if (this.getAlgorithmParameter().containsKey(GATING_ID)) {
			if (this.getAlgorithmParameter().get(GATING_ID).equals(GATING_ON)) {
				this.gatingMode = true;
			} else if (this.getAlgorithmParameter().get(GATING_ID)
					.equals(GATING_OFF)) {
				this.gatingMode = false;
			} else {
				this.gatingMode = false;
			}
		} else {
			this.gatingMode = false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		TupleIndexPath scannedTupleIndexPath = this.getScannedObjectListPath()
				.toTupleIndexPath(object);
		TupleIndexPath predictedTupleIndexPath = this
				.getPredictedObjectListPath().toTupleIndexPath(object);

		// If Gating is active and this is a MultiDistanceEvaluation it should only rate existing
		// connections instead of each combination
		if (gatingMode
				&& this.getAssociationAlgorithm() instanceof MultiDistanceAssociation) {

			this.tupleHelper = new TupleHelper(object);

			for (Connection con : object.getMetadata().getConnectionList()) {
				double currentRating = con.getRating();

				double value = this.associationAlgorithm.evaluate(
						((MVRelationalTuple<M>) tupleHelper.getObject(con.getLeftPath())).getMetadata().getCovariance(),
						this.getMeasurementValues((MVRelationalTuple<M>) tupleHelper.getObject(con.getLeftPath()), TupleIndexPath.fromIntArray(con.getLeftPath(), object, this.scannedObjectListPath)),
						((MVRelationalTuple<M>) tupleHelper.getObject(con.getRightPath())).getMetadata().getCovariance(),
						this.getMeasurementValues((MVRelationalTuple<M>) tupleHelper.getObject(con.getRightPath()), TupleIndexPath.fromIntArray(con.getRightPath(), object, this.predictedObjectListPath)),
						currentRating);

				con.setRating(value);
			}
		}

		// If Gating is inactive check every possible connection
		else {

			ConnectionList newObjConList = new ConnectionList();

			for (TupleInfo scannedTupleInfo : scannedTupleIndexPath) {
				MVRelationalTuple<M> scannedObject = (MVRelationalTuple<M>) scannedTupleInfo.tupleObject;

				for (TupleInfo predictedTupleInfo : predictedTupleIndexPath) {
					MVRelationalTuple<M> predictedObject = (MVRelationalTuple<M>) predictedTupleInfo.tupleObject;

					double currentRating = object
							.getMetadata()
							.getConnectionList()
							.getRatingForElementPair(
									scannedTupleInfo.tupleIndexPath.toArray(false),
									predictedTupleInfo.tupleIndexPath.toArray(false));

					double value = this.associationAlgorithm.evaluate(
							scannedObject.getMetadata().getCovariance(),
							getMeasurementValues(object,
									scannedTupleInfo.tupleIndexPath),
							predictedObject.getMetadata().getCovariance(),
							getMeasurementValues(object,
									predictedTupleInfo.tupleIndexPath),
							currentRating);

					if (currentRating != value) {
						newObjConList.add(new Connection(
								scannedTupleInfo.tupleIndexPath.toArray(true),
								predictedTupleInfo.tupleIndexPath.toArray(true),
								value));
					}
				}
			}

			object.getMetadata().setConnectionList(newObjConList);

		}

		// If debugMode == true print all connections to console
		if (debugMode) {
			System.out
					.println("######################### CONNECTIONS: #########################");
			for (Connection con : object.getMetadata().getConnectionList()) {
				System.out.println("CON: [" + con.getLeftPath() + "] <-> ["
						+ con.getRightPath() + "] RATING: [" + con.getRating()
						+ "]");
			}
		}

		transfer(object);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		this.sendPunctuation(timestamp);
	}

	/* SETTER AND GETTER */

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	public void setScannedObjectListPath(SchemaIndexPath scannedObjectListPath) {
		this.scannedObjectListPath = scannedObjectListPath;
	}

	public SchemaIndexPath getScannedObjectListPath() {
		return scannedObjectListPath;
	}

	public void setPredictedObjectListPath(
			SchemaIndexPath predictedObjectListPath) {
		this.predictedObjectListPath = predictedObjectListPath;
	}

	public SchemaIndexPath getPredictedObjectListPath() {
		return predictedObjectListPath;
	}

	public IAssociationAlgorithm getAssociationAlgorithm() {
		return associationAlgorithm;
	}

	public void setAssociationAlgorithm(
			IAssociationAlgorithm associationAlgorithm) {
		this.associationAlgorithm = associationAlgorithm;
	}

	public boolean isGatingMode() {
		return gatingMode;
	}

	public void setGatingMode(boolean gatingMode) {
		this.gatingMode = gatingMode;
	}

	public void setOldObjListPath(String oldObjListPath) {
		this.oldObjListPath = oldObjListPath;
	}

	public void setNewObjListPath(String newObjListPath) {
		this.newObjListPath = newObjListPath;
	}

	public String getOldObjListPath() {
		return this.oldObjListPath;
	}

	public String getNewObjListPath() {
		return this.newObjListPath;
	}

	public void setAlgorithmParameter(HashMap<String, String> newAlgoParameter) {
		this.algorithmParameter = newAlgoParameter;
		this.associationAlgorithm
				.initAlgorithmParameter(this.algorithmParameter);
	}

	public HashMap<String, String> getAlgorithmParameter() {
		return this.algorithmParameter;
	}
}
