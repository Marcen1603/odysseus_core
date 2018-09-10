package de.uniol.inf.is.odysseus.rcp.editor.text.templates;

import de.uniol.inf.is.odysseus.rcp.editor.text.IOdysseusScriptTemplate;

final class EmptyScriptTemplate implements IOdysseusScriptTemplate {

	@Override
	public String getName() {
		return OdysseusScriptTemplateRegistry.EMPTY_TEMPLATE_NAME;
	}

	@Override
	public String getDescription() {
		return "An empty template to start from scratch.";
	}

	@Override
	public String getText() {
		return "";
	}

}
