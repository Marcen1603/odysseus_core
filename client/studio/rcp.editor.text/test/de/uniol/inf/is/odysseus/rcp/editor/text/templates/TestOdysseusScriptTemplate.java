package de.uniol.inf.is.odysseus.rcp.editor.text.templates;

import de.uniol.inf.is.odysseus.rcp.editor.text.IOdysseusScriptTemplate;

public class TestOdysseusScriptTemplate implements IOdysseusScriptTemplate {

	public static final String TEMPLATE_NAME = "TestTemplate";

	@Override
	public String getName() {
		return TEMPLATE_NAME;
	}

	@Override
	public String getDescription() {
		return "Just for tests";
	}

	@Override
	public String getText() {
		return "Awesome script code here";
	}

}
