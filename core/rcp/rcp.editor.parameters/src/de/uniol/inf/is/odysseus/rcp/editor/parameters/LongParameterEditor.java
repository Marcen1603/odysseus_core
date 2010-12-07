package de.uniol.inf.is.odysseus.rcp.editor.parameters;

import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.rcp.editor.parameter.SimpleParameterEditor;

public class LongParameterEditor extends SimpleParameterEditor<Long> {

	@Override
	public Long convertFromString(String txt) {
		try {
			Long value = Long.valueOf(txt);
			return value;
		} catch( Exception ex ) {
			((Text)getInputControl()).setText("");
			return (Long)getValue();
		}
	}

	@Override
	public String convertToString(Long object) {
		if( object == null ) 
			return "";
		return object.toString();
	}
	
}
