package de.uniol.inf.is.drools;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.dynamicjava.osgi.classloading_utils.OsgiEnvironmentClassLoader;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class OSGiDroolsClassLoader extends OsgiEnvironmentClassLoader {

	public OSGiDroolsClassLoader(BundleContext bundleContext,
			ClassLoader parent, Bundle... prioritizedBundles) {
		super(bundleContext, parent, prioritizedBundles);
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return super.loadClass(name);
	}
	@Override
	public InputStream getResourceAsStream(String name) {
		try {
			URL resource = this.getBundleContext().getBundle().getResource(name);
			if (resource == null) {
				return null;
			}
			return resource.openStream();
		} catch (IOException e) {
			return null;
		}
	}
}
