/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;

/**
 * @author MarkMilster
 *
 */
public class MoreFileUtils {
	
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
