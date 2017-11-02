/*
 * generated by Xtext 2.12.0
 */
package de.uniol.inf.is.odysseus.eca.ui;

import com.google.inject.Injector;
import de.uniol.inf.is.odysseus.eca.ui.internal.EcaActivator;
import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass. 
 */
public class ECAExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

	@Override
	protected Bundle getBundle() {
		return EcaActivator.getInstance().getBundle();
	}
	
	@Override
	protected Injector getInjector() {
		return EcaActivator.getInstance().getInjector(EcaActivator.DE_UNIOL_INF_IS_ODYSSEUS_ECA_ECA);
	}
	
}
