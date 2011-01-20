package de.uniol.inf.is.odysseus.p2p.execution.standardlistener;

import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.AbstractExecutionListener;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

public class ExecutionListener extends AbstractExecutionListener{
	
	static Logger logger = LoggerFactory.getLogger(ExecutionListener.class);

	public ExecutionListener(Query query) {
		super(query);
		setCallback(new ExecutionListenerCallback());
	}
	
	@Override
	protected void execute(Lifecycle lifecycle) {
		if(getHandler().containsKey(lifecycle)) {
			if(getActualExecutionThread()!=null) {
				getActualExecutionThread().interrupt();
			}
			setActualExecutionThread(new Thread(getHandler().get(lifecycle)));
			getActualExecutionThread().start();
		}
	}

	@Override
	public void run() {
		
		while(getQuery().getStatus()!=Lifecycle.TERMINATED) {
			Lifecycle temp = getQuery().getStatus();
			logger.debug("aktueller Zustand: "+temp);
			if(getHandler().containsKey(temp)) {
				logger.debug("Executing "+getHandler().get(temp));
				execute(temp);
			}
			//Sonst hängen wir ewig in einer Schleife fest, falls es keinen passenden ExecutionHandler gibt.
			else {
				logger.warn("no Handler for Lifecycle "+temp+" found!");
				getCallback().changeState(Lifecycle.FAILED);
				return;
//				continue;
				
			}
			synchronized (this) {
				if(temp == getQuery().getStatus()) {
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
