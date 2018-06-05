/*
 * generated by Xtext 2.12.0
 */
package de.uniol.inf.is.odysseus.iql.qdl;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.uniol.inf.is.odysseus.iql.basic.BasicIQLStandaloneSetup;
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLPackage;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.ISetup;
import org.eclipse.xtext.resource.IResourceFactory;
import org.eclipse.xtext.resource.IResourceServiceProvider;

@SuppressWarnings("all")
public class QDLStandaloneSetupGenerated implements ISetup {

	@Override
	public Injector createInjectorAndDoEMFRegistration() {
		BasicIQLStandaloneSetup.doSetup();

		Injector injector = createInjector();
		register(injector);
		return injector;
	}
	
	public Injector createInjector() {
		return Guice.createInjector(new QDLRuntimeModule());
	}
	
	public void register(Injector injector) {
		if (!EPackage.Registry.INSTANCE.containsKey("http://www.uniol.de/inf/is/odysseus/iql/qdl/QDL")) {
			EPackage.Registry.INSTANCE.put("http://www.uniol.de/inf/is/odysseus/iql/qdl/QDL", QDLPackage.eINSTANCE);
		}
		IResourceFactory resourceFactory = injector.getInstance(IResourceFactory.class);
		IResourceServiceProvider serviceProvider = injector.getInstance(IResourceServiceProvider.class);
		
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("qdl", resourceFactory);
		IResourceServiceProvider.Registry.INSTANCE.getExtensionToFactoryMap().put("qdl", serviceProvider);
		if (!EPackage.Registry.INSTANCE.containsKey("http://www.uniol.de/inf/is/odysseus/iql/qdl/QDL")) {
			EPackage.Registry.INSTANCE.put("http://www.uniol.de/inf/is/odysseus/iql/qdl/QDL", QDLPackage.eINSTANCE);
		}
	}
}
