package de.uniol.inf.is.odysseus.mep;

import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Enumeration;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.mep.IFunction;

public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		searchBundle(context.getBundle());

	}

	private void searchBundle(Bundle bundle) {
		Enumeration<URL> entries = bundle.findEntries("/bin/", "*.class", true);
		// collect logical operators and register parameters first
		// add logical operators afterwards, because they may need the newly
		// registered parameters
		if (entries == null) {
			entries = bundle.findEntries("/", "*.class", true);
			if (entries == null) {
				return;
			}
		}
		while (entries.hasMoreElements()) {
			URL curURL = entries.nextElement();
			Class<?> classObject = loadClass(bundle, curURL);
			if (IFunction.class.isAssignableFrom(classObject)
					&& !Modifier.isAbstract(classObject.getModifiers())) {
				try {
					MEP.registerFunction((IFunction<?>) classObject
							.newInstance());
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private Class<?> loadClass(Bundle bundle, URL curURL) {
		String file = curURL.getFile();
		int start = 1;
		String className = "";
		try {
			if (file.startsWith("/bin/")) {
				start = "/bin/".length();
			}
			// remove potential '/bin' and 'class' and change path to package
			// name
			className = file.substring(start, file.length() - 6).replace('/',
					'.');
			Class<?> classObject = bundle.loadClass(className);
			return classObject;
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub

	}

}
