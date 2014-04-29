package de.uniol.inf.is.odysseus.costmodel.physical;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.costmodel.ICostModelKnowledge;

public class PhysicalCostModelActivator implements BundleActivator {

	private static ICostModelKnowledge knowledge;

	// called by OSGi-DS
	public static void bindCostModelKnowledge(ICostModelKnowledge serv) {
		knowledge = serv;
	}

	// called by OSGi-DS
	public static void unbindCostModelKnowledge(ICostModelKnowledge serv) {
		if (knowledge == serv) {
			knowledge = null;
		}
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

	public static ICostModelKnowledge getCostModelKnowledge() {
		return knowledge;
	}
}
