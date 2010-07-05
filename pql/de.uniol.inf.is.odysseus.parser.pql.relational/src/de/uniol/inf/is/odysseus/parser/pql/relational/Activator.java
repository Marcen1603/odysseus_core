package de.uniol.inf.is.odysseus.parser.pql.relational;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.parser.pql.PQLParser;


public class Activator implements BundleActivator {


	private static final String RELATIONAL_PREDICATE = "RELATIONALPREDICATE";

	@Override
	public void start(BundleContext context) throws Exception {
		PQLParser.addPredicateBuilder(RELATIONAL_PREDICATE, new RelationalPredicateBuilder());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		PQLParser.removeOperatorBuilder(RELATIONAL_PREDICATE);
	}

}
