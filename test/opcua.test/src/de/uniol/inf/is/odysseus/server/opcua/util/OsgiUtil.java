package de.uniol.inf.is.odysseus.server.opcua.util;

import java.io.File;
import java.io.FileFilter;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;

public final class OsgiUtil {

	public static final FileFilter XmlFiles = new FileFilter() {
		public boolean accept(File file) {
			return file.getName().endsWith(".xml");
		}
	};

	public static Bundle find(Bundle[] bundles, String name) {
		for (Bundle bundle : bundles)
			if (bundle.getSymbolicName().equalsIgnoreCase(name))
				return bundle;
		return null;
	}

	public static ClassLoader getClassloader(BundleContext context) {
		Bundle bundle = context.getBundle();
		BundleWiring wiring = bundle.adapt(BundleWiring.class);
		return wiring.getClassLoader();
	}
}