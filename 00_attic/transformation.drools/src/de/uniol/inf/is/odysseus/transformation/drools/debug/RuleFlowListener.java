package de.uniol.inf.is.odysseus.transformation.drools.debug;

import org.drools.WorkingMemory;
import org.drools.event.RuleFlowCompletedEvent;
import org.drools.event.RuleFlowEventListener;
import org.drools.event.RuleFlowGroupActivatedEvent;
import org.drools.event.RuleFlowGroupDeactivatedEvent;
import org.drools.event.RuleFlowNodeTriggeredEvent;
import org.drools.event.RuleFlowStartedEvent;

public class RuleFlowListener implements RuleFlowEventListener {

	private ILogger logger;
	private static String PREFIX = "Rule Flow: ";
	
	public RuleFlowListener(ILogger logger){
		this.logger = logger;
	}
	
	
	@Override
	public void beforeRuleFlowStarted(RuleFlowStartedEvent event, WorkingMemory workingMemory) {
		logger.printlog(PREFIX+"Starting \""+event.getProcessInstance().getProcessName()+"\"...");

	}

	@Override
	public void afterRuleFlowStarted(RuleFlowStartedEvent event, WorkingMemory workingMemory) {
		logger.printlog(PREFIX+"\""+event.getProcessInstance().getProcessName()+"\" started.");

	}

	@Override
	public void beforeRuleFlowCompleted(RuleFlowCompletedEvent event, WorkingMemory workingMemory) {
		logger.printlog(PREFIX+"Comleting \""+event.getProcessInstance().getProcessName()+"\"...");

	}

	@Override
	public void afterRuleFlowCompleted(RuleFlowCompletedEvent event, WorkingMemory workingMemory) {
		logger.printlog(PREFIX+"\""+event.getProcessInstance().getProcessName()+"\" completed.");

	}

	@Override
	public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event, WorkingMemory workingMemory) {
		logger.printlog(PREFIX+"Activating group \""+event.getRuleFlowGroup().getName()+"\"...");
	}

	@Override
	public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event, WorkingMemory workingMemory) {		
		logger.printlog(PREFIX+"Group \""+event.getRuleFlowGroup().getName()+"\" activated.");
	}

	@Override
	public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event, WorkingMemory workingMemory) {
		logger.printlog(PREFIX+"Deactivating group \""+event.getRuleFlowGroup().getName()+"\"...");

	}

	@Override
	public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event, WorkingMemory workingMemory) {
		logger.printlog(PREFIX+"Group \""+event.getRuleFlowGroup().getName()+"\" deactivated.");

	}

	@Override
	public void beforeRuleFlowNodeTriggered(RuleFlowNodeTriggeredEvent event, WorkingMemory workingMemory) {
		logger.printlog(PREFIX+"Triggering node \""+event.getRuleFlowNodeInstance().getNodeName()+"\"...");

	}

	@Override
	public void afterRuleFlowNodeTriggered(RuleFlowNodeTriggeredEvent event, WorkingMemory workingMemory) {
		logger.printlog(PREFIX+"Node \""+event.getRuleFlowNodeInstance().getNodeName()+"\" triggered.");

	}

	@Override
	public void beforeRuleFlowNodeLeft(RuleFlowNodeTriggeredEvent event, WorkingMemory workingMemory) {
		logger.printlog(PREFIX+"Leaving node \""+event.getRuleFlowNodeInstance().getNodeName()+"\"...");

	}

	@Override
	public void afterRuleFlowNodeLeft(RuleFlowNodeTriggeredEvent event,	WorkingMemory workingMemory) {
		logger.printlog(PREFIX+"Node \""+event.getRuleFlowNodeInstance().getNodeName()+"\" left.");

	}

}
