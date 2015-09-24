package de.uniol.inf.is.odysseus.iql.odl;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.iql.basic.typing.utils.BasicIQLTypeUtils;
import de.uniol.inf.is.odysseus.iql.odl.executor.ODLExecutor;


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
		cleanUpOperatorsDir();
	}
	
	private void cleanUpOperatorsDir() {
		try {
			File operatorsDir = new File(BasicIQLTypeUtils.getIQLOutputPath()+File.separator+ODLExecutor.OPERATORS_DIR);
			if (operatorsDir.exists()){
				for (File file : operatorsDir.listFiles()) {
					if (file.isDirectory() && !file.getName().startsWith("persistent")) {
						FileUtils.deleteDirectory(file);
					}
				}
			}
		} catch (Exception e) {
		}
	}


}
