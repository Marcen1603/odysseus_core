package de.uniol.inf.is.odysseus.filtering.physicaloperator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.filtering.IGainFunction;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;

public class GainFunctionPO <M extends IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private IGainFunction gainFunction;
	
	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		// list of connections
		Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>[] objConList = (Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>[]) object.getMetadata().getConnectionList().toArray();
		
		double[][] gain = null;
		
		// traverse connection list and filter
		for(Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double> connected : objConList ) {
			
			MVRelationalTuple<M> oldTuple = connected.getRight();
			MVRelationalTuple<M> newTuple = connected.getLeft();
			
			double[][] covarianceNew = newTuple.getMetadata().getCovariance();
			double[][] covarianceOld = oldTuple.getMetadata().getCovariance();
			
			// compute gain
			gainFunction.addParameter("oldCovariance", covarianceOld);
			gainFunction.addParameter("newCovariance", covarianceNew);
			
			gain = gainFunction.computeGain();
			
			//set gain
			// TODO Hier muss der Gain als Metadatum gesetzt werden.
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
	
	public void setGainFunction(IGainFunction gainFunction) {
		this.gainFunction = gainFunction;
	}

}
