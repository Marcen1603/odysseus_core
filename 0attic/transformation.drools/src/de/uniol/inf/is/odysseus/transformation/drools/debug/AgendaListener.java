package de.uniol.inf.is.odysseus.transformation.drools.debug;

import java.util.Iterator;

import org.drools.WorkingMemory;
import org.drools.common.InternalFactHandle;
import org.drools.event.ActivationCancelledEvent;
import org.drools.event.ActivationCreatedEvent;
import org.drools.event.AfterActivationFiredEvent;
import org.drools.event.AgendaEventListener;
import org.drools.event.AgendaGroupPoppedEvent;
import org.drools.event.AgendaGroupPushedEvent;
import org.drools.event.BeforeActivationFiredEvent;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.Activation;
import org.drools.spi.AgendaGroup;

public class AgendaListener implements AgendaEventListener {

	private static String PREFIX = "Agenda:";	
	private ILogger logger;
	
	public AgendaListener(ILogger logger){
		this.logger = logger;
	}
	
	@Override
	public void activationCancelled(ActivationCancelledEvent e, WorkingMemory wm) {
		Rule rule = e.getActivation().getRule();
		logger.printlog(PREFIX+" Activation for rule \""+rule.getName()+"\" in group \""+rule.getRuleFlowGroup()+"\" cancelled.");
		logger.printlog(PREFIX+" \tCause: "+e.getCause().toString());
		this.printActivationDetails(e.getActivation());
		
	}

	@Override
	public void activationCreated(ActivationCreatedEvent e, WorkingMemory wm) {
		Rule rule = e.getActivation().getRule();
		logger.printlog(PREFIX+" Activation for rule \""+rule.getName()+"\" in group \""+rule.getRuleFlowGroup()+"\" created.");		
		this.printActivationDetails(e.getActivation());	
	}

	@Override
	public void afterActivationFired(AfterActivationFiredEvent e, WorkingMemory wm) {
		Rule rule = e.getActivation().getRule();
		logger.printlog(PREFIX+"----------------------------------------- EXITING RULE -----------------------------------------------");
		logger.printlog(PREFIX+" Activation of rule \""+rule.getName()+"\" in group \""+rule.getRuleFlowGroup()+"\" was fired.");
		this.printActivationDetails(e.getActivation());	
		this.printWMDetails(wm);
		logger.printlog(PREFIX+"------------------------------------------- RULE DONE -----------------------------------------------");
		logger.printlog(PREFIX+"\tCurrent scheduled activations:");
		for(AgendaGroup ag : wm.getAgenda().getStack()){
			for(org.drools.runtime.rule.Activation a : ag.getActivations()){
				logger.printlog(PREFIX+"\t\t"+a.getRule().getName());
			}			
		}
		
	}	

	@Override
	public void beforeActivationFired(BeforeActivationFiredEvent e, WorkingMemory wm) {
		Rule rule = e.getActivation().getRule();	
		logger.printlog(PREFIX+"-------------------------------------- STARTING NEW RULE -----------------------------------------------");
		logger.printlog(PREFIX+" Activation for rule \""+rule.getName()+"\" in group \""+rule.getRuleFlowGroup()+"\" is fired...");
		this.printActivationDetails(e.getActivation());
		this.printWMDetails(wm);
		logger.printlog(PREFIX+"------------------------------------------- RULE START -----------------------------------------------");
	}
	
	private void printWMDetails(WorkingMemory wm) {
		logger.printlog(PREFIX+"\tWorking Memory Details:");		
		logger.printlog(PREFIX+"\t\tWorking Memory content:");
		Iterator<?> iter = wm.iterateObjects();
		while(iter.hasNext()){
			logger.printlog(PREFIX+"\t\t\t"+iter.next());
		}		
	}

	@Override
	public void agendaGroupPopped(AgendaGroupPoppedEvent e, WorkingMemory wm) {
		logger.printlog(PREFIX+" Agenda group popped "+e.getAgendaGroup());
		
	}

	@Override
	public void agendaGroupPushed(AgendaGroupPushedEvent e, WorkingMemory wm) {
		logger.printlog(PREFIX+" Agenda group pushed "+e.getAgendaGroup());
		
	}
	
	private void printActivationDetails(Activation act){
		Rule rule = act.getRule();
		logger.printlog(PREFIX+"\tDeclarations");
		for(Declaration decl: rule.getDeclarations()){
			InternalFactHandle ifh = act.getTuple().get(decl);
			logger.printlog(PREFIX+"\t\t"+decl.getIdentifier()+" -> "+ifh.getObject());
		}	
		
	}
	
	
}
