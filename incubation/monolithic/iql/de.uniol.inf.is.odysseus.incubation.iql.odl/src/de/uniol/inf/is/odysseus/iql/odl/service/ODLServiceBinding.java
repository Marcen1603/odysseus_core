package de.uniol.inf.is.odysseus.iql.odl.service;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilderFactory;
import de.uniol.inf.is.odysseus.iql.basic.service.IQLServiceBinding;

public class ODLServiceBinding extends IQLServiceBinding {

	private static ODLServiceBinding instance = new ODLServiceBinding();
	
	public static ODLServiceBinding getInstance() {
		return instance;
	}

	public static void bindODLService(IODLService service) {
		getInstance().onIQLServiceAdded(service);
	}
	
	public static void unbindODLService(IODLService service) {
		getInstance().onIQLServiceRemoved(service);
	}
	
	public static void bindOperatorBuilderFactory(IOperatorBuilderFactory factory) {
		//ODLExecutor.loadPersistentOperators();
	}
	
	public static void unbindOperatorBuilderFactory(IOperatorBuilderFactory factory) {

	}
}
