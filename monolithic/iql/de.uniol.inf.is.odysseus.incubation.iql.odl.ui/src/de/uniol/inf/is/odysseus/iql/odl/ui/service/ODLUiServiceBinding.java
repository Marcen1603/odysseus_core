package de.uniol.inf.is.odysseus.iql.odl.ui.service;

import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.iql.basic.ui.service.IQLUiServiceBinding;
import de.uniol.inf.is.odysseus.iql.odl.ui.wizard.IODLTemplate;
import de.uniol.inf.is.odysseus.iql.odl.ui.wizard.ODLTemplateReader;
import de.uniol.inf.is.odysseus.iql.odl.ui.wizard.ODLTemplateRegistry;

public class ODLUiServiceBinding extends IQLUiServiceBinding {

	private static ODLUiServiceBinding instance = new ODLUiServiceBinding();
	
	public static ODLUiServiceBinding getInstance() {
		return instance;
	}

	public static void bindODLUiService(IODLUiService service) {
		if (service instanceof IODLUiService) {
			IODLUiService odlService = (IODLUiService) service;
			for (IODLTemplate template : odlService.getODLTemplates()) {
				ODLTemplateRegistry.getInstance().register(template);
			}
			for (Bundle bundle : odlService.getODLBundleTemplates()) {
				for (IODLTemplate template : ODLTemplateReader.readTemplates(bundle)) {
					ODLTemplateRegistry.getInstance().register(template);
				}
			}
		}
		getInstance().onIQLUiServiceAdded(service);
	}
	
	public static void unbindODLUiService(IODLUiService service) {
		if (service instanceof IODLUiService) {
			IODLUiService odlService = (IODLUiService) service;
			for (IODLTemplate template : odlService.getODLTemplates()) {
				ODLTemplateRegistry.getInstance().unregister(template);
			}
			for (Bundle bundle : odlService.getODLBundleTemplates()) {
				for (IODLTemplate template : ODLTemplateReader.readTemplates(bundle)) {
					ODLTemplateRegistry.getInstance().unregister(template);
				}
			}
		}
		getInstance().onIQLUiServiceRemoved(service);
	}	
	
}
