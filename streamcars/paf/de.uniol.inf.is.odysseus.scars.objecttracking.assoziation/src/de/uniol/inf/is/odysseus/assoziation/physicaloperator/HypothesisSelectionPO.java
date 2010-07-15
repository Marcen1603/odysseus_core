package de.uniol.inf.is.odysseus.assoziation.physicaloperator;

import de.uniol.inf.is.odysseus.assoziation.CorrelationMatrixUtils;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class HypothesisSelectionPO<M extends IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {
	
	private int[] oldObjListPath;
	private int[] newObjListPath;
	
	private SDFAttributeList leftSchema;
	private SDFAttributeList rightSchema;
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		
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
		Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>[] objConList = (Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>[]) object.getMetadata().getConnectionList().toArray();
		
		// 2 - Convert the connection list to an matrix of ratings so that even connections which are NOT in the connections list (so they have a rating of 0) can be evaluated
		CorrelationMatrixUtils<M> corUtils = new CorrelationMatrixUtils<M>();
		double[][] corMatrix = corUtils.encodeMatrix(newList, oldList, objConList);
		
		// 3 - Evaluate each connection in the matrix
		corMatrix = this.singleMatchingEvaluation(corMatrix);
		
		// 4 - Generate a new connection list out of the matrix. only connections with rating > 0 will be stored so that the connection list is as small as possible
		ConnectionList<MVRelationalTuple<M>, MVRelationalTuple<M>, Double> newObjConList = corUtils.decodeMatrix(newList, oldList, corMatrix);
		
		// 5 - Replace the old connection list in the metadata with the new connection list
		object.getMetadata().setConnectionList(newObjConList);
		
		// 6 - ready -> transfer to next operator
		transfer(object);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Diese Funktion sorgt daf�r, dass nur noch eindeutige Zuordnungen vorhanden sind.
	 */
	public double[][] singleMatchingEvaluation(double[][] matchingMatrix) {
		double[][] singleMatchingMatrix = null;
		int index = -1;
		if(matchingMatrix.length > 0) {
			singleMatchingMatrix = new double[matchingMatrix.length][matchingMatrix[0].length];
		}
		
		for (int i = 0; i < matchingMatrix.length; i++) {
			index = getMaxRowIndex(matchingMatrix[i]);
			if(index != -1) {
				singleMatchingMatrix[i][index] = matchingMatrix[i][index];
			}
			index = -1;
		}
		
		double maxValue = -1;
		int maxValueIndex = -1;
		for (int i = 0; i < singleMatchingMatrix[0].length; i++) {
			for (int j = 0; j < singleMatchingMatrix.length; j++) {
				if(singleMatchingMatrix[j][i] != 0.0d && maxValue < singleMatchingMatrix[j][i]) {
					//Wenn der Wert gr��er als der vorher errechnete Wert ist wird dieser als neuer Wert �bernommen
					maxValue = matchingMatrix[j][i];
					maxValueIndex = j;
					singleMatchingMatrix[j][i] = 0;
				} if(maxValue >= singleMatchingMatrix[j][i]) {
					// Falls der Wert kleiner oder gleich dem vorher errechneten Wert ist wird dieser ignoriert 
					singleMatchingMatrix[j][i] = 0;
				}
			}
			if(maxValue != -1) {
				singleMatchingMatrix[maxValueIndex][i] = maxValue;
			}
			maxValue = -1;
			maxValueIndex = -1;
		}
		
		return singleMatchingMatrix;
	}
	
	/**
	 * 
	 * @param row aktuelle Zeiler der Matrix
	 * @return Index des maximalen Wertes der Zeile
	 */
	private int getMaxRowIndex(double[] row) {
		int index = -1;
		double maxValue = 0.0d;
		for (int i = 0; i < row.length; i++) {
			if(maxValue < row[i]) {
				maxValue = row[i];
				index = i;
			}
		}
		return index;
	}
	
	// ----- SETTER AND GETTER -----

	public void setOldObjListPath(int[] oldObjListPath) {
		this.oldObjListPath = oldObjListPath;
	}

	public void setNewObjListPath(int[] newObjListPath) {
		this.newObjListPath = newObjListPath;
	}

	public void setLeftSchema(SDFAttributeList leftSchema) {
		this.leftSchema = leftSchema;
	}

	public void setRightSchema(SDFAttributeList rightSchema) {
		this.rightSchema = rightSchema;
	}
	
	public int[] getOldObjListPath() {
		return this.oldObjListPath;
	}

	public int[] getNewObjListPath() {
		return this.newObjListPath;
	}

	public SDFAttributeList getLeftSchema() {
		return this.leftSchema;
	}

	public SDFAttributeList getRightSchema() {
		return this.rightSchema;
	}

}
