package de.uniol.inf.is.odysseus.costmodel.operator.util;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class OperatorCostModelUtilPlugIn implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		DataStreamRateSaver.getInstance().load();
		CPURateSaver.getInstance().load();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		DataStreamRateSaver.getInstance().save();
		CPURateSaver.getInstance().save();
	}

}
