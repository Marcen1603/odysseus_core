package de.uniol.inf.is.odysseus.iql.qdl;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;
import de.uniol.inf.is.odysseus.iql.qdl.executor.QDLExecutor;

public class Activator implements BundleActivator {

	private static BundleContext context;

	public static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		cleanUpQueriesDir();
	}
	
	private void cleanUpQueriesDir() {
		try {
			FileUtils.deleteDirectory(new File(BasicIQLTypeUtils.getIQLOutputPath()+File.separator+QDLExecutor.QUERIES_DIR));
		} catch (Exception e) {
		}
	}

}
