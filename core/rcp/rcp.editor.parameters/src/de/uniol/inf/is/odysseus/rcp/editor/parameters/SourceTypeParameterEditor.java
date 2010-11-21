package de.uniol.inf.is.odysseus.rcp.editor.parameters;

public class SourceTypeParameterEditor extends ReadonlyComboParameterEditor {

	@Override
	protected String[] getList() {
		return new String[] {"RelationalStreaming"};
	}
}
