package de.uniol.inf.is.odysseus.report.console;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.report.IReportGenerator;

public class ReportConsole implements CommandProvider {

	private static IReportGenerator reportGenerator;
	private static ISession currentSession;

	// called by OSGi-DS
	public static void bindReportGenerator(IReportGenerator serv) {
		reportGenerator = serv;
	}

	// called by OSGi-DS
	public static void unbindReportGenerator(IReportGenerator serv) {
		if (reportGenerator == serv) {
			reportGenerator = null;
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
		
		sb.append("report         - Prints a current status report about odysseus, bundles, services, etc.");
		
		return sb.toString();
	}

	public void _report( CommandInterpreter ci ) {
		System.out.println(reportGenerator.generateReport(getActiveSession()));
	}
}
