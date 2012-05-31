package de.uniol.inf.is.odysseus.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Activator 
 * 
 * @author Kai Pancratz, Alexander Funk
 *
 */
public class TupleTestActivator implements BundleActivator {

	private static final Logger LOG = LoggerFactory.getLogger(TupleTestActivator.class);

	
	public static BundleContext context;
	public static String bundlePath;
	
	@Override
	public void start(BundleContext ctx) throws Exception {
		context = ctx;
		LOG.info("Start Tuple Test, Testfile path: " + getBundlePath());
		TupleTestActivator.bundlePath = getBundlePath().getAbsolutePath();
//		new TupleTestComponent().startTesting(new String[]{});
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
			e.printStackTrace();
		}
		return null;
	}
	
}
