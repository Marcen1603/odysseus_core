package de.uniol.inf.is.odysseus.costmodel.operator.util;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class OperatorCostModelUtilPlugIn implements BundleActivator {

	@Override
	public void start(BundleContext context) {
//		try {
		DataStreamRateSaver.getInstance().load();
		CPURateSaver.getInstance().load();
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
	}

	@Override
	public void stop(BundleContext context)  {
		DataStreamRateSaver.getInstance().save();
		CPURateSaver.getInstance().save();
	}

}
