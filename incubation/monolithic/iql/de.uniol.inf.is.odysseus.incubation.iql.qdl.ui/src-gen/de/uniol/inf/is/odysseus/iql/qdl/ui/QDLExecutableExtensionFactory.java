/*
 * generated by Xtext
 */
package de.uniol.inf.is.odysseus.iql.qdl.ui;

import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;

import com.google.inject.Injector;

import de.uniol.inf.is.odysseus.iql.qdl.ui.internal.QDLActivator;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass. 
 */
public class QDLExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

	@Override
	protected Bundle getBundle() {
		return QDLActivator.getInstance().getBundle();
	}
	
	@Override
	protected Injector getInjector() {
		return QDLActivator.getInstance().getInjector(QDLActivator.DE_UNIOL_INF_IS_ODYSSEUS_IQL_QDL_QDL);
	}
	
}