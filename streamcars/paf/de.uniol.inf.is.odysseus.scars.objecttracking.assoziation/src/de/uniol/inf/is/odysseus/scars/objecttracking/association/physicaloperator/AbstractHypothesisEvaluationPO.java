package de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.association.CorrelationMatrixUtils;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Physical Operator for the rating of connections within the association process.
 *
 * new = left; old = right
 *
 * @author Volker Janz
 *
 * @param <M>
 */
public abstract class AbstractHypothesisEvaluationPO<M extends IProbability & IConnectionContainer> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private int[] oldObjListPath;
	private int[] newObjListPath;
	private HashMap<String, String> algorithmParameter;

	//private SDFAttributeList leftSchema;
	//private SDFAttributeList rightSchema;

	private SDFAttributeList schema;

	public AbstractHypothesisEvaluationPO() {

	}

	public AbstractHypothesisEvaluationPO(AbstractHypothesisEvaluationPO<M> clone) {
		super(clone);

		this.oldObjListPath = clone.getOldObjListPath();
		this.newObjListPath = clone.getNewObjListPath();
		this.algorithmParameter = clone.getAlgorithmParameter();
		this.schema = clone.getSchema();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		// 1 - Get the needed data out of the MVRelationalTuple object
		// 1.1 - Get the list of new objects as an array of MVRelationalTuple
		MVRelationalTuple<M>[] newList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>)OrAttributeResolver.resolveTuple(object, this.newObjListPath)).getAttributes();
		// 1.2 - Get the list of old objects (which are predicted to the timestamp of the new objects) as an array of MVRelationalTuple
		MVRelationalTuple<M>[] oldList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>)OrAttributeResolver.resolveTuple(object, this.oldObjListPath)).getAttributes();
		// 1.3 - Get the list of connections between old and new objects as an array of Connection
		Connection[] objConList = new Connection[object.getMetadata().getConnectionList().toArray().length];
		ArrayList<Connection> tmpConList = object.getMetadata().getConnectionList();

		for(int i = 0; i < objConList.length; i++) {
			objConList[i] = tmpConList.get(i);
		}

		// 2 - Convert the connection list to an matrix of ratings so that even connections which are NOT in the connections list (so they have a rating of 0) can be evaluated
		CorrelationMatrixUtils<M> corUtils = new CorrelationMatrixUtils<M>();
		double[][] corMatrix = corUtils.encodeMatrix(newList, oldList, objConList);

		// 3 - Evaluate each connection in the matrix
		corMatrix = this.evaluateAll(corMatrix, newList, oldList);

		// 4 - Generate a new connection list out of the matrix. only connections with rating > 0 will be stored so that the connection list is as small as possible
		ConnectionList newObjConList = corUtils.decodeMatrix(newList, oldList, corMatrix);

		// 5 - Replace the old connection list in the metadata with the new connection list
		object.getMetadata().setConnectionList(newObjConList);

		// 6 - ready -> transfer to next operator
		transfer(object);
	}

	public abstract double evaluate(MVRelationalTuple<M> tupleNew, MVRelationalTuple<M> tupleOld, ArrayList<int[]> mesurementValuePathsNew, ArrayList<int[]> mesurementValuePathsOld);

	public double[][] evaluateAll(double[][] matrix, MVRelationalTuple<M>[] tupleNew, MVRelationalTuple<M>[] tupleOld) {
		if(matrix == null || tupleNew == null || tupleOld == null) {
			throw new NullPointerException("");
		}

		// --- Relative Pfade von einem "Auto" aus zu den Messwerten finden ---
		int[] pathToFirstCarInNewList = new int[this.getNewObjListPath().length+1];
		for(int i = 0; i < pathToFirstCarInNewList.length; i++) {
			pathToFirstCarInNewList[i] = this.getNewObjListPath()[i];
		}
		pathToFirstCarInNewList[this.getNewObjListPath().length-1] = 0;

		int[] pathToFirstCarInOldList = new int[this.getOldObjListPath().length];
		for(int i = 0; i < pathToFirstCarInOldList.length; i++) {
			pathToFirstCarInOldList[i] = this.getOldObjListPath()[i];
		}
		pathToFirstCarInOldList[this.getOldObjListPath().length-1] = 0;

		ArrayList<int[]> mesurementValuePathsTupleNew = OrAttributeResolver.getPathsOfMeasurements(OrAttributeResolver.getSubSchema(this.schema, pathToFirstCarInNewList));
		ArrayList<int[]> mesurementValuePathsTupleOld = OrAttributeResolver.getPathsOfMeasurements(OrAttributeResolver.getSubSchema(this.schema, pathToFirstCarInOldList));
		// --------------------------------------------------------------------

		for(int i=0; i<matrix[0].length; i++) {
			for(int j=0; i<matrix.length; j++) {
				// --- Auskommentiert da relative Pfade benötigt werden, keine absoluten ---
				//ArrayList<int[]> mesurementValuePathsTupleNew = OrAttributeResolver.getPathsOfMeasurements(this.leftSchema);
				//ArrayList<int[]> mesurementValuePathsTupleOld = OrAttributeResolver.getPathsOfMeasurements(this.rightSchema);
				matrix[i][j] = evaluate(tupleNew[i], tupleOld[j], mesurementValuePathsTupleNew, mesurementValuePathsTupleOld);
			}
		}
		return matrix;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public abstract AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone();

	/**
	 * Inits the algorithm specific parameter. The parameter are stored in the HashMap algorithmParameter.
	 */
	public abstract void initAlgorithmParameter();

	// ----- SETTER AND GETTER -----

	public void setOldObjListPath(int[] oldObjListPath) {
		this.oldObjListPath = oldObjListPath;
	}

	public void setNewObjListPath(int[] newObjListPath) {
		this.newObjListPath = newObjListPath;
	}

	/*
	public void setLeftSchema(SDFAttributeList leftSchema) {
		this.leftSchema = leftSchema;
	}

	public void setRightSchema(SDFAttributeList rightSchema) {
		this.rightSchema = rightSchema;
	}
	*/

	public int[] getOldObjListPath() {
		return this.oldObjListPath;
	}

	public int[] getNewObjListPath() {
		return this.newObjListPath;
	}

	/*
	public SDFAttributeList getLeftSchema() {
		return this.leftSchema;
	}

	public SDFAttributeList getRightSchema() {
		return this.rightSchema;
	}
	*/

	public void setAlgorithmParameter(HashMap<String, String> newAlgoParameter) {
		this.algorithmParameter = newAlgoParameter;
		this.initAlgorithmParameter();
	}

	public HashMap<String, String> getAlgorithmParameter() {
		return this.algorithmParameter;
	}

	public SDFAttributeList getSchema() {
		return schema;
	}

	public void setSchema(SDFAttributeList schema) {
		this.schema = schema;
	}

}
