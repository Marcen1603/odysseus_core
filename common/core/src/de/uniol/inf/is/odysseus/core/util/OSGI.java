package de.uniol.inf.is.odysseus.core.util;

import static com.google.common.base.Preconditions.checkNotNull;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import de.uniol.inf.is.odysseus.core.Activator;

public class OSGI {

	public static <T> T get(Class<T> service) {
		ServiceReference<T> reference = context().getServiceReference(service);
		return checkNotNull(context().getService(reference));
	}

	private static BundleContext context() {
		return Activator.getBundleContext();
	}

}
