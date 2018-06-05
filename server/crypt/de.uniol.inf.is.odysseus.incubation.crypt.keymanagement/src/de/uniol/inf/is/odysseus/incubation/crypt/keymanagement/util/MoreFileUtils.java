package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;

/**
 * Some util methods to handle files in Java.
 * 
 * @author MarkMilster
 *
 */
public class MoreFileUtils {

	/**
	 * Returns the absolute filesystem path of a file in a OSGi-Bundle.
	 * 
	 * @param bundle
	 *            Bundle, which contains the file
	 * @param path
	 *            Path of the file inside the bundle
	 * @return Absolute path of the file
	 */
	public static String getAbsolutePath(Bundle bundle, String path) {
		try {
			URL url1 = bundle.getEntry(path);
			URL url = FileLocator.toFileURL(url1);
			File file = new File(url.getPath());
			return file.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
