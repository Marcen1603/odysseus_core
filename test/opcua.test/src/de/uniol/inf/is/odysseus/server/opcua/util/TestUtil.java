package de.uniol.inf.is.odysseus.server.opcua.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.util.Properties;

import org.apache.commons.io.input.ClassLoaderObjectInputStream;

public final class TestUtil {

	public static Object loadAsJavaObj(String path, ClassLoader loader) throws IOException, ClassNotFoundException {
		return loadAsJavaObj(new File(path), loader);
	}

	private static Object loadAsJavaObj(File file, ClassLoader loader) throws IOException, ClassNotFoundException {
		try (ObjectInput input = new ClassLoaderObjectInputStream(loader, new FileInputStream(file))) {
			return input.readObject();
		}
	}

	public static Properties loadProps(String fileName) {
		try {
			Properties props = new Properties();
			props.load(new FileInputStream(fileName));
			return props;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}