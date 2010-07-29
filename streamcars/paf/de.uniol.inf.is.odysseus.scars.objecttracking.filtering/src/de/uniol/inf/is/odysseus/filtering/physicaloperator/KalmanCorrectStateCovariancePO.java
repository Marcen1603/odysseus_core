/**
 * 
 */
package de.uniol.inf.is.odysseus.filtering.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.filtering.HashConstants;
import de.uniol.inf.is.odysseus.metadata.base.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;
/**
 * @author dtwumasi
 *
 */
public class KalmanCorrectStateCovariancePO  extends AbstractFilterPO {
	
	@Override
	public MVRelationalTuple<StreamCarsMetaData> computeAll(MVRelationalTuple<StreamCarsMetaData> object) {
		
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
	
	@Override
	public void compute(Connection connected, ArrayList<int[]> measurementValuePathsTupleNew,
			ArrayList<int[]> measurementValuePathsTupleOld) {
		
		
		
		MVRelationalTuple<StreamCarsMetaData> oldTuple = (MVRelationalTuple<StreamCarsMetaData>) connected.getRight();
		MVRelationalTuple<StreamCarsMetaData> newTuple = (MVRelationalTuple<StreamCarsMetaData>) connected.getLeft();
		
		double[][] covarianceOld = oldTuple.getMetadata().getCovariance();
		
		double[][] covarianceNew = newTuple.getMetadata().getCovariance();
		
		double[][] gain = oldTuple.getMetadata().getGain();
	
		getFilterFunction().addParameter(HashConstants.GAIN, gain);
		getFilterFunction().addParameter(HashConstants.OLD_COVARIANCE, covarianceOld);
		getFilterFunction().addParameter(HashConstants.NEW_COVARIANCE, covarianceNew);
		
		double[][] resultCovariance = (double[][]) getFilterFunction().compute();
	
		//set new state covariance
		((MetaAttributeContainer<StreamCarsMetaData>) connected.getRight()).getMetadata().setCovariance(resultCovariance);
	
	 }

}