package de.uniol.inf.is.odysseus.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Activator 
 * 
 * @author Kai Pancratz, Alexander Funk
 *
 */
public class TupleTestActivator implements BundleActivator {

	public static BundleContext context;

	@Override
	public void start(BundleContext ctx) throws Exception {
		context = ctx;
		new TupleTestComponent().startTesting(new String[]{});
	}

	@Override
	public void stop(BundleContext ctx) throws Exception {
		context = null;
	}

	public static File getBundlePath() {
		URL fileUrl = TupleTestActivator.context.getBundle().getResource("tests");
		try {
			return new File(FileLocator.toFileURL(fileUrl).getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
