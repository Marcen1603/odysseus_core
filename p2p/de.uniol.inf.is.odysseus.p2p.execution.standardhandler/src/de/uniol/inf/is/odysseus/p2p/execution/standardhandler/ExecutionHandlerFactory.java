package de.uniol.inf.is.odysseus.p2p.execution.standardhandler;

import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandlerFactory;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public class ExecutionHandlerFactory implements IExecutionHandlerFactory {

	private IExecutionHandler handler;
	
	public ExecutionHandlerFactory(IExecutionHandler handler) {
		this.handler = handler;
	}
	
	@Override
	public IExecutionHandler getNewInstance() {
		try {
			return this.handler.getClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Lifecycle getProvidedLifecycle() {
		return this.handler.getProvidedLifecycle();
	}

}
