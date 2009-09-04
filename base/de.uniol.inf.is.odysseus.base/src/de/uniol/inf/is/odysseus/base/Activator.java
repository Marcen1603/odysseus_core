package de.uniol.inf.is.odysseus.base;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.function.DolToEur;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.function.Now;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.function.ToNumber;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.function.ToString;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

public class Activator implements BundleActivator {

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		// Add default Functions
		SDFExpression.addFunction(new DolToEur());
		SDFExpression.addFunction(new Now());
		SDFExpression.addFunction(new ToNumber());
		SDFExpression.addFunction(new ToString());
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	}

}
