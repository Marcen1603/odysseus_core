package de.uniol.inf.is.odysseus.admission.rules;

import de.uniol.inf.is.odysseus.admission.AdmissionRuleGroup;
import de.uniol.inf.is.odysseus.admission.IAdmissionActions;
import de.uniol.inf.is.odysseus.admission.IAdmissionRule;
import de.uniol.inf.is.odysseus.admission.IAdmissionStatus;
import de.uniol.inf.is.odysseus.admission.action.ExecutorAdmissionActionComponent;
import de.uniol.inf.is.odysseus.admission.status.SystemLoadAdmissionStatusComponent;

public class CheckPartialQueryAdmissionRule implements IAdmissionRule<CheckQueryAgainAdmissionEvent> {

	@Override
	public Class<CheckQueryAgainAdmissionEvent> getEventType() {
		return CheckQueryAgainAdmissionEvent.class;
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
	public boolean isExecutable(CheckQueryAgainAdmissionEvent event, IAdmissionStatus status) {
		SystemLoadAdmissionStatusComponent systemLoadStatus = status.getStatusComponent(SystemLoadAdmissionStatusComponent.class);

		return systemLoadStatus.getCpuLoadPercentage() >= 70;
	}

	@Override
	public void execute(CheckQueryAgainAdmissionEvent event, IAdmissionStatus status, IAdmissionActions actions) {
		ExecutorAdmissionActionComponent actionComponent = actions.getAdmissionActionComponent(ExecutorAdmissionActionComponent.class);
		
		actionComponent.stopQuery(event.getQueryID());
	}
}
