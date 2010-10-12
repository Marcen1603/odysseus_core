package de.uniol.inf.is.odysseus.rcp.viewer.osgicommands.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;


public class Activator implements BundleActivator {

	private static IExecutor executor = null;
//	@SuppressWarnings("unchecked")
//	private static ParameterTransformationConfiguration trafoConfigParam = new ParameterTransformationConfiguration(
//			new TransformationConfiguration(new RelationalTransformationHelper(), "relational", ITimeInterval.class));

	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(final BundleContext context) throws Exception {
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	}

	public static IExecutor getExecutor() {
		return executor;
	}
	
	public void bindExecutor( IExecutor ex ) {
		executor = ex;
	}
	
	public void unbindExecutor( IExecutor ex ) {
		executor = null;
	}

//	public static AbstractQueryBuildSetting<?> getTrafoConfigParam() {
//		return trafoConfigParam;
//	}
}
