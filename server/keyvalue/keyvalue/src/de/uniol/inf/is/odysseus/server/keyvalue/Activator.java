package de.uniol.inf.is.odysseus.server.keyvalue;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.server.keyvalue.expression.KeyValuePredicateBuilder;

public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		OperatorBuilderFactory.addExpressionBuilder(new KeyValuePredicateBuilder<>());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		OperatorBuilderFactory.removeExpressionBuilder(new KeyValuePredicateBuilder<>());
	}
}
