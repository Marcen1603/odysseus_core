package de.uniol.inf.is.odysseus.core.util;

import java.util.Objects;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import de.uniol.inf.is.odysseus.core.Activator;

public class OSGI {

	public static <T> T get(Class<T> service) {
		ServiceReference<T> reference = context().getServiceReference(service);
		return Objects.requireNonNull(context().getService(reference));
	}

	private static BundleContext context() {
		return Activator.getBundleContext();
	}

}
