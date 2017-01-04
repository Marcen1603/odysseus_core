package loadShedding;

import de.uniol.inf.is.odysseus.admission.AdmissionRuleGroup;
import de.uniol.inf.is.odysseus.admission.IAdmissionActions;
import de.uniol.inf.is.odysseus.admission.IAdmissionRule;
import de.uniol.inf.is.odysseus.admission.IAdmissionStatus;
import de.uniol.inf.is.odysseus.admission.event.QueryStopAdmissionEvent;
import de.uniol.inf.is.odysseus.admission.status.ILoadSheddingAdmissionStatusComponent;

public class RemoveQueryFromStatusComponentsRule implements IAdmissionRule<QueryStopAdmissionEvent> {

	@Override
	public Class<QueryStopAdmissionEvent> getEventType() {
		return QueryStopAdmissionEvent.class;
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
	public boolean isExecutable(QueryStopAdmissionEvent event, IAdmissionStatus status) {
		return status.hasStatusComponent(ILoadSheddingAdmissionStatusComponent.class);
	}

	@Override
	public void execute(QueryStopAdmissionEvent event, IAdmissionStatus status, IAdmissionActions actions) {
		ILoadSheddingAdmissionStatusComponent statusComponent = status.getStatusComponent(ILoadSheddingAdmissionStatusComponent.class);
		statusComponent.removeQuery(event.getQuery());
	}

}
