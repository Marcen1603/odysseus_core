package de.uniol.inf.is.odysseus.costmodel.operator.datasrc;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class OperatorCostModelDataSourcePlugIn implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		DataSourceManager.getInstance().load();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		DataSourceManager.getInstance().save();
	}

}
