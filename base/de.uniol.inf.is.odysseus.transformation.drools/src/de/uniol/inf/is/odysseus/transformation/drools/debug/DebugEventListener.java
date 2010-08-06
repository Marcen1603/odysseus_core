package de.uniol.inf.is.odysseus.transformation.drools.debug;

import org.drools.event.AgendaEventListener;
import org.drools.event.RuleFlowEventListener;
import org.drools.event.WorkingMemoryEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class DebugEventListener implements ILogger{
	
	private Logger logger;
	private boolean on;
	
	public DebugEventListener(String loggername, boolean on){
		this.logger = LoggerFactory.getLogger(loggername);
		this.on = on;
	}
	
	public WorkingMemoryEventListener getWorkingMemoryEventListener(){
		return new WorkingMemoryListener(this);
	}
	
	public AgendaEventListener getAgendaEventListener(){
		return new AgendaListener(this);
	}

	public RuleFlowEventListener getRuleFlowEventListener(){
		return new RuleFlowListener(this);
	}

	@Override
	public void printlog(String message) {
		if(on){
			logger.debug(message);
		}
		
		
	}
	
	
	


}
