package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryCommunicator;

public class RecoveryHelper {
	
	/**
	 * Installs and executes a query from PQL.
	 * 
	 * @param pql
	 *            PQL to execute.
	 */
	public static Collection<Integer> installAndRunQueryPartFromPql(String pql) {
		
		IServerExecutor executor = RecoveryCommunicator.getExecutor();
		ISession session = RecoveryCommunicator.getActiveSession();
		
		Collection<Integer> installedQueries = executor.addQuery(pql, "PQL",
				session, "Standard", Context.empty());
		for (int query : installedQueries) {
			executor.startQuery(query, session);
		}
		return installedQueries;

	}

}
