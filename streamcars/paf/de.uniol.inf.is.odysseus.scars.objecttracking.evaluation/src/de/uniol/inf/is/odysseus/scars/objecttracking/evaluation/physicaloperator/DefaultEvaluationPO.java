package de.uniol.inf.is.odysseus.scars.objecttracking.evaluation.physicaloperator;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IRating;

public class DefaultEvaluationPO<M extends IRating & IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>> extends AbstractEvaluationPO<M>{

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void evaluate(IRating rating, int port) {
		/*
		 * Port 0: Objekte aus der Assoziation 
		 * Port 1: Objekte aus der Filterung
		 * Port 2: Objekte aus dem temporaeren Broker
		 */
		switch (port) {
		case 0:
			rating.decRating();
			break;
		case 1:
			rating.incRating();
			break;
		case 2:
			rating.setRating(5);
			break;

		default:
			break;
		}
	}

}
