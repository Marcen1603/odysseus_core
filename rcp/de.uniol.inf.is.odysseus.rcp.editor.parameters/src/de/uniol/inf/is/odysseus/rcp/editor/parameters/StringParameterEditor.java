package de.uniol.inf.is.odysseus.rcp.editor.parameters;

import de.uniol.inf.is.odysseus.rcp.editor.parameter.SimpleParameterEditor;


public class StringParameterEditor extends SimpleParameterEditor<String> {

	@Override
	public String convertFromString(String txt) {
		return txt;
	}

	@Override
	public String convertToString(String object) {
		return object;
	}

}
