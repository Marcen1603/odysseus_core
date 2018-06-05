package de.uniol.inf.is.odysseus.iql.qdl.ui.service;

import de.uniol.inf.is.odysseus.iql.basic.ui.service.IQLUiServiceBinding;


public class QDLUiServiceBinding extends IQLUiServiceBinding {	
	
	private static QDLUiServiceBinding instance = new QDLUiServiceBinding();
	
	public static QDLUiServiceBinding getInstance() {
		return instance;
	}

	public static void bindODLUiService(IQDLUiService service) {
		getInstance().onIQLUiServiceAdded(service);
	}
	
	public static void unbindODLUiService(IQDLUiService service) {
		getInstance().onIQLUiServiceRemoved(service);
	}
}
