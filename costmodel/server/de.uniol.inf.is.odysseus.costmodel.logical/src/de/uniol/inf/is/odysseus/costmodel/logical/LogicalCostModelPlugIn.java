package de.uniol.inf.is.odysseus.costmodel.logical;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.costmodel.logical.estimate.AccessAOEstimator;
import de.uniol.inf.is.odysseus.costmodel.logical.estimate.BufferAOEstimator;
import de.uniol.inf.is.odysseus.costmodel.logical.estimate.ElementWindowAOEstimator;
import de.uniol.inf.is.odysseus.costmodel.logical.estimate.JoinAOEstimator;
import de.uniol.inf.is.odysseus.costmodel.logical.estimate.ProjectAOEstimator;
import de.uniol.inf.is.odysseus.costmodel.logical.estimate.RenameAOEstimator;
import de.uniol.inf.is.odysseus.costmodel.logical.estimate.SelectAOEstimator;
import de.uniol.inf.is.odysseus.costmodel.logical.estimate.StreamAOEstimator;
import de.uniol.inf.is.odysseus.costmodel.logical.estimate.TopAOEstimator;
import de.uniol.inf.is.odysseus.costmodel.logical.estimate.UnionAOEstimator;
import de.uniol.inf.is.odysseus.costmodel.logical.estimate.WindowAOEstimator;
import de.uniol.inf.is.odysseus.costmodel.logical.impl.OperatorEstimatorRegistry;

public class LogicalCostModelPlugIn implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		OperatorEstimatorRegistry.bindLogicalOperatorEstimator(new BufferAOEstimator());
		OperatorEstimatorRegistry.bindLogicalOperatorEstimator(new WindowAOEstimator());
		OperatorEstimatorRegistry.bindLogicalOperatorEstimator(new JoinAOEstimator());
		OperatorEstimatorRegistry.bindLogicalOperatorEstimator(new ProjectAOEstimator());
		OperatorEstimatorRegistry.bindLogicalOperatorEstimator(new AccessAOEstimator());
		OperatorEstimatorRegistry.bindLogicalOperatorEstimator(new SelectAOEstimator());
		OperatorEstimatorRegistry.bindLogicalOperatorEstimator(new UnionAOEstimator());
		OperatorEstimatorRegistry.bindLogicalOperatorEstimator(new RenameAOEstimator());
		OperatorEstimatorRegistry.bindLogicalOperatorEstimator(new TopAOEstimator());
		OperatorEstimatorRegistry.bindLogicalOperatorEstimator(new ElementWindowAOEstimator());
		OperatorEstimatorRegistry.bindLogicalOperatorEstimator(new StreamAOEstimator());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		OperatorEstimatorRegistry.unbindLogicalOperatorEstimator(new BufferAOEstimator());
		OperatorEstimatorRegistry.unbindLogicalOperatorEstimator(new WindowAOEstimator());
		OperatorEstimatorRegistry.unbindLogicalOperatorEstimator(new JoinAOEstimator());
		OperatorEstimatorRegistry.unbindLogicalOperatorEstimator(new ProjectAOEstimator());
		OperatorEstimatorRegistry.unbindLogicalOperatorEstimator(new AccessAOEstimator());
		OperatorEstimatorRegistry.unbindLogicalOperatorEstimator(new SelectAOEstimator());
		OperatorEstimatorRegistry.unbindLogicalOperatorEstimator(new UnionAOEstimator());
		OperatorEstimatorRegistry.unbindLogicalOperatorEstimator(new RenameAOEstimator());
		OperatorEstimatorRegistry.unbindLogicalOperatorEstimator(new TopAOEstimator());
		OperatorEstimatorRegistry.unbindLogicalOperatorEstimator(new ElementWindowAOEstimator());
		OperatorEstimatorRegistry.unbindLogicalOperatorEstimator(new StreamAOEstimator());
	}

}
