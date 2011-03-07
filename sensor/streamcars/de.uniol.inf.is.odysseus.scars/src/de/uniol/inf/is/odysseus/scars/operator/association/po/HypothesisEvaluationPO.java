/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.scars.operator.association.po;

import java.util.ArrayList;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IObjectTrackingLatency;
import de.uniol.inf.is.odysseus.scars.operator.association.algorithms.IAssociationAlgorithm;
import de.uniol.inf.is.odysseus.scars.operator.association.algorithms.MahalanobisDistanceAssociation;
import de.uniol.inf.is.odysseus.scars.operator.association.algorithms.MultiDistanceAssociation;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;
import de.uniol.inf.is.odysseus.scars.util.TupleIterator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * <p>
 * Physical operator for the rating of <strong>Connections</strong>
 * ({@link de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection})
 * within the association process.
 * </p>
 * 
 * <p>
 * For the evaluation an implementation of <strong>IAssociationAlgorithm</strong> is used.
 * ({@link IAssociationAlgorithm})
 * </p>
 * 
 * <p>
 * Based on which implementation is used, either each possible pair of predicted and scanned objects is used or
 * only existing connections will be considered.
 * </p>
 *
 * @author Volker Janz
 */
public class HypothesisEvaluationPO<M extends IProbability & IConnectionContainer & IObjectTrackingLatency >
		extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	// if debugMode is true the connections will be send to the console
	private boolean debugMode = false;

	private String oldObjListPath;
	private String newObjListPath;
	private HashMap<String, String> algorithmParameter;
	private SchemaHelper schemaHelper;
	private SchemaIndexPath predictedObjectListPath;
	private SchemaIndexPath scannedObjectListPath;

//	private TupleHelper tupleHelper;

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
		object.getMetadata().setObjectTrackingLatencyStart();
		if(this.getAssociationAlgorithm() instanceof MahalanobisDistanceAssociation) {
			object.getMetadata().setObjectTrackingLatencyStart("Association Evaluation - Mahalanobis");
		} else if(this.getAssociationAlgorithm() instanceof MultiDistanceAssociation) {
			object.getMetadata().setObjectTrackingLatencyStart("Association Evaluation - Multi Distance");
		}

		TupleIndexPath scannedTupleIndexPath = this.getScannedObjectListPath()
				.toTupleIndexPath(object);
		TupleIndexPath predictedTupleIndexPath = this
				.getPredictedObjectListPath().toTupleIndexPath(object);

		// If Gating is active and this is a MultiDistanceEvaluation it should only rate existing
		// connections instead of each combination
		if (gatingMode
				&& this.getAssociationAlgorithm() instanceof MultiDistanceAssociation) {

//			this.tupleHelper = new TupleHelper(object);

			for (IConnection con : object.getMetadata().getConnectionList()) {
				double currentRating = con.getRating();

				double value = this.associationAlgorithm.evaluate(
						((MVRelationalTuple<M>) con.getLeftPath().getTupleObject()).getMetadata().getCovariance(),
						this.getMeasurementValues((MVRelationalTuple<M>) con.getLeftPath().getTupleObject(), con.getLeftPath()),
						((MVRelationalTuple<M>) con.getRightPath().getTupleObject()).getMetadata().getCovariance(),
						this.getMeasurementValues((MVRelationalTuple<M>) con.getRightPath().getTupleObject(), con.getRightPath()),
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
									scannedTupleInfo.tupleIndexPath,
									predictedTupleInfo.tupleIndexPath);

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
								scannedTupleInfo.tupleIndexPath,
								predictedTupleInfo.tupleIndexPath,
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
			for (IConnection con : object.getMetadata().getConnectionList()) {
				System.out.println("CON: [" + con.getLeftPath() + "] <-> ["
						+ con.getRightPath() + "] RATING: [" + con.getRating()
						+ "]");
			}
		}

		if(this.getAssociationAlgorithm() instanceof MahalanobisDistanceAssociation) {
			object.getMetadata().setObjectTrackingLatencyEnd("Association Evaluation - Mahalanobis");
		} else if(this.getAssociationAlgorithm() instanceof MultiDistanceAssociation) {
			object.getMetadata().setObjectTrackingLatencyEnd("Association Evaluation - Multi Distance");
		}

		object.getMetadata().setObjectTrackingLatencyEnd();
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
