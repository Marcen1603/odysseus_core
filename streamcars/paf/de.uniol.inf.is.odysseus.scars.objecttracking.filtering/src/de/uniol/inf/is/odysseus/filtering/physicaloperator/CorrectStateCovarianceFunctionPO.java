package de.uniol.inf.is.odysseus.filtering.physicaloperator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.filtering.HashConstants;
import de.uniol.inf.is.odysseus.filtering.ICorrectStateCovarianceFunction;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class CorrectStateCovarianceFunctionPO <M extends IGain & IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private ICorrectStateCovarianceFunction covarianceFunction;
	private SDFAttributeList schema;
	
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


		// traverse connection list and filter
		for(Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double> connected : objConList ) {

			MVRelationalTuple<M> oldTuple = connected.getRight();

			double[][] covarianceOld = oldTuple.getMetadata().getCovariance();

			double[][] correctedCovariance;

			double[][] gain = oldTuple.getMetadata().getGain();
			
			// update state covariance
			covarianceFunction.addParameter(HashConstants.COVARIANCE_GAIN, gain);
			covarianceFunction.addParameter(HashConstants.COVARIANCE_OLD_COVARIANCE, covarianceOld);

			correctedCovariance = covarianceFunction.correctStateCovariance();
			
			oldTuple.getMetadata().setCovariance(correctedCovariance);
		}

		// transfer to broker
		transfer(object);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub
		
	}
	
	public void setCovarianceFunction(ICorrectStateCovarianceFunction covarianceFunction) {
		this.covarianceFunction = covarianceFunction;
	}

	public void setSchema(SDFAttributeList schema) {
		this.schema = schema;
	}
}
