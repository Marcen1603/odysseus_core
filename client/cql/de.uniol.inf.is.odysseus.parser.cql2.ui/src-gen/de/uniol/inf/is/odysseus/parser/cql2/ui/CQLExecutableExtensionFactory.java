/*
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.cql2.ui;

import com.google.inject.Injector;
import de.uniol.inf.is.odysseus.parser.cql2.ui.internal.Cql2Activator;
import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass. 
 */
public class CQLExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

	@Override
	protected Bundle getBundle() {
		return Cql2Activator.getInstance().getBundle();
	}
	
	@Override
	protected Injector getInjector() {
		return Cql2Activator.getInstance().getInjector(Cql2Activator.DE_UNIOL_INF_IS_ODYSSEUS_PARSER_CQL2_CQL);
	}
	
}
