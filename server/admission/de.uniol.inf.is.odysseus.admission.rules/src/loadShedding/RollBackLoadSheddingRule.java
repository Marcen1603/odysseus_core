package loadShedding;

import de.uniol.inf.is.odysseus.admission.AdmissionRuleGroup;
import de.uniol.inf.is.odysseus.admission.IAdmissionActions;
import de.uniol.inf.is.odysseus.admission.IAdmissionRule;
import de.uniol.inf.is.odysseus.admission.IAdmissionStatus;
import de.uniol.inf.is.odysseus.admission.event.TimingAdmissionEvent;
import de.uniol.inf.is.odysseus.admission.status.ILoadSheddingAdmissionStatusComponent;
import de.uniol.inf.is.odysseus.admission.status.SystemLoadAdmissionStatusComponent;

public class RollBackLoadSheddingRule implements IAdmissionRule<TimingAdmissionEvent> {

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
		if (status.hasStatusComponent(ILoadSheddingAdmissionStatusComponent.class) && 
				status.hasStatusComponent(SystemLoadAdmissionStatusComponent.class)) {
			SystemLoadAdmissionStatusComponent systemLoadStatus = status.getStatusComponent(SystemLoadAdmissionStatusComponent.class);
			return systemLoadStatus.getCpuLoadPercentage() <= 70;
		} else {
			return false;
		}
	}

	@Override
	public void execute(TimingAdmissionEvent event, IAdmissionStatus status, IAdmissionActions actions) {
		ILoadSheddingAdmissionStatusComponent loadSheddingStatus = status.getStatusComponent(ILoadSheddingAdmissionStatusComponent.class);
		loadSheddingStatus.rollBackLoadShedding();
	}

}
