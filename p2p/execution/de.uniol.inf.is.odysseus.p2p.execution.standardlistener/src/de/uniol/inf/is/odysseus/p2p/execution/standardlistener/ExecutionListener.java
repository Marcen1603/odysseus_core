package de.uniol.inf.is.odysseus.p2p.execution.standardlistener;

import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.AbstractExecutionListener;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public class ExecutionListener extends AbstractExecutionListener{
	

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
			System.out.println("aktueller Zustand: "+getQuery().getStatus());
			if(getHandler().containsKey(getQuery().getStatus())) {
				execute(getQuery().getStatus());
			}
			//Sonst hängen wir ewig in einer Schleife fest, falls es keinen passenden ExecutionHandler gibt.
			else {
				getCallback().changeState(Lifecycle.FAILED);
				continue;
				
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
