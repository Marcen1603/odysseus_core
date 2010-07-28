package de.uniol.inf.is.odysseus.parser.pql.priority;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.logicaloperator.builder.OperatorBuilderFactory;

public class Activator implements BundleActivator {

	private static final String PRIORITY_PREDICATE = "PRIORITYPREDICATE";
	private static final String PRIORITY = "PRIORITY";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		OperatorBuilderFactory.putOperatorBuilderType(PRIORITY,
				PriorityAOBuilder.class);

		OperatorBuilderFactory.putPredicateBuilder(PRIORITY_PREDICATE,
				new PriorityPredicateBuilder());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		OperatorBuilderFactory.removeOperatorBuilderType(PRIORITY);
		OperatorBuilderFactory.removePredicateBuilder(PRIORITY_PREDICATE);
	}

}
