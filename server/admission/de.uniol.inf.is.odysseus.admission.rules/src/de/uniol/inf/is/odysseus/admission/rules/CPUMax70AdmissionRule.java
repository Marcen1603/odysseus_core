package de.uniol.inf.is.odysseus.admission.rules;

import de.uniol.inf.is.odysseus.admission.AdmissionRuleGroup;
import de.uniol.inf.is.odysseus.admission.IAdmissionActions;
import de.uniol.inf.is.odysseus.admission.IAdmissionRule;
import de.uniol.inf.is.odysseus.admission.IAdmissionStatus;
import de.uniol.inf.is.odysseus.admission.action.ExecutorAdmissionActionComponent;
import de.uniol.inf.is.odysseus.admission.event.QueryStartAdmissionEvent;
import de.uniol.inf.is.odysseus.admission.status.SystemLoadAdmissionStatusComponent;

public class CPUMax70AdmissionRule implements IAdmissionRule<QueryStartAdmissionEvent> {

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
		return status.hasStatusComponent(SystemLoadAdmissionStatusComponent.class);
	}

	@Override
	public void execute(QueryStartAdmissionEvent event, IAdmissionStatus status, IAdmissionActions actions) {
		SystemLoadAdmissionStatusComponent systemLoadStatus = status.getStatusComponent(SystemLoadAdmissionStatusComponent.class);
		ExecutorAdmissionActionComponent actionComponent = actions.getAdmissionActionComponent(ExecutorAdmissionActionComponent.class);
		
		if( systemLoadStatus.getCpuLoadPercentage() >= 70 ) {
			actionComponent.stopQuery(event.getQueryID());
		}
	}

}
