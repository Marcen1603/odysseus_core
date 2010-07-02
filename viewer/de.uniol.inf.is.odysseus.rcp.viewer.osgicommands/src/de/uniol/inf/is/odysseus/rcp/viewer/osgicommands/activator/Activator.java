package de.uniol.inf.is.odysseus.rcp.viewer.osgicommands.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.AbstractQueryBuildParameter;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.transformation.helper.relational.RelationalTransformationHelper;


public class Activator implements BundleActivator {

	private static IAdvancedExecutor executor = null;
	@SuppressWarnings("unchecked")
	private static ParameterTransformationConfiguration trafoConfigParam = new ParameterTransformationConfiguration(
			new TransformationConfiguration(new RelationalTransformationHelper(), "relational", ITimeInterval.class));

	
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

	public static IAdvancedExecutor getExecutor() {
		return executor;
	}
	
	public void bindExecutor( IAdvancedExecutor ex ) {
		executor = ex;
	}
	
	public void unbindExecutor( IAdvancedExecutor ex ) {
		executor = null;
	}

	public static AbstractQueryBuildParameter<?> getTrafoConfigParam() {
		return trafoConfigParam;
	}
}
