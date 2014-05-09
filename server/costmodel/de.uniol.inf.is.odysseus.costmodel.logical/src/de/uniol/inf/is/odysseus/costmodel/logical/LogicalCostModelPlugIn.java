package de.uniol.inf.is.odysseus.costmodel.logical;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.costmodel.logical.estimate.BufferAOEstimator;
import de.uniol.inf.is.odysseus.costmodel.logical.impl.OperatorEstimatorRegistry;

public class LogicalCostModelPlugIn implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		OperatorEstimatorRegistry.bindLogicalOperatorEstimator(new BufferAOEstimator());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		OperatorEstimatorRegistry.unbindLogicalOperatorEstimator(new BufferAOEstimator());
	}

}
