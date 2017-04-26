package de.uniol.inf.is.odysseus.server.xml;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.server.xml.predicate.XMLStreamObjectPredicateBuilder;

public class Activator implements BundleActivator {

	private static final String XMLSTREAMOBJECT_PREDICATE = "XMLSTREAMOBJECTPREDICATE";

	@Override
	public void start(BundleContext context) throws Exception {
		OperatorBuilderFactory.putPredicateBuilder(XMLSTREAMOBJECT_PREDICATE, new XMLStreamObjectPredicateBuilder<>());
		OperatorBuilderFactory.putPredicateBuilder(XMLStreamObject.class.getName(), new XMLStreamObjectPredicateBuilder<XMLStreamObject<?>>());
			}

	@Override
	public void stop(BundleContext context) throws Exception {
		OperatorBuilderFactory.removePredicateBuilder(XMLSTREAMOBJECT_PREDICATE);
		OperatorBuilderFactory.removePredicateBuilder(XMLStreamObject.class.getName());
	}
}
