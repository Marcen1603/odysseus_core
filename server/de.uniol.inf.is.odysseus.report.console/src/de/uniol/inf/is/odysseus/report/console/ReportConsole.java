package de.uniol.inf.is.odysseus.report.console;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.report.IReport;
import de.uniol.inf.is.odysseus.report.IReportGenerator;

public class ReportConsole implements CommandProvider {

	private static final String NO_REPORT_TEXT = "<no report>";
	private static IReportGenerator reportGenerator;
	private static ISession currentSession;

	// called by OSGi-DS
	public void bindReportGenerator(IReportGenerator serv) {
		reportGenerator = serv;
	}

	// called by OSGi-DS
	public void unbindReportGenerator(IReportGenerator serv) {
		if (reportGenerator == serv) {
			reportGenerator = null;
		}
	}
	
	public static ISession getActiveSession() {
		if( currentSession == null || !currentSession.isValid()) {
			currentSession = SessionManagement.instance.loginSuperUser(null, UserManagementProvider.instance.getDefaultTenant().getName());
		}
		return currentSession;
	}

	@Override
	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("report         - Prints a current status report about odysseus, bundles, services, etc.\n");
		
		return sb.toString();
	}

	public void _report( CommandInterpreter ci ) {
		IReport report = reportGenerator.generateReport(getActiveSession());
		
		for( String reportTitle : report.getTitles() ) {
			ci.println(getLine());
			ci.println("## " + reportTitle);
			ci.println(getLine());
			ci.println();
			Optional<String> optReportText = report.getReportText(reportTitle);
			ci.println(optReportText.isPresent() ? optReportText.get() : NO_REPORT_TEXT);
			ci.println();
		}
	}
	
	private static String getLine() {
		return "##########################################################################################";
	}
}
