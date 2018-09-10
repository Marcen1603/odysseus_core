package de.uniol.inf.is.odysseus.admission.rules.btw;

import java.util.Collection;
import java.util.Random;

import de.uniol.inf.is.odysseus.admission.AdmissionRuleGroup;
import de.uniol.inf.is.odysseus.admission.IAdmissionActions;
import de.uniol.inf.is.odysseus.admission.IAdmissionRule;
import de.uniol.inf.is.odysseus.admission.IAdmissionStatus;
import de.uniol.inf.is.odysseus.admission.action.ExecutorAdmissionActionComponent;
import de.uniol.inf.is.odysseus.admission.event.QueryStartAdmissionEvent;
import de.uniol.inf.is.odysseus.admission.status.ExecutorAdmissionStatusComponent;
import de.uniol.inf.is.odysseus.admission.status.IPhysicalQuerySelector;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class PauseStartingQueryWhenCpuOver70AdmissionRule implements IAdmissionRule<QueryStartAdmissionEvent> {

	private static final Random RAND = new Random();
	
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
//		SystemLoadAdmissionStatusComponent systemLoadStatus = status.getStatusComponent(SystemLoadAdmissionStatusComponent.class);
		ExecutorAdmissionStatusComponent executorStatus = status.getStatusComponent(ExecutorAdmissionStatusComponent.class);

//		return event.getPriority() < 10 && systemLoadStatus.getCpuLoadPercentage() >= 70;
		return executorStatus.getRunningQueryCount() >= 7;
	}

	@Override
	public void execute(QueryStartAdmissionEvent event, IAdmissionStatus status, IAdmissionActions actions) {
		ExecutorAdmissionActionComponent actionComponent = actions.getAdmissionActionComponent(ExecutorAdmissionActionComponent.class);
		ExecutorAdmissionStatusComponent executorStatus = status.getStatusComponent(ExecutorAdmissionStatusComponent.class);
		
		if( event.getPriority() < 10 ) {
			actionComponent.suspendQuery(event.getQueryID());
		} else {
			Collection<Integer> unimportantRunningQueries = executorStatus.selectQueries(new IPhysicalQuerySelector() {
				@Override
				public boolean isSelected(IPhysicalQuery query) {
					return query.getPriority() < 10 && query.getState() == QueryState.RUNNING;
				}
			});
			
			if( !unimportantRunningQueries.isEmpty() ) {
				actionComponent.suspendQuery(selectRandomQuery(unimportantRunningQueries));
			} else {
				actionComponent.suspendQuery(event.getQueryID());
			}
		}
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
