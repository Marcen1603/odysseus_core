package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.admission.status.AdmissionStatusPlugIn;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public abstract class AbstractLoadSheddingAdmissionStatusComponent implements ILoadSheddingAdmissionStatusComponent {

	protected HashMap<Integer, Integer> allowedQueries = new HashMap<Integer, Integer>();
	
	/**
	 * Contains all queries with active load shedding.
	 */
	protected HashMap<Integer, Integer> activeQueries = new HashMap<Integer, Integer>();
	
	/**
	 * Contains all queries with maximal shedding-factor.
	 */
	protected ArrayList<Integer> maxSheddingQueries = new ArrayList<Integer>();
	
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
		return false;
	}

}
