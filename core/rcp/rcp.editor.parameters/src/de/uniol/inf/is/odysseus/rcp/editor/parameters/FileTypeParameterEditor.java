package de.uniol.inf.is.odysseus.rcp.editor.parameters;

public class FileTypeParameterEditor extends ReadonlyComboParameterEditor {

	@Override
	protected String[] getList() {
		return new String[] { "csv"};
	}
}
