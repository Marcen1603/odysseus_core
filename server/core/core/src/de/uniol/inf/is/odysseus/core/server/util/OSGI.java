package de.uniol.inf.is.odysseus.core.server.util;

import static com.google.common.base.Preconditions.checkNotNull;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import de.uniol.inf.is.odysseus.core.server.Activator;

public class OSGI {

	static <T> T get(Class<T> service) {
		ServiceReference<T> reference = context().getServiceReference(service);
		return checkNotNull(context().getService(reference));
	}

	private static BundleContext context() {
		return Activator.getBundleContext();
	}

}
