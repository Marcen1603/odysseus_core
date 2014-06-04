package de.uniol.inf.is.odysseus.keyvalue;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.NestedKeyValueObject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.keyvalue.predicate.KeyValuePredicateBuilder;

public class Activator implements BundleActivator {

	private static final String KEYVALUE_PREDICATE = "KEYVALUEPREDICATE";

	@Override
	public void start(BundleContext context) throws Exception {
		OperatorBuilderFactory.putPredicateBuilder(KEYVALUE_PREDICATE, new KeyValuePredicateBuilder<>());
		OperatorBuilderFactory.putPredicateBuilder(KeyValueObject.class.getName(), new KeyValuePredicateBuilder<KeyValueObject<?>>());
		OperatorBuilderFactory.putPredicateBuilder(NestedKeyValueObject.class.getName(), new KeyValuePredicateBuilder<NestedKeyValueObject<?>>());
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		OperatorBuilderFactory.removePredicateBuilder(KEYVALUE_PREDICATE);
		OperatorBuilderFactory.removePredicateBuilder(KeyValueObject.class.getName());
		OperatorBuilderFactory.removePredicateBuilder(NestedKeyValueObject.class.getName());
	}
}
