package de.uniol.inf.is.odysseus.rcp.editor.parameters;

import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.rcp.editor.parameter.SimpleParameterEditor;

public class IntegerParameterEditor extends SimpleParameterEditor<Integer> {

	@Override
	public Integer convertFromString(String txt) {
		try {
			Integer value = Integer.valueOf(txt);
			return value;
		} catch( Exception ex ) {
			((Text)getInputControl()).setText("");
			return (Integer)getValue();
		}	
	}

	@Override
	public String convertToString(Integer object) {
		if( object == null ) 
			return "";
		return String.valueOf(object);
	}

}
