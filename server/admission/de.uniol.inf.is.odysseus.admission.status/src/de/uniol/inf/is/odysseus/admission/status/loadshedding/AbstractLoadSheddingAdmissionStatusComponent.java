package de.uniol.inf.is.odysseus.admission.status.loadshedding;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.admission.status.AdmissionStatusPlugIn;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public abstract class AbstractLoadSheddingAdmissionStatusComponent implements ILoadSheddingAdmissionStatusComponent {

	/**
	 * The ISession superUser is used to get access to the execution plan of the queries.
	 */
	static private final ISession superUser = UserManagementProvider.getUsermanagement(true).getSessionManagement()
			.loginSuperUser(null);

	/**
	 * Contains all allowed queries with their maximal shedding factor.
	 */
	protected volatile Map<Integer, Integer> allowedQueries = new HashMap<Integer, Integer>();

	/**
	 * Contains all queries with active load shedding and their actual shedding
	 * factor.
	 */
	protected volatile Map<Integer, Integer> activeQueries = new HashMap<Integer, Integer>();

	/**
	 * Contains all queries with maximal shedding-factor.
	 */
	protected volatile List<Integer> maxSheddingQueries = new ArrayList<Integer>();

	/**
	 * The Default Constructor adds the object of this class to the LoadSheddingAdmissionStatusRegistry.
	 */
	public AbstractLoadSheddingAdmissionStatusComponent() {
		LoadSheddingAdmissionStatusRegistry.addLoadSheddingAdmissionComponent(this);
	}

	@Override
	public boolean addQuery(int queryID) {
		IPhysicalQuery physQuery = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan(superUser)
				.getQueryById(queryID, superUser);
		ILogicalQuery logQuery = physQuery.getLogicalQuery();
		String o = logQuery.getUserParameter("LOADSHEDDINGENABLED");
		if (o != null) {
			boolean enabled = Boolean.parseBoolean(o);
			if (enabled) {
				String factor = logQuery.getUserParameter("maxSheddingFactor");
				if (factor != null) {
					int maxSheddingFactor = Integer.parseInt(factor);
					if (maxSheddingFactor > 0) {
						allowedQueries.put(queryID, maxSheddingFactor);
						return true;
					} else {
						return false;
					}
				} else {
					allowedQueries.put(queryID, LoadSheddingAdmissionStatusRegistry.getStandartMaxSheddingFactor());
					return true;
				}
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
		if (allowedQueries.isEmpty()) {
			return false;
		}
		if (allowedQueries.keySet().size() <= maxSheddingQueries.size()) {
			return false;
		}
		return true;
	}

	protected void setSheddingFactor(int queryID, int factor) {
		if (AdmissionStatusPlugIn.getServerExecutor().getQueryState(queryID, superUser) != QueryState.INACTIVE) {
			AdmissionStatusPlugIn.getServerExecutor().partialQuery(queryID, factor, superUser);
			String text = queryID + " has shedding factor : " + factor;
			LoggerFactory.getLogger(this.getClass()).info(text);
			try {
				Files.write(Paths.get("C:/Users/Jannes/Desktop/Uni/6.Semester/Bachelor_Arbeit/Evaluation/Ergebnis/sheddingfactor.txt"),
						text.getBytes(), StandardOpenOption.APPEND);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	protected IPhysicalQuery getQueryByID(int queryID) {
		return AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan(superUser).getQueryById(queryID, superUser);
	}
}
