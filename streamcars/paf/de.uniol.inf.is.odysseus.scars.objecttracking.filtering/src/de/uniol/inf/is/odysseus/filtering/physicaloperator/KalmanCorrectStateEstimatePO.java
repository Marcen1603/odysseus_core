/**
 * 
 */
package de.uniol.inf.is.odysseus.filtering.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.filtering.HashConstants;
import de.uniol.inf.is.odysseus.filtering.KalmanCorrectStateEstimateFunction;
import de.uniol.inf.is.odysseus.filtering.logicaloperator.FilterAO;
import de.uniol.inf.is.odysseus.metadata.base.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;

/**
 * @author dtwumasi
 *
 */
public class KalmanCorrectStateEstimatePO<M extends IProbability & IConnectionContainer> extends AbstractFilterPO<M> {
	
	public KalmanCorrectStateEstimatePO() {
	super();	
	}

	public KalmanCorrectStateEstimatePO(FilterAO<M> filterAO) {
		this.setFilterFunction(filterAO.getFilterFunction());
		this.setNewObjListPath(filterAO.getNewObjListPathInt());
		this.setOldObjListPath(filterAO.getOldObjListPathInt());
	}

	public KalmanCorrectStateEstimatePO(KalmanCorrectStateEstimatePO<M> copy) {
		if (copy.getFilterFunction().getFunctionID() == 1) {
			this.setFilterFunction(new KalmanCorrectStateEstimateFunction(copy.getFilterFunction().getParameters()));
		}
		
		this.setNewObjListPath(copy.getNewObjListPath().clone());
		this.setOldObjListPath(copy.getOldObjListPath().clone());
	
	}
	
	public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object) {
		
		// 1 - Get the needed data out of the MVRelationalTuple object
		// 1.1 - Get the list of new objects as an array of MVRelationalTuple
	//	MVRelationalTuple<M>[] newList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>)OrAttributeResolver.resolveTuple(object, this.getNewObjListPath())).getAttributes();
		// 1.2 - Get the list of old objects (which are predicted to the timestamp of the new objects) as an array of MVRelationalTuple
	//	MVRelationalTuple<M>[] oldList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>)OrAttributeResolver.resolveTuple(object, this.getOldObjListPath())).getAttributes();
		
		// --- Relative Pfade von einem "Auto" aus zu den Messwerten finden ---
		int[] pathToFirstCarInNewList = new int[this.getNewObjListPath().length+1];
		for(int i = 0; i < pathToFirstCarInNewList.length-1; i++) {
			pathToFirstCarInNewList[i] = this.getNewObjListPath()[i];
		}
		pathToFirstCarInNewList[this.getNewObjListPath().length-1] = 0;

		int[] pathToFirstCarInOldList = new int[this.getOldObjListPath().length+1];
		for(int i = 0; i < pathToFirstCarInOldList.length-1; i++) {
			pathToFirstCarInOldList[i] = this.getOldObjListPath()[i];
		}
		pathToFirstCarInOldList[this.getOldObjListPath().length-1] = 0;

		ArrayList<int[]> measurementValuePathsTupleNew = OrAttributeResolver.getPathsOfMeasurements(OrAttributeResolver.getSubSchema(this.getSchema(), pathToFirstCarInNewList));
		ArrayList<int[]> measurementValuePathsTupleOld = OrAttributeResolver.getPathsOfMeasurements(OrAttributeResolver.getSubSchema(this.getSchema(), pathToFirstCarInOldList));
		
		// list of connections
		Connection[] objConList = new Connection[object.getMetadata().getConnectionList().toArray().length];
		ArrayList<Connection> tmpConList = object.getMetadata().getConnectionList();

		for(int i = 0; i < objConList.length; i++) {
			objConList[i] = tmpConList.get(i);
		}
		
		// traverse connection list and filter
		for(Connection connected : objConList ) {
			compute(connected, measurementValuePathsTupleNew, measurementValuePathsTupleOld);
		}
		
		return object;
		
		}
	
	public void compute(Connection connected, ArrayList<int[]> measurementValuePathsTupleNew, ArrayList<int[]> measurementValuePathsTupleOld  ) {
		
		MVRelationalTuple<StreamCarsMetaData> oldTuple = (MVRelationalTuple<StreamCarsMetaData>) connected.getRight();
		MVRelationalTuple<StreamCarsMetaData> newTuple = (MVRelationalTuple<StreamCarsMetaData>) connected.getLeft();
	
		double[] measurementOld = OrAttributeResolver.getMeasurementValues(measurementValuePathsTupleOld, oldTuple);
		
		double[] measurementNew = OrAttributeResolver.getMeasurementValues(measurementValuePathsTupleNew, newTuple);
		
	
		double[][] gain = oldTuple.getMetadata().getGain();
		
		getFilterFunction().addParameter(HashConstants.NEW_MEASUREMENT, measurementNew);
		getFilterFunction().addParameter(HashConstants.OLD_MEASUREMENT, measurementOld);
		getFilterFunction().addParameter(HashConstants.GAIN, gain);
	
		double[] result = (double[]) getFilterFunction().compute();
		
		// TODO richtig machen
		for (int i=0; i<= result.length-1; i++) {
			newTuple.setAttribute(measurementValuePathsTupleNew.get(i)[0], result[i]);
		}

	}





}
