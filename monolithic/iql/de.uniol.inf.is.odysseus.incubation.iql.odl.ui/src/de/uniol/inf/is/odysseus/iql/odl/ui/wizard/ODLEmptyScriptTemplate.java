package de.uniol.inf.is.odysseus.iql.odl.ui.wizard;


public class ODLEmptyScriptTemplate implements IODLTemplate{
	@Override
	public String getName() {
		return ODLTemplateRegistry.EMPTY_TEMPLATE_NAME;
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
