package de.uniol.inf.is.odysseus.admission.control;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.admission.AdmissionRuleGroup;
import de.uniol.inf.is.odysseus.admission.IAdmissionActions;
import de.uniol.inf.is.odysseus.admission.IAdmissionControl;
import de.uniol.inf.is.odysseus.admission.IAdmissionEvent;
import de.uniol.inf.is.odysseus.admission.IAdmissionRule;
import de.uniol.inf.is.odysseus.admission.IAdmissionStatus;
import de.uniol.inf.is.odysseus.admission.action.AdmissionActions;
import de.uniol.inf.is.odysseus.admission.activator.AdmissionPlugIn;
import de.uniol.inf.is.odysseus.admission.status.AdmissionStatus;

public class AdmissionControl implements IAdmissionControl {

	private static final Logger LOG = LoggerFactory.getLogger(AdmissionControl.class);
	
	private final ConcurrentLinkedDeque<IAdmissionEvent> collectedEvents = new ConcurrentLinkedDeque<IAdmissionEvent>();
	private final IAdmissionStatus admissionStatus = new AdmissionStatus();
	private final IAdmissionActions admissionActions = new AdmissionActions();
	
	private Thread execThread;
	private Boolean inProcessing = false;
	
	@Override
	public <E extends IAdmissionEvent> void processEventDelayedAsync(final E event, final int delayMillis) {
		Thread t = new Thread( new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(delayMillis);
				} catch (InterruptedException e) {
				}
				
				processEventInternal(event);
			}
		});
		t.setName("Delayed admission event process thread");
		t.setDaemon(true);
		t.start();
	}
	
	@Override
	public void processEventAsync(final IAdmissionEvent event) {
		processEventInternal(event);
	}

	@SuppressWarnings("unchecked")
	private void processEventInternal(final IAdmissionEvent event) {
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
									rule.execute(event, admissionStatus, admissionActions);
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
}
