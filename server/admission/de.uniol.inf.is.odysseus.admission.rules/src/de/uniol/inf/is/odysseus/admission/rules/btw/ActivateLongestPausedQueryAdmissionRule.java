package de.uniol.inf.is.odysseus.admission.rules.btw;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.admission.AdmissionRuleGroup;
import de.uniol.inf.is.odysseus.admission.IAdmissionActions;
import de.uniol.inf.is.odysseus.admission.IAdmissionRule;
import de.uniol.inf.is.odysseus.admission.IAdmissionStatus;
import de.uniol.inf.is.odysseus.admission.action.ExecutorAdmissionActionComponent;
import de.uniol.inf.is.odysseus.admission.event.TimingAdmissionEvent;
import de.uniol.inf.is.odysseus.admission.status.ExecutorAdmissionStatusComponent;

public class ActivateLongestPausedQueryAdmissionRule implements IAdmissionRule<TimingAdmissionEvent> {

//	private static final int EVENT_COUNT_TO_REACT = 2;
//	
//	private int eventCounter = 0;
	
	@Override
	public Class<TimingAdmissionEvent> getEventType() {
		return TimingAdmissionEvent.class;
	}

	@Override
	public AdmissionRuleGroup getRouleGroup() {
		return AdmissionRuleGroup.RECOVER;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public boolean isExecutable(TimingAdmissionEvent event, IAdmissionStatus status) {
//		eventCounter++;
//		if( eventCounter % EVENT_COUNT_TO_REACT != 0 ) {
//			return false;
//		}
//		eventCounter = 0;
		
		ExecutorAdmissionStatusComponent executorStatus = status.getStatusComponent(ExecutorAdmissionStatusComponent.class);
		return executorStatus.hasSuspenedQueries();
	}

	@Override
	public void execute(TimingAdmissionEvent event, IAdmissionStatus status, IAdmissionActions actions) {
		ExecutorAdmissionStatusComponent executorStatus = status.getStatusComponent(ExecutorAdmissionStatusComponent.class);
		ExecutorAdmissionActionComponent actionComponent = actions.getAdmissionActionComponent(ExecutorAdmissionActionComponent.class);
		
		Collection<Integer> queryIDs = executorStatus.getSuspendedQueryIDs();
		Map<Integer, Long> suspendTimeMap = createSuspendTimeMap(queryIDs, executorStatus);
		Integer longestSuspendedQueryID = selectedLongestSuspendTime(suspendTimeMap);
		
		actionComponent.resumeQuery(longestSuspendedQueryID);
	}
	
	private static Integer selectedLongestSuspendTime(Map<Integer, Long> suspendTimeMap) {
		long longestTime = Long.MIN_VALUE;
		int longestQueryID = -1;
		for( Integer id : suspendTimeMap.keySet() ) {
			long time = suspendTimeMap.get(id);
			if( time > longestTime ) {
				longestQueryID = id;
				longestTime = time;
			}
		}
		return longestQueryID;
	}

	private static Map<Integer, Long> createSuspendTimeMap(Collection<Integer> queryIDs, ExecutorAdmissionStatusComponent executorStatus) {
		Map<Integer, Long> map = Maps.newHashMap();
		
		// Assumption: queryIDs only contains ids of suspended queries!
		for( Integer queryID : queryIDs ) {
			map.put(queryID, executorStatus.getQueryStateTimeMillis(queryID));
		}
		
		return map;
	}
}
