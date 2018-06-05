package de.uniol.inf.is.odysseus.admission.rules;

import de.uniol.inf.is.odysseus.admission.AdmissionRuleGroup;
import de.uniol.inf.is.odysseus.admission.IAdmissionActions;
import de.uniol.inf.is.odysseus.admission.IAdmissionRule;
import de.uniol.inf.is.odysseus.admission.IAdmissionStatus;
import de.uniol.inf.is.odysseus.admission.action.ExecutorAdmissionActionComponent;
import de.uniol.inf.is.odysseus.admission.event.QueryStartAdmissionEvent;
import de.uniol.inf.is.odysseus.admission.status.ExecutorAdmissionStatusComponent;

public class MaxQueryCountAdmissionRule implements IAdmissionRule<QueryStartAdmissionEvent> {

	@Override
	public Class<QueryStartAdmissionEvent> getEventType() {
		return QueryStartAdmissionEvent.class;
	}

	@Override
	public AdmissionRuleGroup getRouleGroup() {
		return AdmissionRuleGroup.REACT;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public boolean isExecutable(QueryStartAdmissionEvent event, IAdmissionStatus status) {
		return status.hasStatusComponent(ExecutorAdmissionStatusComponent.class);
	}

	@Override
	public void execute(QueryStartAdmissionEvent event, IAdmissionStatus status, IAdmissionActions actions) {
		ExecutorAdmissionStatusComponent executorStatus = status.getStatusComponent(ExecutorAdmissionStatusComponent.class);
		ExecutorAdmissionActionComponent actionComponent = actions.getAdmissionActionComponent(ExecutorAdmissionActionComponent.class);
		
		if( executorStatus.getRunningQueryCount() > 2 ) {
			actionComponent.stopQuery(event.getQueryID());
		}
	}
}
