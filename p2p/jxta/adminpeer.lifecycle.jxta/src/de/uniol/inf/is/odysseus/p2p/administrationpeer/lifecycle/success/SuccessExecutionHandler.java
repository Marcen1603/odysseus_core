package de.uniol.inf.is.odysseus.p2p.administrationpeer.lifecycle.success;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

/**
 * Handler that get active each time
 * 
 * @author Marco Grawunder
 * 
 * @param <F>
 */

public class SuccessExecutionHandler<F> extends AbstractExecutionHandler<F> {
	
	static Logger logger = LoggerFactory.getLogger(SuccessExecutionHandler.class);

	static private Map<Lifecycle, Lifecycle> followingCycle = new HashMap<Lifecycle, Lifecycle>();
	{
		// TODO: Wieso gibt es hier RUNNING aber keine Implementierung dafuer?
		followingCycle.put(Lifecycle.NEW, Lifecycle.SPLIT);
		followingCycle.put(Lifecycle.SPLIT, Lifecycle.DISTRIBUTION);
		followingCycle.put(Lifecycle.DISTRIBUTION, Lifecycle.GRANTED);
		followingCycle.put(Lifecycle.GRANTED, Lifecycle.RUNNING);
		followingCycle.put(Lifecycle.RUNNING, Lifecycle.TERMINATED);
	}

	public SuccessExecutionHandler() {
		super();
		setProvidedLifecycle(Lifecycle.SUCCESS);

	}

	public SuccessExecutionHandler(
			SuccessExecutionHandler<F> successExecutionHandler) {
		super(successExecutionHandler);
	}

	@Override
	public IExecutionHandler<F> clone() {
		return new SuccessExecutionHandler<F>(this);
	}

	@Override
	public void run() {
		P2PQuery q = getExecutionListenerCallback().getQuery();
		Lifecycle oldState = q.getHistory().get(getExecutionListenerCallback().getQuery().getHistory().size()-2);
		Lifecycle newState = followingCycle.get(oldState);
		logger.debug("Changing state from " + oldState + " to " + newState);
		getExecutionListenerCallback().changeState(newState);
	}

}
