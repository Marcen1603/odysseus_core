package de.uniol.inf.is.odysseus.costmodel.physical;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.costmodel.physical.estimate.MetadataCreationPOEstimator;
import de.uniol.inf.is.odysseus.costmodel.physical.estimate.MetadataUpdatePOEstimator;
import de.uniol.inf.is.odysseus.costmodel.physical.estimate.ReceiverPOEstimator;
import de.uniol.inf.is.odysseus.costmodel.physical.estimate.SelectPOEstimator;
import de.uniol.inf.is.odysseus.costmodel.physical.estimate.UnionPOEstimator;
import de.uniol.inf.is.odysseus.costmodel.physical.impl.OperatorEstimatorRegistry;

public class PhysicalCostModelPlugIn implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		OperatorEstimatorRegistry.bindPhysicalOperatorEstimator(new SelectPOEstimator());
		OperatorEstimatorRegistry.bindPhysicalOperatorEstimator(new MetadataCreationPOEstimator());
		OperatorEstimatorRegistry.bindPhysicalOperatorEstimator(new MetadataUpdatePOEstimator());
		OperatorEstimatorRegistry.bindPhysicalOperatorEstimator(new ReceiverPOEstimator());
		OperatorEstimatorRegistry.bindPhysicalOperatorEstimator(new UnionPOEstimator());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		OperatorEstimatorRegistry.unbindPhysicalOperatorEstimator(new SelectPOEstimator());
		OperatorEstimatorRegistry.unbindPhysicalOperatorEstimator(new MetadataCreationPOEstimator());
		OperatorEstimatorRegistry.unbindPhysicalOperatorEstimator(new MetadataUpdatePOEstimator());
		OperatorEstimatorRegistry.unbindPhysicalOperatorEstimator(new ReceiverPOEstimator());
		OperatorEstimatorRegistry.unbindPhysicalOperatorEstimator(new UnionPOEstimator());
	}

}
