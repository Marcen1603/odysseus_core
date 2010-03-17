package de.uniol.inf.is.odysseus.cep.epa;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.cep.epa.symboltable.RelationalSymbolTableOperationFactory;
import de.uniol.inf.is.odysseus.cep.metamodel.symboltable.SymbolTableOperationFactory;


public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext arg0) throws Exception {
		SymbolTableOperationFactory.setSymbolTableOperationFactory(new RelationalSymbolTableOperationFactory());
		
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		SymbolTableOperationFactory.unsetSymbolTableOperationFactory();		
	}

}
