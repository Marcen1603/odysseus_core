package de.uniol.inf.is.odysseus.p2p.execution.standardlistener;

import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.AbstractExecutionListener;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

public class ExecutionListener extends AbstractExecutionListener{
	
	static Logger logger = LoggerFactory.getLogger(ExecutionListener.class);

	public ExecutionListener(P2PQuery query) {
		super(query);
		setCallback(new ExecutionListenerCallback());
	}
	
//	@Override
//	protected void execute(Lifecycle lifecycle) {
//		if(getHandler().containsKey(lifecycle)) {
//			execute(getHandler().get(lifecycle));
//		}
//	}
	
	@Override
	protected void execute(IExecutionHandler<?> executionHandler){
		if(getActualExecutionThread()!=null) {
			getActualExecutionThread().interrupt();
		}
		// Jetzt muss man hier eigentlich warten bis der alte fertig ist ...
		// Warum wird der überhaupt interrupted??
		setActualExecutionThread(new Thread(executionHandler));
		getActualExecutionThread().start();
	
	}

	@Override
	public void run() {
		
		while(getQuery().getStatus()!=Lifecycle.TERMINATED) {
			Lifecycle currentQueryState = getQuery().getStatus();
			IExecutionHandler<?> nextHandler = getHandler().get(currentQueryState);
			logger.debug("aktueller Zustand: "+currentQueryState);
			if(nextHandler != null) {
				logger.debug("Executing "+nextHandler);
				execute(nextHandler);
			}
			//Sonst hÃ¤ngen wir ewig in einer Schleife fest, falls es keinen passenden ExecutionHandler gibt.
			else {
				logger.warn("no Handler for Lifecycle "+currentQueryState+" found!");
				getCallback().changeState(Lifecycle.FAILED);
				return;
//				continue;
				
			}
			synchronized (this) {
				if(currentQueryState == getQuery().getStatus()) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

}
