package de.uniol.inf.is.odysseus.core.server.store;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

import de.uniol.inf.is.odysseus.core.Activator;
import de.uniol.inf.is.odysseus.core.util.BundleClassLoading;

public class OsgiObjectInputStream extends ObjectInputStream {

	public OsgiObjectInputStream(FileInputStream fileInputStream)
			throws IOException {
		super(fileInputStream);
	}

	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException,
			ClassNotFoundException {
		try {
			return BundleClassLoading.findClass(desc.getName(), Activator.getBundleContext().getBundle());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.resolveClass(desc);
	}

}
