package de.uniol.inf.is.odysseus.parser.pql.relational;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.logicaloperator.builder.OperatorBuilderFactory;


public class Activator implements BundleActivator {

	private static final String RELATIONAL_PREDICATE = "RELATIONALPREDICATE";

	@Override
	public void start(BundleContext context) throws Exception {
		OperatorBuilderFactory.putPredicateBuilder(RELATIONAL_PREDICATE, new RelationalPredicateBuilder());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		OperatorBuilderFactory.removePredicateBuilder(RELATIONAL_PREDICATE);
	}

}
