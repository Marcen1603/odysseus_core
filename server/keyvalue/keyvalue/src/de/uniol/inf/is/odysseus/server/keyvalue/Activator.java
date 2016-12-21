package de.uniol.inf.is.odysseus.server.keyvalue;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.server.keyvalue.predicate.KeyValuePredicateBuilder;

public class Activator implements BundleActivator {

	private static final String KEYVALUE_PREDICATE = "KEYVALUEPREDICATE";

	@Override
	public void start(BundleContext context) throws Exception {
		OperatorBuilderFactory.putPredicateBuilder(KEYVALUE_PREDICATE, new KeyValuePredicateBuilder<>());
		OperatorBuilderFactory.putPredicateBuilder(KeyValueObject.class.getName(), new KeyValuePredicateBuilder<KeyValueObject<?>>());
			}

	@Override
	public void stop(BundleContext context) throws Exception {
		OperatorBuilderFactory.removePredicateBuilder(KEYVALUE_PREDICATE);
		OperatorBuilderFactory.removePredicateBuilder(KeyValueObject.class.getName());
	}
}
