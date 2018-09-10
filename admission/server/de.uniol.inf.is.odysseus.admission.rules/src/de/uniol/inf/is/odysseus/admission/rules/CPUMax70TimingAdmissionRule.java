package de.uniol.inf.is.odysseus.admission.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.admission.AdmissionRuleGroup;
import de.uniol.inf.is.odysseus.admission.IAdmissionActions;
import de.uniol.inf.is.odysseus.admission.IAdmissionRule;
import de.uniol.inf.is.odysseus.admission.IAdmissionStatus;
import de.uniol.inf.is.odysseus.admission.action.ExecutorAdmissionActionComponent;
import de.uniol.inf.is.odysseus.admission.event.TimingAdmissionEvent;
import de.uniol.inf.is.odysseus.admission.status.ExecutorAdmissionStatusComponent;
import de.uniol.inf.is.odysseus.admission.status.SystemLoadAdmissionStatusComponent;

public class CPUMax70TimingAdmissionRule implements IAdmissionRule<TimingAdmissionEvent> {

	@Override
	public Class<TimingAdmissionEvent> getEventType() {
		return TimingAdmissionEvent.class;
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
	public boolean isExecutable(TimingAdmissionEvent event, IAdmissionStatus status) {
		SystemLoadAdmissionStatusComponent systemLoadStatus = status.getStatusComponent(SystemLoadAdmissionStatusComponent.class);
		ExecutorAdmissionStatusComponent executorStatus = status.getStatusComponent(ExecutorAdmissionStatusComponent.class);

		return systemLoadStatus.getCpuLoadPercentage() >= 70 && executorStatus.hasRunningQueries();
	}

	@Override
	public void execute(TimingAdmissionEvent event, IAdmissionStatus status, IAdmissionActions actions) {
		ExecutorAdmissionActionComponent actionComponent = actions.getAdmissionActionComponent(ExecutorAdmissionActionComponent.class);
		ExecutorAdmissionStatusComponent executorStatus = status.getStatusComponent(ExecutorAdmissionStatusComponent.class);
		
		actionComponent.stopQuery(selectOneQuery(executorStatus.getRunningQueryIDs()));
	}

	private static int selectOneQuery(Collection<Integer> ids) {
		return ids.iterator().next();
	}

}
