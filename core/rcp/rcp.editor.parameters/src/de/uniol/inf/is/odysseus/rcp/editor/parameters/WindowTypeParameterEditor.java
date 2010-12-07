package de.uniol.inf.is.odysseus.rcp.editor.parameters;

import de.uniol.inf.is.odysseus.rcp.editor.parameter.IParameterEditor;

public class WindowTypeParameterEditor extends ReadonlyComboParameterEditor implements IParameterEditor {

	private static final String[] WINDOW_TYPES = {
		"Unbounded", "Time", "Tuple"
	};
	
	@Override
	protected String[] getList() {
		return WINDOW_TYPES;
	}
}
