/**
 * 
 */
package de.uniol.inf.is.odysseus.filtering.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.filtering.AbstractDataUpdateFunction;
import de.uniol.inf.is.odysseus.filtering.AbstractMetaDataCreationFunction;
import de.uniol.inf.is.odysseus.filtering.HashConstants;
import de.uniol.inf.is.odysseus.filtering.KalmanCorrectStateEstimateFunction;
import de.uniol.inf.is.odysseus.filtering.logicaloperator.FilterAO;
import de.uniol.inf.is.odysseus.metadata.base.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;

/**
 * @author dtwumasi
 *
 */
public class UpdateDataPO<M extends IProbability & IConnectionContainer> extends AbstractFilterPO<M> {
	
	private AbstractDataUpdateFunction dataUpdateFunction;
	
	public UpdateDataPO() {
	super();	
	}

	public UpdateDataPO(FilterAO<M> filterAO) {
	
		this.setNewObjListPath(filterAO.getNewObjListPathInt());
		this.setOldObjListPath(filterAO.getOldObjListPathInt());
		this.setParameters(filterAO.getParameters());
	}

	public UpdateDataPO(UpdateDataPO<M> copy) {
		super();
		HashMap<Integer,Object> parametercopy = new HashMap<Integer,Object>(copy.getDataUpdateFunction().getParameters());
		//this.(new KalmanCorrectStateEstimateFunction(parametercopy));
		
		
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
		int i=0;
		for(Connection connected : objConList ) {
			compute(connected, measurementValuePathsTupleNew, measurementValuePathsTupleOld,i);
			i++;
		}
		
		return object;
		
		}
	
	public void compute(Connection connected, ArrayList<int[]> measurementValuePathsTupleNew, ArrayList<int[]> measurementValuePathsTupleOld, int i  ) {
		this.dataUpdateFunction.compute(connected, measurementValuePathsTupleNew, measurementValuePathsTupleOld, i);
		
	}

	public void setDataUpdateFunction(AbstractDataUpdateFunction dataUpdateFunction) {
		this.dataUpdateFunction = dataUpdateFunction;
	}

	public AbstractDataUpdateFunction getDataUpdateFunction() {
		return dataUpdateFunction;
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
	return new UpdateDataPO<M>(this);	
	}





}
