package de.uniol.inf.is.odysseus.rcp.editor.parameters;

import de.uniol.inf.is.odysseus.rcp.editor.parameter.SimpleParameterEditor;

public class IntegerParameterEditor extends SimpleParameterEditor<Integer> {

	@Override
	public Integer convertFromString(String txt) {
		return Integer.valueOf(txt);
	}

	@Override
	public String convertToString(Integer object) {
		if( object == null ) 
			return "";
		return String.valueOf(object);
	}

}
