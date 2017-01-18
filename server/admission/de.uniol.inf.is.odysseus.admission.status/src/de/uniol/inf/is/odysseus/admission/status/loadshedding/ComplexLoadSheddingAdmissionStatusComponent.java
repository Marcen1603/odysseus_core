package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.admission.status.AdmissionStatusPlugIn;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Provides the status for complex load shedding.
 * 
 * The CPU load together witch the latency and queue lengths of each query is used in this status component.
 * 
 * @author Jannes
 *
 */
public class ComplexLoadSheddingAdmissionStatusComponent implements ILoadSheddingAdmissionStatusComponent {

	private final String NAME = "complex";
	
	private final QueueLengthsAdmissionMonitor QUEUE_LENGTHS_MONITOR = new QueueLengthsAdmissionMonitor();
	private final LatencyAdmissionMonitor LATENCY_MONITOR = new LatencyAdmissionMonitor();
	
	/**
	 * Contains all queries with active load shedding.
	 */
	private HashMap<Integer, Integer> activeQueries = new HashMap<Integer, Integer>();
	
	/**
	 * Contains all queries with maximal shedding-factor.
	 */
	private ArrayList<Integer> maxSheddingQueries = new ArrayList<Integer>();
	
	public ComplexLoadSheddingAdmissionStatusComponent() {
		LoadSheddingAdmissionStatusRegistry.addLoadSheddingAdmissionComponent(this);
	}
	
	@Override
	public void addQuery(int queryID) {
		IPhysicalQuery query = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan(superUser).getQueryById(queryID, superUser);
		for(String name : AdmissionStatusPlugIn.getServerExecutor().getPreTransformationHandlerNames()) {
			LoggerFactory.getLogger(this.getClass()).info(name);
		}
		QUEUE_LENGTHS_MONITOR.addQuery(query);
		LATENCY_MONITOR.addQuery(query);
	}

	@Override
	public void removeQuery(int queryID) {
		IPhysicalQuery query = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan(superUser).getQueryById(queryID, superUser);
		QUEUE_LENGTHS_MONITOR.removeQuery(query);
		LATENCY_MONITOR.removeQuery(query);
	}

	@Override
	public void runLoadShedding() {
		tryComplexLoadShedding();
	}

	@Override
	public void rollBackLoadShedding() {
		int queryID = getRandomActiveQueryID();
		if (queryID < 0) {
			return;
		}
		
		if (activeQueries.containsKey(queryID)) {
			if (maxSheddingQueries.contains(queryID)) {
				maxSheddingQueries.remove(queryID);
			}
			int sheddingFactor = activeQueries.get(queryID) - 10;
			if (sheddingFactor <= 0) {
				sheddingFactor = 0;
				activeQueries.remove(queryID);
			}
			AdmissionStatusPlugIn.getServerExecutor().partialQuery(queryID, sheddingFactor, superUser);
		}
	}

	@Override
	public void measureStatus() {
		QUEUE_LENGTHS_MONITOR.updateMeasurements();
	}
	
	private boolean tryComplexLoadShedding() {
		ArrayList<IPhysicalQuery> queuelengths = removeQuerysWithMaxSheddingFactor
				(QUEUE_LENGTHS_MONITOR.getQuerysWithIncreasingTendency());
		
		ArrayList<IPhysicalQuery> latencies = removeQuerysWithMaxSheddingFactor
				(LATENCY_MONITOR.getQuerysWithIncreasingTendency());
		
		HashMap<IPhysicalQuery, Integer> queryRanks = new HashMap<>();
		
		if(!queuelengths.isEmpty() && !latencies.isEmpty()) {
			for(int q = 0; q < queuelengths.size(); q++) {
				for(int l = 0; l < latencies.size(); l++) {
					if(queuelengths.get(q) == latencies.get(l)) {
						queryRanks.put(queuelengths.get(q), (q + l));
						latencies.remove(l);
						break;
					}
				}
				if(latencies.isEmpty()) {
					break;
				}
			}
			if(!queryRanks.isEmpty()) {
				IPhysicalQuery partialQuery = null;
				int rank = -1;
				for (IPhysicalQuery query : queryRanks.keySet()) {
					if(queryRanks.get(query) > rank) {
						partialQuery = query;
						rank = queryRanks.get(query);
					}
				}
				
				int sheddingFactor = getAllowedFactor(partialQuery);
				if(activeQueries.containsKey(partialQuery.getID())) {
					activeQueries.replace(partialQuery.getID(), sheddingFactor);
				} else {
					activeQueries.put(partialQuery.getID(), sheddingFactor);
				}
				AdmissionStatusPlugIn.getServerExecutor().partialQuery(partialQuery.getID(), sheddingFactor, superUser);
				return true;
			}
		}
		return false;
	}
	
	private ArrayList<IPhysicalQuery> removeQuerysWithMaxSheddingFactor(ArrayList<IPhysicalQuery> list) {
		for(IPhysicalQuery query : list) {
			if (maxSheddingQueries.contains(query)) {
				list.remove(query);
			}
		}
		return list;
	}
	
	private int getAllowedFactor(IPhysicalQuery query) {
		int factor;
		if(activeQueries.containsKey(query.getID())) {
			factor = activeQueries.get(query.getID()) + LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
		} else {
			factor = LoadSheddingAdmissionStatusRegistry.getSheddingGrowth();
		}
		
		ILogicalQuery logQuery = query.getLogicalQuery();
		int maxSheddingFactor = (int) logQuery.getParameter("maxSheddingFactor");
		if(factor >= maxSheddingFactor) {
			factor = factor - (factor - maxSheddingFactor);
			maxSheddingQueries.add(query.getID());
		}
		return factor;
	}
	
	/**
	 * Returns a queryID, which query has load shedding activated.
	 * @return
	 */
	private int getRandomActiveQueryID() {
		if (activeQueries.isEmpty()) {
			return -1;
		}
		
		ArrayList<Integer> list = new ArrayList<Integer>(activeQueries.keySet());
		Collections.shuffle(list);
		return list.get(0);
	}

	@Override
	public String getComponentName() {
		return NAME;
	}

}
