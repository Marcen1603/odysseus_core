package de.uniol.inf.is.odysseus.report.registry;

import de.uniol.inf.is.odysseus.report.IReportProvider;
import de.uniol.inf.is.odysseus.report.activator.ReportPlugIn;

public class ReportProviderBinder {

	// called by OSGi-DS
	public static void bindReportProvider(IReportProvider serv) {
		ReportPlugIn.getReportProviderRegistry().addReportProvider(serv);
	}

	// called by OSGi-DS
	public static void unbindReportProvider(IReportProvider serv) {
		ReportPlugIn.getReportProviderRegistry().removeReportProvider(serv);
	}
}
