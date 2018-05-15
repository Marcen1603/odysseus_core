package de.uniol.inf.is.odysseus.server.xml;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {


	@Override
	public void start(BundleContext context) throws Exception {
//		OperatorBuilderFactory.putExpressionBuilder(new XMLStreamObjectPredicateBuilder<>());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		//OperatorBuilderFactory.removeExpressionBuilder(new XMLStreamObjectPredicateBuilder<>());
	}
}
