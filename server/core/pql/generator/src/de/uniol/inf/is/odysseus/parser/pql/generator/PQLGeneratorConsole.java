package de.uniol.inf.is.odysseus.parser.pql.generator;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class PQLGeneratorConsole implements CommandProvider {

	private static IPQLGenerator pqlGenerator;
	private static IExecutor executor;
	private static ISession currentSession;

	// called by OSGi-DS
	public static void bindPQLGenerator(IPQLGenerator serv) {
		pqlGenerator = serv;
	}

	// called by OSGi-DS
	public static void unbindPQLGenerator(IPQLGenerator serv) {
		if (pqlGenerator == serv) {
			pqlGenerator = null;
		}
	}
	
	// called by OSGi-DS
	public static void bindExecutor(IExecutor serv) {
		executor = serv;
	}

	// called by OSGi-DS
	public static void unbindExecutor(IExecutor serv) {
		if (executor == serv) {
			executor = null;
		}
	}
	
	public static ISession getActiveSession() {
		if( currentSession == null || !currentSession.isValid()) {
			currentSession = UserManagementProvider.getSessionmanagement().loginSuperUser(null, UserManagementProvider.getDefaultTenant().getName());
		}
		return currentSession;
	}
	
	@Override
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("---PQLGenerator commands---\n");
		sb.append("generatePQL <queryID>		- Generates and prints the PQL-Text of the given query.\n");
		
		return sb.toString();
	}
	
	public void _generatePQL(CommandInterpreter ci) {
		String queryIDString = ci.nextArgument();
		if (Strings.isNullOrEmpty(queryIDString)) {
			System.out.println("usage: generatePQL <queryID>");
			return;
		}

		int queryID = 0;
		try {
			queryID = Integer.valueOf(queryIDString);
		} catch (Throwable t) {
			System.out.println("usage: generatePQL <queryID>");
		}

		if( !executor.getLogicalQueryIds(getActiveSession()).contains(queryID) ) {
			System.out.println("No query with queryid " + queryID + " found");
			return;
		}
		
		ILogicalQuery logicalQuery = executor.getLogicalQueryById(queryID, getActiveSession());
		String pqlStatement = pqlGenerator.generatePQLStatement(logicalQuery.getLogicalPlan());
		System.out.println(pqlStatement);
	}

}
