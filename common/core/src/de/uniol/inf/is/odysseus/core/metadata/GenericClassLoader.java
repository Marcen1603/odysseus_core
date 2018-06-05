package de.uniol.inf.is.odysseus.core.metadata;

import java.util.ArrayList;
import java.util.List;

class GenericClassLoader extends ClassLoader {
	private List<ClassLoader> classLoader = new ArrayList<>();

	public GenericClassLoader(Object[] className) {
		for (Object m : className) {
			classLoader.add(m.getClass().getClassLoader());
		}
	}

	public GenericClassLoader(@SuppressWarnings("rawtypes") List className) {
		for (Object m : className) {
			classLoader.add(m.getClass().getClassLoader());
		}
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		try {
			return super.loadClass(name);
		} catch (ClassNotFoundException e) {
		}

		for (ClassLoader c : classLoader) {
			try {
				return c.loadClass(name);
			} catch (ClassNotFoundException e) {
			}
		}
		return super.loadClass(name);
	}
}
