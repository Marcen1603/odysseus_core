package de.uniol.inf.is.odysseus.rcp.editor.parameters;

import de.uniol.inf.is.odysseus.rcp.editor.parameter.SimpleParameterEditor;

public class LongParameterEditor extends SimpleParameterEditor<Long> {

	@Override
	public Long convertFromString(String txt) {
		return Long.valueOf(txt);
	}

	@Override
	public String convertToString(Long object) {
		if( object == null ) 
			return "";
		return object.toString();
	}

}
