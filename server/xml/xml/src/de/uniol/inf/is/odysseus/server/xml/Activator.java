package de.uniol.inf.is.odysseus.server.xml;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.server.xml.predicate.XMLStreamObjectPredicateBuilder;

public class Activator implements BundleActivator {


	@Override
	public void start(BundleContext context) throws Exception {
		OperatorBuilderFactory.putExpressionBuilder(new XMLStreamObjectPredicateBuilder<>());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		OperatorBuilderFactory.removeExpressionBuilder(new XMLStreamObjectPredicateBuilder<>());
	}
}
