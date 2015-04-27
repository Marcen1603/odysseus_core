package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.PlanModificationActionAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;

public class PlanModificationActionPO extends AbstractSink<Tuple<?>> {

	private final IServerExecutor executor;
	
	public PlanModificationActionPO(PlanModificationActionAO ao, IServerExecutor executor) {
		Preconditions.checkNotNull(executor, "ServerExecutor must not be null!");
		Preconditions.checkNotNull(ao, "PlanModificationActionAO must not be null!");
		
		this.executor = executor;
	}
	
	@Override
	protected void process_next(Tuple<?> object, int port) {
		System.err.println(object);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		System.err.println(punctuation);
	}

}
