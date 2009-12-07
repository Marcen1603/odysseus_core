package de.uniol.inf.is.odysseus.p2p.execution.standardhandler;

import de.uniol.inf.is.odysseus.p2p.peer.IPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandlerFactory;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public class ExecutionHandlerFactory implements IExecutionHandlerFactory {

	private IExecutionHandler<? extends IPeer> handler;
	
	@SuppressWarnings("unchecked")
	@Override
	public IExecutionHandler<? extends IPeer> getNewInstance() {
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

	@Override
	public void setExecutionHandler(IExecutionHandler<? extends IPeer> handler) {
		this.handler = handler;
		
	}

}
