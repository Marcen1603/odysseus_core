package de.uniol.inf.is.odysseus.iql.odl.ui.service;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.iql.basic.ui.service.AbstractIQLUiServiceObserver;
import de.uniol.inf.is.odysseus.iql.basic.ui.service.IIQLUiService;
import de.uniol.inf.is.odysseus.iql.odl.ui.wizard.IODLTemplate;
import de.uniol.inf.is.odysseus.iql.odl.ui.wizard.ODLTemplateReader;
import de.uniol.inf.is.odysseus.iql.odl.ui.wizard.ODLTemplateRegistry;

@Singleton
public class ODLUiServiceObserver extends AbstractIQLUiServiceObserver implements IODLUiServiceObserver{

	@Inject
	public ODLUiServiceObserver() {
		super();
		ODLUiServiceBinding.getInstance().addListener(this);
		for (IIQLUiService service : ODLUiServiceBinding.getInstance().getServices()) {
			onServiceAdded(service);
		}
	}
	
	@Override
	public void onServiceAdded(IIQLUiService service) {
		if (service instanceof IODLUiService) {
			IODLUiService odlService = (IODLUiService) service;
			if (odlService.getODLTemplates() != null) {
				for (IODLTemplate template : odlService.getODLTemplates()) {
					ODLTemplateRegistry.getInstance().register(template);
				}
			}
			if (odlService.getODLBundleTemplates() != null) {
				for (Bundle bundle : odlService.getODLBundleTemplates()) {
					for (IODLTemplate template : ODLTemplateReader.readTemplates(bundle)) {
						ODLTemplateRegistry.getInstance().register(template);
					}
				}
			}
		}
		super.onServiceAdded(service);
	}

	@Override
	public void onServiceRemoved(IIQLUiService service) {
		if (service instanceof IODLUiService) {
			IODLUiService odlService = (IODLUiService) service;
			if (odlService.getODLTemplates() != null) {
				for (IODLTemplate template : odlService.getODLTemplates()) {
					ODLTemplateRegistry.getInstance().unregister(template);
				}
			}			
			if (odlService.getODLBundleTemplates() != null) {
				for (Bundle bundle : odlService.getODLBundleTemplates()) {
					for (IODLTemplate template : ODLTemplateReader.readTemplates(bundle)) {
						ODLTemplateRegistry.getInstance().unregister(template);
					}
				}
			}
		}
		super.onServiceRemoved(service);
	}

}
