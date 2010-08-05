/**
 * 
 */
package de.uniol.inf.is.odysseus.filtering.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.filtering.HashConstants;
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
		
	}

	public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object) {
		
		// 1 - Get the needed data out of the MVRelationalTuple object
		// 1.1 - Get the list of new objects as an array of MVRelationalTuple
	//	MVRelationalTuple<M>[] newList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>)OrAttributeResolver.resolveTuple(object, this.getNewObjListPath())).getAttributes();
		// 1.2 - Get the list of old objects (which are predicted to the timestamp of the new objects) as an array of MVRelationalTuple
	//	MVRelationalTuple<M>[] oldList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>)OrAttributeResolver.resolveTuple(object, this.getOldObjListPath())).getAttributes();
		
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

		ArrayList<int[]> measurementValuePathsTupleNew = OrAttributeResolver.getPathsOfMeasurements(OrAttributeResolver.getSubSchema(this.getSchema(), pathToFirstCarInNewList));
		ArrayList<int[]> measurementValuePathsTupleOld = OrAttributeResolver.getPathsOfMeasurements(OrAttributeResolver.getSubSchema(this.getSchema(), pathToFirstCarInOldList));
		// list of connections
		Connection[] objConList = (Connection[]) object.getMetadata().getConnectionList().toArray();
		
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
	
		double[][] result = (double[][]) getFilterFunction().compute();
	
		//set gain
		((MetaAttributeContainer<StreamCarsMetaData>) connected.getRight()).getMetadata().setGain(result);

	
}
}
