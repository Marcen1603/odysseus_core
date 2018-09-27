package de.uniol.inf.is.odysseus.report.general;

import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.report.IReportProvider;

public class BundleReportProvider implements IReportProvider {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public String getTitle() {
		return "Bundle information";
	}

	@Override
	public String getReport(ISession session) {
		StringBuilder sb = new StringBuilder();
		Bundle[] bundles = GeneralReportPlugIn.getBundleContext().getBundles();
		
		sb.append("| Id").append("\t| ").append("State").append("\t| ").append("Name").append("\t| ").append("Bundle").append(" |\n");

		for (final Bundle bundle : bundles) {
			final int state = bundle.getState();
			String stateString = "UNKNOWN";
			if (state == Bundle.ACTIVE) {
				stateString = "ACTIVE";
			} else if (state == Bundle.INSTALLED) {
				stateString = "INSTALLED";
			} else if (state == Bundle.RESOLVED) {
				stateString = "RESOLVED";
			} else if (state == Bundle.STARTING) {
				stateString = "STARTING";
			} else if (state == Bundle.STOPPING) {
				stateString = "STOPPING";
			} else if (state == Bundle.UNINSTALLED) {
				stateString = "UNINSTALLED";
			}
			sb.append("| ").append(bundle.getBundleId()).append("\t| ").append(stateString).append("\t| ").append(bundle.getSymbolicName()).append("\t| ").append(bundle.toString()).append(" |\n");
		}
		return sb.toString();
	}

}
