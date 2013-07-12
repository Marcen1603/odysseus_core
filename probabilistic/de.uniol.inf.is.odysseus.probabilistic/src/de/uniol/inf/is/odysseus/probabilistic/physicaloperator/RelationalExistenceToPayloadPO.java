package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class RelationalExistenceToPayloadPO extends ExistenceToPayloadPO<IProbabilistic, Tuple<IProbabilistic>> {

	public RelationalExistenceToPayloadPO(RelationalExistenceToPayloadPO relationalExistenceToPayloadPO) {
		super(relationalExistenceToPayloadPO);
	}

	public RelationalExistenceToPayloadPO() {
	}

	@Override
	protected void process_next(Tuple<IProbabilistic> object, int port) {
		int inputSize = object.size();
		ProbabilisticTuple<IProbabilistic> out = new ProbabilisticTuple<IProbabilistic>(object.size() + 2, false);

		System.arraycopy(object.getAttributes(), 0, out.getAttributes(), 0, inputSize);

		out.setAttribute(inputSize, object.getMetadata().getExistence());

		out.setMetadata(object.getMetadata());
		out.setRequiresDeepClone(object.requiresDeepClone());
		transfer(out);
	}

	@Override
	public AbstractPipe<Tuple<IProbabilistic>, Tuple<IProbabilistic>> clone() {
		return new RelationalExistenceToPayloadPO(this);
	}

}
