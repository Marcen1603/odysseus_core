package de.uniol.inf.is.odysseus.server.keyvalue;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.server.keyvalue.expression.KeyValuePredicateBuilder;

public class Activator implements BundleActivator {

	private static final String KEYVALUE_PREDICATE = "KEYVALUEPREDICATE";

	@Override
	public void start(BundleContext context) throws Exception {
		OperatorBuilderFactory.putExpressionBuilder(KEYVALUE_PREDICATE, new KeyValuePredicateBuilder<>());
		OperatorBuilderFactory.putExpressionBuilder(KeyValueObject.class.getName(), new KeyValuePredicateBuilder<IMetaAttribute>());
			}

	@Override
	public void stop(BundleContext context) throws Exception {
		OperatorBuilderFactory.removeExpressionBuilder(KEYVALUE_PREDICATE);
		OperatorBuilderFactory.removeExpressionBuilder(KeyValueObject.class.getName());
	}
}
