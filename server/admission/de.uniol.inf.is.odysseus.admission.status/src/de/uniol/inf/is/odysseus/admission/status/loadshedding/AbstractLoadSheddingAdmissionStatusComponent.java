package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.admission.status.AdmissionStatusPlugIn;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public abstract class AbstractLoadSheddingAdmissionStatusComponent implements ILoadSheddingAdmissionStatusComponent {

	/**
	 * Contains all allowed queries with their maximal shedding factor.
	 */
	protected volatile Map<Integer, Integer> allowedQueries = new HashMap<Integer, Integer>();
	
	/**
	 * Contains all queries with active load shedding and their actual shedding factor.
	 */
	protected volatile Map<Integer, Integer> activeQueries = new HashMap<Integer, Integer>();
	
	/**
	 * Contains all queries with maximal shedding-factor.
	 */
	protected volatile List<Integer> maxSheddingQueries = new ArrayList<Integer>();
	
	public AbstractLoadSheddingAdmissionStatusComponent() {
		LoadSheddingAdmissionStatusRegistry.addLoadSheddingAdmissionComponent(this);
	}
	
	@Override
	public boolean addQuery(int queryID) {
		LoggerFactory.getLogger(this.getClass()).info("addquery");
		IPhysicalQuery physQuery = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan(superUser).getQueryById(queryID, superUser);
		ILogicalQuery logQuery = physQuery.getLogicalQuery();
		String o = logQuery.getUserParameter("LOADSHEDDINGENABLED");
		if (o != null) {
			boolean enabled = Boolean.parseBoolean(o);
			if (enabled) {
				String factor = logQuery.getUserParameter("maxSheddingFactor");
				if (factor != null) {
					int maxSheddingFactor = Integer.parseInt(factor);
					allowedQueries.put(queryID, maxSheddingFactor);
					LoggerFactory.getLogger(this.getClass()).info("maxSheddingFactor : " + maxSheddingFactor);
				} else {
					allowedQueries.put(queryID, LoadSheddingAdmissionStatusRegistry.getStandartMaxSheddingFactor());
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean removeQuery(int queryID) {
		if (allowedQueries.containsKey(queryID)) {
			if (maxSheddingQueries.contains(queryID)) {
				maxSheddingQueries.remove(Integer.valueOf(queryID));
			}
			if (activeQueries.containsKey(queryID)) {
				activeQueries.remove(queryID);
			}
			allowedQueries.remove(queryID);
			return true;
			
		}
		return false;
	}
	
	protected boolean isSheddingPossible() {
		if(allowedQueries.isEmpty()) {
			return false;
		}
		if(allowedQueries.keySet().size() == maxSheddingQueries.size()) {
			return false;
		}
		return true;
	}
}
