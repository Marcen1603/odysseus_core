package de.uniol.inf.is.odysseus.iql.odl.ui.service;

import java.util.Collection;

import org.osgi.framework.Bundle;

import de.uniol.inf.is.odysseus.iql.basic.ui.service.IIQLUiService;
import de.uniol.inf.is.odysseus.iql.odl.ui.wizard.IODLTemplate;

public interface IODLUiService extends IIQLUiService {
	Collection<IODLTemplate> getODLTemplates();
	Collection<Bundle> getODLBundleTemplates();

}
