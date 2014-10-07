package de.uniol.inf.is.odysseus.admission.rules.btw;

import java.util.Collection;
import java.util.Random;

import de.uniol.inf.is.odysseus.admission.AdmissionRuleGroup;
import de.uniol.inf.is.odysseus.admission.IAdmissionActions;
import de.uniol.inf.is.odysseus.admission.IAdmissionRule;
import de.uniol.inf.is.odysseus.admission.IAdmissionStatus;
import de.uniol.inf.is.odysseus.admission.action.ExecutorAdmissionActionComponent;
import de.uniol.inf.is.odysseus.admission.event.TimingAdmissionEvent;
import de.uniol.inf.is.odysseus.admission.status.ExecutorAdmissionStatusComponent;
import de.uniol.inf.is.odysseus.admission.status.IPhysicalQuerySelector;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class PauseRandomQueryWhenCpuOver70AdmissionRule implements IAdmissionRule<TimingAdmissionEvent> {

	private static final Random RAND = new Random();
	
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
//		SystemLoadAdmissionStatusComponent systemLoadStatus = status.getStatusComponent(SystemLoadAdmissionStatusComponent.class);
		ExecutorAdmissionStatusComponent executorStatus = status.getStatusComponent(ExecutorAdmissionStatusComponent.class);

		return executorStatus.getRunningQueryCount() >= 7;
//		return systemLoadStatus.getCpuLoadPercentage() >= 70 && ( executorStatus.hasRunningQueries() );
	}

	@Override
	public void execute(TimingAdmissionEvent event, IAdmissionStatus status, IAdmissionActions actions) {
		ExecutorAdmissionActionComponent actionComponent = actions.getAdmissionActionComponent(ExecutorAdmissionActionComponent.class);
		ExecutorAdmissionStatusComponent executorStatus = status.getStatusComponent(ExecutorAdmissionStatusComponent.class);

		Collection<Integer> queries = executorStatus.selectQueries(new IPhysicalQuerySelector() {
			@Override
			public boolean isSelected(IPhysicalQuery query) {
				return query.getPriority() < 10 && query.getState() == QueryState.RUNNING;
			}
		});
		
		if( queries.isEmpty() ) {
			queries = executorStatus.getRunningQueryIDs();
		}
		
		int queryID = selectRandomQuery(queries);
		
		actionComponent.suspendQuery(queryID);
	}

	private static int selectRandomQuery(Collection<Integer> ids) {
		int randIndex = RAND.nextInt(ids.size());

		int counter = 0;
		for( Integer id : ids ) {
			if( counter == randIndex ) {
				return id;
			} 
			counter++;
		}
		
		return ids.iterator().next();
	}
}
