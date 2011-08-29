package de.uniol.inf.is.odysseus.costmodel.operator.relational;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.costmodel.operator.relational.cfg.CPURateSaver;
import de.uniol.inf.is.odysseus.costmodel.operator.relational.cfg.DataStreamRateSaver;

public class Activator implements BundleActivator {

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
