package de.uniol.inf.is.odysseus.rcp.editor.text.services;

import de.uniol.inf.is.odysseus.rcp.editor.text.IOdysseusScriptTemplate;
import de.uniol.inf.is.odysseus.rcp.editor.text.templates.OdysseusScriptTemplateRegistry;

public class OdysseusScriptTemplateService {

	public void bind( IOdysseusScriptTemplate template ) {
		OdysseusScriptTemplateRegistry.getInstance().register(template);
	}
	
	public void unbind( IOdysseusScriptTemplate template ) {
		OdysseusScriptTemplateRegistry.getInstance().unregister(template);
	}
}
