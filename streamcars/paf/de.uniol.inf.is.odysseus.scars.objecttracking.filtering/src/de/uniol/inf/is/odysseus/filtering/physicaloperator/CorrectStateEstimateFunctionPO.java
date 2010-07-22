package de.uniol.inf.is.odysseus.filtering.physicaloperator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.filtering.HashConstants;
import de.uniol.inf.is.odysseus.filtering.ICorrectStateEstimateFunction;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class CorrectStateEstimateFunctionPO <M extends IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private ICorrectStateEstimateFunction estimateFunction;
	private int[] oldObjListPath;
	private int[] newObjListPath;
	private SDFAttributeList schema;

	public CorrectStateEstimateFunctionPO() {

	}
	
	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		// list of new objects
		MVRelationalTuple<M>[] newList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>)OrAttributeResolver.resolveTuple(object, this.newObjListPath)).getAttributes();
		// list of old objects
		MVRelationalTuple<M>[] oldList = (MVRelationalTuple<M>[]) ((MVRelationalTuple<M>)OrAttributeResolver.resolveTuple(object, this.oldObjListPath)).getAttributes();
		// list of connections
		Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>[] objConList = (Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>[]) object.getMetadata().getConnectionList().toArray();



		// traverse connection list and filter
		for(Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double> connected : objConList ) {

			MVRelationalTuple<M> oldTuple = connected.getRight();
			MVRelationalTuple<M> newTuple = connected.getLeft();

			double[] measurementOld = newTuple.getMeasurementValues();
			double[] measurementNew = oldTuple.getMeasurementValues();

			double[] correctedMeasurement;

			double[][] gain = oldTuple.getMetadata().getGain();
			
			// update state estimate
			estimateFunction.addParameter(HashConstants.ESTIMATE_OLD_MEASUREMENT, measurementOld);
			estimateFunction.addParameter(HashConstants.ESTIMATE_NEW_MEASUREMENT, measurementNew);
			estimateFunction.addParameter(HashConstants.ESTIMATE_GAIN, gain);


			correctedMeasurement = estimateFunction.correctStateEstimate();
			
			//set corrected measurement
			// TODO Hier muss der korrigierte Wert als Metadatum gesetzt werden.
			// Dazu anschauen: objecttracking.metadata. Ein Interface
			// anlegen und dieses implementieren. Dann kann man auf 
			// object.getMetadata().get/setConnectionList() zugreifen.
		}

		// transfer to broker
		transfer(object);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub
		
	}

	public void setEstimateFunction(ICorrectStateEstimateFunction estimateFunction) {
		this.estimateFunction = estimateFunction;
	}

	public int[] getOldObjListPath() {
		return this.oldObjListPath;
	}
	
	public void setOldObjListPath(int[] oldObjListPath) {
		this.oldObjListPath = oldObjListPath;
	}

	public int[] getNewObjListPath() {
		return this.newObjListPath;
	}
	
	public void setNewObjListPath(int[] newObjListPath) {
		this.newObjListPath = newObjListPath;
	}

	public SDFAttributeList getSchema() {
		return schema;
	}

	public void setSchema(SDFAttributeList schema) {
		this.schema = schema;
	}
}
