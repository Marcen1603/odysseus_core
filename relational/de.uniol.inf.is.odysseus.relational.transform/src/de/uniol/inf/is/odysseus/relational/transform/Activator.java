package de.uniol.inf.is.odysseus.relational.transform;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.transform.engine.TransformationInventory;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		TransformationInventory.getInstance().addRule(new TAccessAORelationalInputRule(), TransformRuleFlowGroup.ACCESS);
		TransformationInventory.getInstance().addRule(new TAccessAOAtomicDataRule(), TransformRuleFlowGroup.ACCESS);
		TransformationInventory.getInstance().addRule(new TAccessAORelationalByteBufferRule(), TransformRuleFlowGroup.ACCESS);
		TransformationInventory.getInstance().addRule(new TFixedSetAccessAORule(), TransformRuleFlowGroup.ACCESS);
		TransformationInventory.getInstance().addRule(new TAggregateAORule(), TransformRuleFlowGroup.METAOBJECTS);
		TransformationInventory.getInstance().addRule(new TMapAORule(), TransformRuleFlowGroup.TRANSFORMATION);
		TransformationInventory.getInstance().addRule(new TProjectAORule(), TransformRuleFlowGroup.TRANSFORMATION);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
