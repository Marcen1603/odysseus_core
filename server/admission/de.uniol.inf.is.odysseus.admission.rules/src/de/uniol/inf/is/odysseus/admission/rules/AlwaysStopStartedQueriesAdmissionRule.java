package de.uniol.inf.is.odysseus.admission.rules;

import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.admission.AdmissionRuleGroup;
import de.uniol.inf.is.odysseus.admission.IAdmissionAction;
import de.uniol.inf.is.odysseus.admission.IAdmissionRule;
import de.uniol.inf.is.odysseus.admission.IAdmissionStatus;
import de.uniol.inf.is.odysseus.admission.action.StopQueryAdmissionAction;
import de.uniol.inf.is.odysseus.admission.event.QueryStartAdmissionEvent;

public class AlwaysStopStartedQueriesAdmissionRule implements IAdmissionRule<QueryStartAdmissionEvent> {

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
		return true;
	}

	@Override
	public List<IAdmissionAction> execute(QueryStartAdmissionEvent event, IAdmissionStatus status) {
		return Lists.<IAdmissionAction>newArrayList(new StopQueryAdmissionAction(event.getQueryID()));
	}
}
