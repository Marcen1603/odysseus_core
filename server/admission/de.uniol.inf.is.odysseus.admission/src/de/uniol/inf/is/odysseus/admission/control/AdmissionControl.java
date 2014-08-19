package de.uniol.inf.is.odysseus.admission.control;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.admission.AdmissionRuleGroup;
import de.uniol.inf.is.odysseus.admission.IAdmissionAction;
import de.uniol.inf.is.odysseus.admission.IAdmissionControl;
import de.uniol.inf.is.odysseus.admission.IAdmissionEvent;
import de.uniol.inf.is.odysseus.admission.IAdmissionRule;
import de.uniol.inf.is.odysseus.admission.IAdmissionStatus;
import de.uniol.inf.is.odysseus.admission.activator.AdmissionPlugIn;
import de.uniol.inf.is.odysseus.admission.status.AdmissionStatus;

public class AdmissionControl implements IAdmissionControl {

	private static final Logger LOG = LoggerFactory.getLogger(AdmissionControl.class);
	
	private final ConcurrentLinkedDeque<IAdmissionEvent> collectedEvents = new ConcurrentLinkedDeque<IAdmissionEvent>();
	private final IAdmissionStatus admissionStatus = new AdmissionStatus();
	
	private Thread execThread;
	private Boolean inProcessing = false;
	
	@SuppressWarnings("unchecked")
	@Override
	public void processEventAsync(final IAdmissionEvent event) {
		Preconditions.checkNotNull(event, "Admission event must not be null!");
		
		LOG.debug("Got new admission event of type '{}': {}", event.getClass().getName(), event);
		collectedEvents.push(event);

		synchronized( inProcessing ) {
			if( inProcessing ) {
				LOG.debug("Already in admission event processing now");
				return;
			}
			LOG.debug("Start admission event processing");
			inProcessing = true;
		}
		
		execThread = new Thread(new Runnable() {

			@SuppressWarnings("rawtypes")
			@Override
			public void run() {
				try {
					while( !collectedEvents.isEmpty() ) {
						IAdmissionEvent event = collectedEvents.removeLast();
						LOG.debug("Start processing admission event {} of type {}", event, event.getClass().getName());
						
						for( AdmissionRuleGroup group : AdmissionRuleGroup.values()) {
							List<? extends IAdmissionRule> rules = AdmissionPlugIn.getAdmissionRuleRegistry().getAdmissionRules(group, event.getClass());
							
							for( IAdmissionRule rule : rules ) {
								if( rule.isExecutable(event, admissionStatus) ) {
									LOG.debug("Executing rule {}", rule.getClass().getName());
									List<IAdmissionAction> actions = rule.execute(event, admissionStatus);
									
									executeActions(actions, event, admissionStatus);
									continue;
								}
							}
						}
						
						LOG.debug("No executable rule found");
					}
				} finally {
					synchronized( inProcessing ) {
						inProcessing = false;
						LOG.debug("Finished admission event processing");
					}
				}
			}
		});
		execThread.setDaemon(true);
		execThread.setName("Admission ProcessEvent '" + event.getClass().getName() + "'");
		execThread.start();
	}

	private static void executeActions( List<IAdmissionAction> actions, IAdmissionEvent event, IAdmissionStatus status ) {
		Preconditions.checkNotNull(actions, "List of admission actions must not be null!");
		
		List<IAdmissionAction> executedActions = Lists.newArrayList();
		try {
			for( IAdmissionAction action : actions ) {
				action.execute(event, status);
				
				executedActions.add(action);
			}
		} catch( Throwable t ) {
			revertAllActions(executedActions, event, status);
		}
	}

	private static void revertAllActions(List<IAdmissionAction> executedActions, IAdmissionEvent event, IAdmissionStatus status) {
		for( IAdmissionAction executedAction : executedActions ) {
			try {
				executedAction.revert(event, status);
			} catch( Throwable t ) {
				LOG.error("Could not revert admission action", t);
			}
		}
	}
	
}
