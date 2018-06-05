package de.uniol.inf.is.odysseus.report.general;

import java.util.Arrays;

import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.report.IReportProvider;

public class ServicesReportProvider implements IReportProvider {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public String getTitle() {
		return "Services information";
	}

	@Override
	public String getReport(ISession session) {
		StringBuilder report = new StringBuilder();
		Bundle[] bundles = GeneralReportPlugIn.getBundleContext().getBundles();
		for (Bundle bundle : bundles) {
			ServiceReference<?>[] services = bundle.getRegisteredServices();
			if (services != null) {
				for (ServiceReference<?> service : services) {
					Object serviceId = service.getProperty(Constants.SERVICE_ID);
					Object[] clazzes = (Object[]) service.getProperty(Constants.OBJECTCLASS);

					report.append("* ").append(serviceId).append(" ").append(Arrays.toString(clazzes)).append("\n");
					for (String propertyKey : service.getPropertyKeys()) {
						if (!propertyKey.equals(Constants.OBJECTCLASS)) {
							report.append("\t+ ").append(propertyKey).append(": ").append(service.getProperty(propertyKey)).append("\n");
						}
					}
					if (service.getUsingBundles() != null) {
						report.append("\tUsed by:\n");
						for (Bundle b : service.getUsingBundles()) {
							report.append("\t\t- ").append(b.toString()).append("\n");
						}
					}

				}
			}
		}
		return report.toString();
	}

}
